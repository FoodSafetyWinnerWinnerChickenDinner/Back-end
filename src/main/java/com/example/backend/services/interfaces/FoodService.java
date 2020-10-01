package com.example.backend.services.interfaces;

import com.example.backend.models.Foods;

import java.util.ArrayList;


public interface FoodService {
    public Foods findById(Long id);

    public void dataUpdateProcessorByFoodOpenApi();

    public void foodListUpdater();

    public ArrayList<String> menuRecommendation();

    public void save(Foods food);

    public void delete(Foods food);

    public double validation(String data);
}
