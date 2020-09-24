package com.example.backend.services;

import com.example.backend.models.Foods;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.FoodServiceInterfaces;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodService implements FoodServiceInterfaces {

    @Autowired
    private FoodOpenApiService openApiService;

    @Autowired
    private FoodRepository foodRepository;

    private static final String SERVICE_NAME = "I2790";

    @Override
    public Foods findById(Long id) {
        return foodRepository.findById(id).orElse(null);
    }

    @Override
    public void foodOpenApiProcessor() {
        String jsonText = openApiService.requestFoods();
        JSONParser parser = new JSONParser();

        try {
            JSONObject json = (JSONObject) parser.parse(jsonText);

            JSONObject jsonFood = (JSONObject) json.get(SERVICE_NAME);
            JSONArray jsonArray = (JSONArray) jsonFood.get("row");

            for(int i = 0; i < 5; i++) {
                JSONObject food = (JSONObject) jsonArray.get(i);

                System.out.println("food_name \t" + food.get("DESC_KOR"));
                System.out.println("total \t" + food.get("SERVING_SIZE"));
                System.out.println("kcal \t" + food.get("NUTR_CONT1"));
                System.out.println("carbohydrate \t" + food.get("NUTR_CONT2"));
                System.out.println("protein \t" + food.get("NUTR_CONT3"));
                System.out.println("fat \t" + food.get("NUTR_CONT4"));
                System.out.println("sugar \t" + food.get("NUTR_CONT5"));
                System.out.println("sodium \t" + food.get("NUTR_CONT6"));
                System.out.println("cholesterol \t" + food.get("NUTR_CONT7"));
                System.out.println("saturated_fatty_acid \t" + food.get("NUTR_CONT8"));
                System.out.println("trans_fat \t" + food.get("NUTR_CONT9"));
                System.out.println();

                // DB update logic
            }

        } catch (ParseException e) {
            System.out.println("failed!");
            e.printStackTrace();
        }
    }

    @Override
    public void save(Foods account) {

    }

    @Override
    public void delete(Foods account) {

    }
}
