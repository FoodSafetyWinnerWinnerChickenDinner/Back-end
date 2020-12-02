package com.example.backend.services;

import com.example.backend.models.Foods;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeRepository;
import com.example.backend.services.interfaces.RecipeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Long recommendRecipeByNutrients(double carbohydrate, double protein, double fat) {
        List<Recipes> recipeDB = new ArrayList<>();
        recipeDB.addAll((Collection<? extends Recipes>) recipeRepository.findAll());



        return null;
    }

    @Override
    public Long recommendRecipeByRecommendedFood(Foods food) {
        return null;
    }

    @Override
    public void save(Foods food) {

    }

    @Override
    public void delete(Foods food) {

    }
}
