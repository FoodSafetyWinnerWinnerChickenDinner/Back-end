package com.example.backend.services;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.RestTemplateConfig;
import com.example.backend.models.Foods;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeOpenApiRepository;
import com.example.backend.services.interfaces.DataAccessible;
import com.example.backend.services.interfaces.OpenApiConnectable;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecipeOpenApi implements OpenApiConnectable, DataAccessible {

    private final OpenApiConfig recipeApi;

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

        while(true) {

            if(start > lastIndex) break;

            String jsonText = requestOpenApiData(start, end);
            JSONParser parser = new JSONParser();

            try {

                JSONObject json = (JSONObject) parser.parse(jsonText);
                JSONObject jsonFood = (JSONObject) json.get(recipeApi.getNutrientServiceName());

                if (lastIndex == INIT) {
                    lastIndex = Integer.parseInt(jsonFood.get(TOTAL).toString());
                }

                JSONArray jsonArray = (JSONArray) jsonFood.get(LIST_FLAG);
                List<Recipes> apiDataList = new ArrayList<>();

                for (Object obj: jsonArray) {
                    JSONObject recipe = (JSONObject) obj;

                    Recipes apiData = (Recipes) jsonToModel(recipe);
                    if(dbContainsData(apiData)) {
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
        urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(recipeApi.getNutrientServiceName(), ENCODING_TYPE));
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

        Recipes recipe = new Recipes();

        return recipe;
    }

    @Override
    public Object valueValidator(Object value) {
        String str = value.toString();

        if(str.length() == 0 || str == null){
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
        return null;
    }

    @Override
    public boolean dbContainsData(Object object) {
        Recipes recipe = (Recipes) object;

//        String name = food.getFoodName();
//        String category = food.getCategory();

        // return repository
        return false;
    }

    public void saveAll(List<Recipes> recipeList) {
        if(recipeList.size() == 0) return;
        recipeOpenApiRepository.saveAll(recipeList);
    }
}
