package com.example.backend.services;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.RestTemplateConfig;
import com.example.backend.models.ManualPairs;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeRepository;
import com.example.backend.services.interfaces.db_access.DataBaseAccessible;
import com.example.backend.services.interfaces.openapi.OpenApiConnectable;
import com.example.backend.utils.Cast;
import com.example.backend.utils.OpenApiJsonDataParse;
import com.example.backend.utils.OpenApiUrlBuilder;
import com.example.backend.utils.PairMaker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipeOpenApi implements OpenApiConnectable, DataBaseAccessible {

    private final RecipeRepository recipeRepository;

    private final OpenApiConfig recipeApi;
    private final RestTemplateConfig restTemplate;

    private final PairMaker pairMaker;
    private final Cast cast;
    private final OpenApiUrlBuilder urlBuilder;
    private final OpenApiJsonDataParse openApiJsonDataParse;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public String requestOpenApiData(int start, int end) {
        String jsonInString = null;
        Map<String, Object> jsonData = new HashMap<>();

        try {
            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            String openApiUrl = urlBuilder.openApiUrlBuilder(
                    recipeApi.getUrl(),
                    recipeApi.getKey(),
                    recipeApi.getRecipeServiceName(), start, end);

            ResponseEntity<Map> resultMap = restTemplate.getCustomRestTemplate()
                    .exchange(openApiUrl, HttpMethod.GET, entity, Map.class);

            jsonData.put(STATUS_CODE, resultMap.getStatusCodeValue());   // http status
            jsonData.put(HEADER, resultMap.getHeaders());               // header
            jsonData.put(BODY, resultMap.getBody());                    // body

            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());

        }
        catch (UnsupportedEncodingException | JsonProcessingException unsupportedEncodingException) {
            LOGGER.error(">>> RecipeOpenApi >> exception >> ", unsupportedEncodingException);
        }

        return jsonInString;
    }

    @Override
    public void updateByOpenApiData() {
        int start = 1;
        int end = INTERVAL;

        int lastIndex = INIT;

        while(end <= lastIndex) {

            String jsonText = requestOpenApiData(start, end);
            Map<JSONArray, Integer> jsonMap
                    = openApiJsonDataParse.jsonDataParser(recipeApi.getRecipeServiceName(), jsonText, lastIndex);

            JSONArray jsonArray = null;

            for(Map.Entry<JSONArray, Integer> entry: jsonMap.entrySet()) {
                jsonArray = entry.getKey();
                lastIndex = entry.getValue();
            }

            if(jsonArray == null) break;
            List<Recipes> apiDataList = new ArrayList<>();

            for (Object obj: jsonArray) {
                JSONObject recipe = (JSONObject) obj;

                Recipes apiData = (Recipes) jsonToModel(recipe);
                if(isContainsField(apiData)) {
                    continue;
                }

                apiDataList.add(apiData);
            }

            saveAll(apiDataList);

            start += INTERVAL;
            end += INTERVAL;

        }
    }

    @Override
    public Object jsonToModel(JSONObject object) {
        List<Object> values = new ArrayList<>();

        for(final String FORMAT: RECIPE_JSON_FORMATS){
            values.add(cast.valueValidator(object.get(FORMAT)));
        }

        List<ManualPairs> pairsList = pairMaker.pairListBuilder(object);

        return Recipes.builder()
                .id(cast.toLong(values.get(0)))
                .recipeName(cast.toString(values.get(1))).category(cast.toString(values.get(2)))
                .cookingMaterialExample(cast.toString(values.get(3))).cookingCompletionExample(cast.toString(values.get(4)))
                .ingredients(cast.toString(values.get(5))).cookingMethod(cast.toString(values.get(6)))
                .kcal(cast.toDouble(values.get(7)))
                .carbohydrate(cast.toDouble(values.get(8))).protein(cast.toDouble(values.get(9))).fat(cast.toDouble(values.get(10)))
                .sodium(cast.toDouble(values.get(11)))
                .manualPairsList(pairsList)
                .build();

    }

    @Override
    public boolean isContainsField(Object object) {
        Recipes recipe = (Recipes) object;
        long id = recipe.getId();

        return recipeRepository.existsById(id);
    }

    @Override
    public void saveAll(List<?> recipeList) {
        if(recipeList.size() == 0) return;

        recipeRepository.saveAll(Arrays.asList(
                recipeList.stream()
                        .toArray(Recipes[]::new)
                        .clone()
        ));
    }
}
