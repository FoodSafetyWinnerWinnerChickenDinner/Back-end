package com.example.backend.services;

import com.example.backend.models.Foods;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.FoodService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodOpenApiServiceImpl openApiService;

    @Autowired
    private FoodRepository foodRepository;

    private static final String SERVICE_NAME = "I2790";
    private static final String LIST_FLAG = "row";
    private static final int INTERVAL = 200;
    private static final int LAST_INDEX = 29_274;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public Foods findById(Long id) {
        return foodRepository.findById(id).orElse(null);
    }

    @Override
    public void dataUpdateProcessorByFoodOpenApi() {
        int start = 1;
        int end = INTERVAL;

        while(start <= LAST_INDEX) {
            String jsonText = openApiService.requestFoods(start, end);
            JSONParser parser = new JSONParser();

            try {
                JSONObject json = (JSONObject) parser.parse(jsonText);

                JSONObject jsonFood = (JSONObject) json.get(SERVICE_NAME);
                JSONArray jsonArray = (JSONArray) jsonFood.get(LIST_FLAG);

                int size = jsonArray.size();
                for (int i = 0; i < size; i++) {
                    JSONObject food = (JSONObject) jsonArray.get(i);

                    Foods apiData = new Foods();

                    apiData.setFoodName(food.get("DESC_KOR").toString());
                    apiData.setCategory(food.get("GROUP_NAME").toString());
                    apiData.setTotal(validation(food.get("SERVING_SIZE").toString()));
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
                System.out.println(size);
            } catch (ParseException parseException) {
                LOGGER.error(">>> FoodsServiceImpl >> exception >> ", parseException);
                parseException.printStackTrace();
            }

            start += INTERVAL;
            end += INTERVAL;
        }
    }

    @Override
    public ArrayList<String> menuRecommendation() {
        /*
            another methods called, and there needs @Autowired NutrientConfigs;
            Nutrients nutrients = nutrientConfigs.getFoodName();
            compare data from front with Nutrients(enum) -> make list and return

            here TODO: logic -> recommendation menu, nutrient base requiring ingestion
        */

        return null;
    }


    @Override
    public void save(Foods food) {
        foodRepository.save(food);
    }

    @Override
    public void delete(Foods food) {
        foodRepository.delete(food);
    }

    @Override
    public double validation(String data) {
        if(data.length() == 0) return 0.0;
        else return Double.parseDouble(data);
    }
}
