package com.example.backend.services;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.models.Foods;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.db_access.DataBaseAccessible;
import com.example.backend.services.interfaces.openapi.OpenApiConnectable;
import com.example.backend.util_components.Cast;
import com.example.backend.util_components.LastIndexTracker;
import com.example.backend.util_components.OpenApiConnectorByWebClient;
import com.example.backend.util_components.OpenApiJsonDataParse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.UnknownContentTypeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class FoodOpenApi implements OpenApiConnectable, DataBaseAccessible {

    private final FoodRepository foodRepository;

    private final OpenApiConfig foodApi;

    private final Cast cast;
    private final OpenApiJsonDataParse openApiJsonDataParse;
    private final OpenApiConnectorByWebClient byWebClient;
    private final LastIndexTracker tracker;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public void updateByOpenApiData() throws ExecutionException, InterruptedException {
        int start = 1;
        int end = INTERVAL;

        final int SIZE = tracker.findTag(foodApi.getKey(), foodApi.getNutrientServiceName());

//        List<CompletableFuture<String>> futures = new ArrayList<>();
        List<String> responses = new ArrayList<>();

        while(start <= SIZE) {
            end = Math.min(end, SIZE);

            try {

//                int finalStart = start;
//                int finalEnd = end;
//
//                Callable<CompletableFuture<String>> callable = () ->
//                        byWebClient
//                                .requestOpenApiData(foodApi.getKey(), foodApi.getNutrientServiceName(), finalStart, finalEnd);
//
//                futures.add(Executors.newCachedThreadPool().submit(callable).get());

                responses.add(byWebClient.requestOpenApiData(foodApi.getKey(), foodApi.getNutrientServiceName(), start ,end));

            }
            catch (UnknownContentTypeException unknownContentTypeException) {
                LOGGER.error(">>> Open API >> End of pages >> ", unknownContentTypeException);
                break;
            }

            start += INTERVAL;
            end += INTERVAL;
        }

//        for(CompletableFuture<String> future: futures) {

//            JSONArray jsonFood
//                    = openApiJsonDataParse.jsonDataParser(foodApi.getNutrientServiceName(), future.get());

        for(String response: responses){

            JSONArray jsonFood
                    = openApiJsonDataParse.jsonDataParser(foodApi.getNutrientServiceName(), response);

            if(jsonFood == null) break;
            List<Foods> apiDataList = new ArrayList<>();

            for (Object obj: jsonFood) {
                JSONObject food = (JSONObject) obj;

                Foods apiData = (Foods) jsonToModel(food);
                if(isContainsField(apiData)) {
                    continue;
                }

                apiDataList.add(apiData);
            }

            saveAll(apiDataList);
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
