package com.example.backend.services.interfaces;

import com.example.backend.models.Foods;


public interface FoodService {
    public Foods findById(Long id);

    public void dataUpdateProcessorByFoodOpenApi();

    public void save(Foods food);

    public void delete(Foods food);

    public double validation(String data);
}
