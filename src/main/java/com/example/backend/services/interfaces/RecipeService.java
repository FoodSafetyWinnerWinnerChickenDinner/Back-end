package com.example.backend.services.interfaces;

import com.example.backend.models.Foods;

public interface RecipeService {

    Long recommendRecipeByNutrients(double carbohydrate, double protein, double fat);

    Long recommendRecipeByRecommendedFood(Foods food);

    void save(Foods food);

    void delete(Foods food);
}
