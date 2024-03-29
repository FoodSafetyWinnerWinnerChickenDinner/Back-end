package com.example.backend.services.service_foods;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.models.Foods;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.service_foods.interface_foods.FoodOpenApiService;
import com.example.backend.util_components.util_connector.OpenApiConnectorByWebClient;
import com.example.backend.util_components.util_string.Casting;
import com.example.backend.util_components.util_string.parse.LastIndexTracker;
import com.example.backend.util_components.util_string.parse.OpenApiJsonDataParse;
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
public class FoodOpenApiServiceImpl implements FoodOpenApiService {

    private final FoodRepository foodRepository;

    private final OpenApiConfig foodApi;

//    private final AsyncOpenApiConnectorByWebClient asyncOpenApiConnectorByWebClient;
    private final OpenApiConnectorByWebClient byWebClient;
    private final Casting casting;
    private final OpenApiJsonDataParse openApiJsonDataParse;
    private final LastIndexTracker tracker;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public void updateByOpenApiData() throws ExecutionException, InterruptedException {
        int start = 1;
        int end = INTERVAL;

        final int SIZE = tracker.findTag(foodApi.getKey1(), foodApi.getNutrientServiceName());
        List<String> responses = new ArrayList<>();

        while(start <= SIZE) {
            end = Math.min(end, SIZE);

            try {

                responses.add(byWebClient.requestOpenApiData(foodApi.getKey1(), foodApi.getNutrientServiceName(), start ,end));

            }
            catch (UnknownContentTypeException unknownContentTypeException) {
                LOGGER.error(">>> Open API >> End of pages >> ", unknownContentTypeException);
                break;
            }

            start += INTERVAL;
            end += INTERVAL;
        }

        for(String response: responses){

            JSONArray jsonFood = openApiJsonDataParse
                    .jsonDataParser(foodApi.getNutrientServiceName(), response);

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
            values.add(casting.valueValidator(object.get(FORMAT)));
        }

        return Foods.builder()
                .id(casting.toLong(values.get(0)))
                .foodName(casting.toString(values.get(1))).category(casting.toString(values.get(2)))
                .total(casting.toDouble(values.get(3))).kcal(casting.toDouble(values.get(4)))
                .carbohydrate(casting.toDouble(values.get(5))).protein(casting.toDouble(values.get(6))).fat(casting.toDouble(values.get(7)))
                .sugar(casting.toDouble(values.get(8))).sodium(casting.toDouble(values.get(9))).cholesterol(casting.toDouble(values.get(10)))
                .saturatedFattyAcid(casting.toDouble(values.get(11))).transFat(casting.toDouble(values.get(12)))
                .build();
    }

    @Override
    public boolean isContainsField(Object object) {
        Foods food = (Foods) object;
        long id = food.getId();

        return foodRepository.existsById(id);
    }
}
