package com.example.backend.services.interfaces;

import com.example.backend.models.Foods;

import java.util.ArrayList;
import java.util.List;


public interface FoodService {
    Foods findById(Long id);

    Foods findByNameAndCategory(String name, String category, double total);

    Long countAllData();

    void dataUpdateProcessorByFoodOpenApi();

    void categorySetter();

    double[] ingestedTotalNutrientsGetter(List<String> eats);

    void foodListUpdater();

    void exceptCategorySetter();

    double priorCalculator(Foods food, double carbohydrate, double protein, double fat);

    ArrayList<Foods>[] extractCandidates(double[] ingested);

    ArrayList<Foods> menuRecommendation(ArrayList<Foods>[] candidates);

    void save(Foods food);

    void delete(Foods food);

    double validation(String data);
}
