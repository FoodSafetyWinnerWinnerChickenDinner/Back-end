package com.example.backend.services;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.HttpConnectionConfig;
import com.example.backend.models.Foods;
import com.example.backend.repositories.FoodOpenApiRepository;
import com.example.backend.services.interfaces.FoodOpenApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class FoodOpenApiServiceImpl implements FoodOpenApiService {

    private final OpenApiConfig foodApi;

    private final FoodOpenApiRepository foodOpenApiRepository;

    private final HttpConnectionConfig restTemplate;

    private static final String TYPE = "json";
    private static final String FORWARD_SLASH = "/";
    private static final String ENCODING_TYPE = "UTF-8";

    private final String LIST_FLAG = "row";
    private final int INTERVAL = 200;
    private final int LAST_INDEX = 28_488;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Autowired
    public FoodOpenApiServiceImpl(HttpConnectionConfig restTemplate, OpenApiConfig foodApi, FoodOpenApiRepository foodOpenApiRepository) {
        this.restTemplate = restTemplate;
        this.foodApi = foodApi;
        this.foodOpenApiRepository = foodOpenApiRepository;
    }

    @Override
    public void nutrientsDataBaseUpdateProcessorByFoodOpenApi() {
        int start = 1;
        int end = INTERVAL;

        while(start <= LAST_INDEX) {
            String jsonText = requestFoodLists(start, end);
            JSONParser parser = new JSONParser();

            try {
                JSONObject json = (JSONObject) parser.parse(jsonText);

                JSONObject jsonFood = (JSONObject) json.get(foodApi.getNutrientServiceName());
                JSONArray jsonArray = (JSONArray) jsonFood.get(LIST_FLAG);

                int size = jsonArray.size();
                for (int i = 0; i < size; i++) {
                    JSONObject food = (JSONObject) jsonArray.get(i);

                    Foods apiData = new Foods();
                    String foodName = food.get("DESC_KOR").toString();
                    String category = food.get("GROUP_NAME").toString();
                    double total = validation(food.get("SERVING_SIZE").toString());

                    if(findByNameAndCategory(foodName, category, total) != null) continue;

                    apiData.setFoodName(foodName);
                    apiData.setCategory(category);
                    apiData.setTotal(total);
                    apiData.setKcal(validation(food.get("NUTR_CONT1").toString()));
                    apiData.setCarbohydrate(validation(food.get("NUTR_CONT2").toString()));
                    apiData.setProtein(validation(food.get("NUTR_CONT3").toString()));
                    apiData.setFat(validation(food.get("NUTR_CONT4").toString()));
                    apiData.setSugar(validation(food.get("NUTR_CONT5").toString()));
                    apiData.setSodium(validation(food.get("NUTR_CONT6").toString()));
                    apiData.setCholesterol(validation(food.get("NUTR_CONT7").toString()));
                    apiData.setSaturatedFattyAcid(validation(food.get("NUTR_CONT8").toString()));
                    apiData.setTransFat(validation(food.get("NUTR_CONT9").toString()));

                    save(apiData);
                }

            } catch (ParseException parseException) {
                LOGGER.error(">>> FoodsServiceImpl >> exception >> ", parseException);
                parseException.printStackTrace();
            }

            start += INTERVAL;
            end += INTERVAL;
        }
    }

    @Override
    public String requestFoodLists(int startIndex, int endIndex) {
        String jsonInString = null;
        Map<String, Object> jsonData = new HashMap<>();
        StringBuilder urlBuilder = new StringBuilder(foodApi.getUrl());

        try {
            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(foodApi.getKey(), ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(foodApi.getNutrientServiceName(), ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(TYPE, ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(startIndex);
            urlBuilder.append(FORWARD_SLASH).append(endIndex);

            ResponseEntity<Map> resultMap = restTemplate.getCustomRestTemplate()
                    .exchange(urlBuilder.toString(), HttpMethod.GET, entity, Map.class);

            jsonData.put("statusCode", resultMap.getStatusCodeValue());   // http status
            jsonData.put("header", resultMap.getHeaders());               // header
            jsonData.put("body", resultMap.getBody());                    // body

            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());
        }
        catch (UnsupportedEncodingException | JsonProcessingException unsupportedEncodingException) {
            LOGGER.error(">>> FoodOpenApiServiceImpl >> exception >> ", unsupportedEncodingException);
        }

        return jsonInString;
    }

    @Override
    public void save(Foods food) {
        foodOpenApiRepository.save(food);
    }

    @Override
    public void delete(Foods food) {
        foodOpenApiRepository.delete(food);
    }

    @Override
    public double validation(String data) {
        if(data.length() == 0) return 0.0;
        else return Double.parseDouble(data);
    }

    @Override
    public Foods findByNameAndCategory(String name, String category, double total) {
        return foodOpenApiRepository.findByNameAndCategory(name, category, total);
    }
}
