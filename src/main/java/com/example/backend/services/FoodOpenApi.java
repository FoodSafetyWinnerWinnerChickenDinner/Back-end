package com.example.backend.services;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.RestTemplateConfig;
import com.example.backend.models.Foods;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.db_access.DataBaseAccessible;
import com.example.backend.services.interfaces.openapi.OpenApiConnectable;
import com.example.backend.utils.Cast;
import com.example.backend.utils.OpenApiJsonDataParse;
import com.example.backend.utils.OpenApiUrlBuilder;
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
public class FoodOpenApi implements OpenApiConnectable, DataBaseAccessible {

    private final FoodRepository foodRepository;

    private final RestTemplateConfig restTemplate;
    private final OpenApiConfig foodApi;

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
                    foodApi.getUrl(),
                    foodApi.getKey(),
                    foodApi.getNutrientServiceName() ,start ,end);

            ResponseEntity<Map> resultMap = restTemplate.getCustomRestTemplate()
                    .exchange(openApiUrl, HttpMethod.GET, entity, Map.class);

            jsonData.put(STATUS_CODE, resultMap.getStatusCodeValue());   // http status
            jsonData.put(HEADER, resultMap.getHeaders());               // header
            jsonData.put(BODY, resultMap.getBody());                    // body

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

        while(end <= lastIndex) {

            String jsonText = requestOpenApiData(start, end);
            Map<JSONArray, Integer> jsonMap
                    = openApiJsonDataParse.jsonDataParser(foodApi.getNutrientServiceName(), jsonText, lastIndex);

            JSONArray jsonArray = null;

            for(Map.Entry<JSONArray, Integer> entry: jsonMap.entrySet()) {
                jsonArray = entry.getKey();
                lastIndex = entry.getValue();
            }

            if(jsonArray == null) break;
            List<Foods> apiDataList = new ArrayList<>();

            for (Object obj: jsonArray) {
                JSONObject food = (JSONObject) obj;

                Foods apiData = (Foods) jsonToModel(food);
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
    public void saveAll(List<?> foodList) {
        if(foodList.size() == 0) return;

        foodRepository.saveAll(Arrays.asList(
                foodList.stream()
                        .toArray(Foods[]::new)
                        .clone()
        ));
    }

    @Override
    public Object jsonToModel(JSONObject object) {
        List<Object> values = new ArrayList<>();

        for(final String FORMAT: FOOD_JSON_FORMATS){
            values.add(cast.valueValidator(object.get(FORMAT)));
        }

        return Foods.builder()
                .id(cast.toLong(values.get(0)))
                .foodName(cast.toString(values.get(1))).category(cast.toString(values.get(2)))
                .total(cast.toDouble(values.get(3))).kcal(cast.toDouble(values.get(4)))
                .carbohydrate(cast.toDouble(values.get(5))).protein(cast.toDouble(values.get(6))).fat(cast.toDouble(values.get(7)))
                .sugar(cast.toDouble(values.get(8))).sodium(cast.toDouble(values.get(9))).cholesterol(cast.toDouble(values.get(10)))
                .saturatedFattyAcid(cast.toDouble(values.get(11))).transFat(cast.toDouble(values.get(12)))
                .build();
    }

    @Override
    public boolean isContainsField(Object object) {
        Foods food = (Foods) object;
        long id = food.getId();

        return foodRepository.existsById(id);
    }
}
