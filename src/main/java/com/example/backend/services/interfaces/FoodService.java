package com.example.backend.services.interfaces;

import com.example.backend.models.Foods;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public interface FoodService {
    public Foods findById(Long id);

    public void dataUpdateProcessorByFoodOpenApi();

    public void categorySetter();

    public double[] ingestedTotalNutrientsGetter(List<String> eats);

    public void foodListUpdater();

    public double priorityCalculator(double carbohydrate, double protein, double fat);

    public void exceptCategorySetter();

    public ArrayList<Foods>[] extractCandidates(double[] ingested);

    public ArrayList<Foods> menuRecommendation(ArrayList<Foods>[] candidates);

    public void save(Foods food);

    public void delete(Foods food);

    public double validation(String data);
}
