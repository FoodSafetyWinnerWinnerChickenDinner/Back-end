package com.example.backend.services.interfaces;

import com.example.backend.models.Foods;
import com.example.backend.models.data_enums.Nutrients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public interface FoodService {

    Foods findByNameAndCategory(String name, String category, double total);

    void dataUpdateProcessorByFoodOpenApi();

    HashMap<String, Nutrients> categorySetter();

    double[] ingestedTotalNutrientsGetter(List<String> eats, HashMap<String, Nutrients> categories);

    ArrayList<Foods> foodListExtractFromDB();

    HashSet<String> exceptCategorySetter();

    double priorCalculator(Foods food, double carbohydrate, double protein, double fat);

    ArrayList<Foods>[] extractCandidates(double[] ingested, ArrayList<Foods> foodDB, HashSet<String> except);

    ArrayList<Foods> menuRecommendation(ArrayList<Foods>[] candidates);

    void save(Foods food);

    void delete(Foods food);

    double validation(String data);
}
