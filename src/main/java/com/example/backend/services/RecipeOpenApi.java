package com.example.backend.services;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.RestTemplateConfig;
import com.example.backend.models.Foods;
import com.example.backend.models.ManualPairs;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeOpenApiRepository;
import com.example.backend.services.interfaces.DataBaseAccessible;
import com.example.backend.services.interfaces.JsonDataPreservable;
import com.example.backend.services.interfaces.OpenApiConnectable;
import com.example.backend.services.interfaces.TypeConvertable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipeOpenApi implements OpenApiConnectable, JsonDataPreservable
        , TypeConvertable, DataBaseAccessible {

    private final OpenApiConfig recipeApi;

    private final PairMaker pairMaker;

    private final RecipeOpenApiRepository recipeOpenApiRepository;

    private final RestTemplateConfig restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public String requestOpenApiData(final int START, final int END) {
        String jsonInString = null;
        Map<String, Object> jsonData = new HashMap<>();

        try {
            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            String foodOpenApiUrl = openApiUrlBuilder(START, END);

            ResponseEntity<Map> resultMap = restTemplate.getCustomRestTemplate()
                    .exchange(foodOpenApiUrl, HttpMethod.GET, entity, Map.class);

            jsonData.put("statusCode", resultMap.getStatusCodeValue());   // http status
            jsonData.put("header", resultMap.getHeaders());               // header
            jsonData.put("body", resultMap.getBody());                    // body

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

            String jsonText = requestOpenApiData(start, Math.min(end, lastIndex));
            JSONParser parser = new JSONParser();

            try {

                JSONObject json = (JSONObject) parser.parse(jsonText);
                JSONObject jsonRecipe = (JSONObject) json.get(recipeApi.getRecipeServiceName());

                if (lastIndex == INIT) {
                    lastIndex = Integer.parseInt(jsonRecipe.get(TOTAL).toString());
                }

                JSONArray jsonArray = (JSONArray) jsonRecipe.get(LIST_FLAG);
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

            } catch (ParseException parseException) {
                LOGGER.error(">>> RecipeOpenApi >> exception >> ", parseException);
                parseException.printStackTrace();
            }

            start += INTERVAL;
            end += INTERVAL;

        }
    }

    @Override
    public String openApiUrlBuilder(final int START, final int END) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(recipeApi.getUrl());

        urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(recipeApi.getKey(), ENCODING_TYPE));
        urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(recipeApi.getRecipeServiceName(), ENCODING_TYPE));
        urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(FORMAT_TYPE, ENCODING_TYPE));
        urlBuilder.append(FORWARD_SLASH).append(START);
        urlBuilder.append(FORWARD_SLASH).append(END);

        return urlBuilder.toString();
    }

    @Override
    public Object jsonToModel(JSONObject object) {
        List<Object> values = new ArrayList<>();

        for(final String FORMAT: RECIPE_JSON_FORMATS){
            values.add(valueValidator(object.get(FORMAT)));
        }

        List<ManualPairs> pairsList = pairMaker.pairListBuilder(object);

        return Recipes.builder()
                .id(toLong(values.get(0)))
                .recipeName(toString(values.get(1))).category(toString(values.get(2)))
                .cookingMaterialExample(toString(values.get(3))).cookingCompletionExample(toString(values.get(4)))
                .ingredients(toString(values.get(5))).cookingMethod(toString(values.get(6)))
                .kcal(toDouble(values.get(7)))
                .carbohydrate(toDouble(values.get(8))).protein(toDouble(values.get(9))).fat(toDouble(values.get(10))).sodium(toDouble(values.get(11)))
                .manualPairsList(pairsList)
                .build();
    }

    @Override
    public Object valueValidator(Object value) {
        if(value == null){
            return EMPTY_STRING;
        }

        String str = value.toString();
        if(str.length() == 0){
            return EMPTY_STRING;
        }

        return value;
    }

    @Override
    public String toString(Object value) {
        return value.toString();
    }

    @Override
    public Double toDouble(Object value) {
        String valueString = value.toString();

        if(valueString.matches(IS_NUMERIC)) {
            return Double.parseDouble(valueString);
        }

        return 0.0;
    }

    @Override
    public Long toLong(Object value) {
        String valueString = toString(value);

        if(valueString.matches(IS_NUMERIC)) {
            return Long.parseLong(valueString);
        }

        return 0L;
    }

    @Override
    public boolean isContainsField(Object object) {
        Recipes recipe = (Recipes) object;
        long id = recipe.getId();

        return recipeOpenApiRepository.findById(id).isPresent();
    }

    @Override
    public void saveAll(List<?> recipeList) {
        if(recipeList.size() == 0) return;

        recipeOpenApiRepository.saveAll(Arrays.asList(
                recipeList.stream()
                        .toArray(Recipes[]::new)
                        .clone()
        ));
    }
}
