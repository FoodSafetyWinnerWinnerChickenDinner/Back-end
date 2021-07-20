package com.example.backend.services;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.RestTemplateConfig;
import com.example.backend.models.Foods;
import com.example.backend.repositories.FoodOpenApiRepository;
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
public class FoodOpenApi implements OpenApiConnectable, DataAccessible {

    private final OpenApiConfig foodApi;

    private final FoodOpenApiRepository foodOpenApiRepository;

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
            LOGGER.error(">>> FoodOpenApi >> exception >> ", unsupportedEncodingException);
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
                JSONObject jsonFood = (JSONObject) json.get(foodApi.getNutrientServiceName());

                if (start == INIT) {
                    lastIndex = Integer.parseInt(jsonFood.get(TOTAL).toString());
                }

                JSONArray jsonArray = (JSONArray) jsonFood.get(LIST_FLAG);
                List<Foods> apiDataList = new ArrayList<>();

                for (Object obj: jsonArray) {
                    JSONObject food = (JSONObject) obj;

                    Foods apiData = (Foods) jsonToModel(food);
                    if(dbContainsData(apiData)) {
                        continue;
                    }

                    apiDataList.add(apiData);
                }

                saveAll(apiDataList);

            } catch (ParseException parseException) {
                LOGGER.error(">>> FoodOpenApi >> exception >> ", parseException);
                parseException.printStackTrace();
            }

            start += INTERVAL;
            end += INTERVAL;
        }
    }

    @Override
    public String openApiUrlBuilder(final int START, final int END) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(foodApi.getUrl());

        urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(foodApi.getKey(), ENCODING_TYPE));
        urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(foodApi.getNutrientServiceName(), ENCODING_TYPE));
        urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(FORMAT_TYPE, ENCODING_TYPE));
        urlBuilder.append(FORWARD_SLASH).append(START);
        urlBuilder.append(FORWARD_SLASH).append(END);

        return urlBuilder.toString();
    }

    public void saveAll(List<Foods> foodList) {
        if(foodList.size() == 0) return;
        foodOpenApiRepository.saveAll(foodList);
    }

    @Override
    public Object jsonToModel(JSONObject object) {
        List<Object> values = new ArrayList<>();

        for(final String FORMAT: FOOD_JSON_FORMATS){
            values.add(valueValidator(object.get(FORMAT)));
        }

        return Foods.builder()
                .id(toLong(values.get(0)))
                .foodName(toString(values.get(1))).category(toString(values.get(2)))
                .total(toDouble(values.get(3))).kcal(toDouble(values.get(4)))
                .carbohydrate(toDouble(values.get(5))).protein(toDouble(values.get(6))).fat(toDouble(values.get(7)))
                .sugar(toDouble(values.get(8))).sodium(toDouble(values.get(9))).cholesterol(toDouble(values.get(10)))
                .saturatedFattyAcid(toDouble(values.get(11))).transFat(toDouble(values.get(12)))
                .build();
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
        String valueString = toString(value);

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
    public boolean dbContainsData(Object object) {
        Foods food = (Foods) object;

        long id = food.getId();

        return foodOpenApiRepository.findById(id) != null;
    }
}
