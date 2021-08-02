package com.example.backend.controllers;

import com.example.backend.models.Foods;
import com.example.backend.services.service_foods.FoodServiceImpl;
import com.example.backend.services.service_nutrients.NutrientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodServiceImpl foodServiceImpl;

    private final NutrientServiceImpl nutrientServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @PostMapping("/recommend")
    public ResponseEntity<List<Foods>> menuRecommender(@RequestBody Map<String, Integer> ate) {
        List<Foods> recommends = null;

        try {

            double[] ingestedAvg = nutrientServiceImpl.ingestedNutrientsAvg(ate);
            recommends = foodServiceImpl.menuRecommender(ingestedAvg);

        }
        catch (NullPointerException nullPointerException) {
            LOGGER.error(">>> FoodController >> exception >> ", nullPointerException, " >> wrong food list !!");
            nullPointerException.printStackTrace();
        }

        return new ResponseEntity(recommends, HttpStatus.OK);
    }
}
