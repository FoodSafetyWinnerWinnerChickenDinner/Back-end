package com.example.backend.controllers;

import com.example.backend.models.Recipes;
import com.example.backend.services.RecipeService;
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
@RequestMapping("recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @PostMapping("/recommend")
    public ResponseEntity<List<Recipes>> menuRecommender(@RequestBody Map<String, Double> ate) {
        List<Recipes> recommend = null;

        try {

            double[] ingested = {ate.get("Carbohydrate"), ate.get("Protein"), ate.get("Fat")};
            recommend = recipeService.menuRecommender(ingested);

        }
        catch (NullPointerException nullPointerException) {
            LOGGER.error(">>> FoodController >> exception >> ", nullPointerException, " >> wrong food list !!");
            nullPointerException.printStackTrace();
        }

        return new ResponseEntity(recommend, HttpStatus.OK);
    }
}
