package com.example.backend.services.interfaces;

import com.example.backend.models.Foods;

import java.util.ArrayList;
import java.util.List;


public interface FoodService {
    public Foods findById(Long id);

    public void dataUpdateProcessorByFoodOpenApi();

    public void categorySetter();

    public double[] ingestedTotalNutrientsGetter(List<String> eats);

    public void foodListUpdater();

    public ArrayList<String> menuRecommendation(double[] ingested);

    public void save(Foods food);

    public void delete(Foods food);

    public double validation(String data);
}
