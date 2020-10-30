package com.example.backend.services.interfaces;

import com.example.backend.models.Foods;

import java.net.MalformedURLException;

public interface FoodOpenApiService {
    void nutrientsDataBaseUpdateProcessorByFoodOpenApi();

    String requestFoodLists(int startIndex, int endIndex) throws MalformedURLException;

    void save(Foods food);

    void delete(Foods food);

    double validation(String data);

    Foods findByNameAndCategory(String name, String category, double total);
}
