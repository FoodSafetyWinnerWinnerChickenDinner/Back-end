package com.example.backend.services.interfaces;

import com.example.backend.models.Foods;
import com.example.backend.models.data_enums.Nutrients;

import java.util.HashMap;
import java.util.List;


public interface FoodService {

    HashMap<String, Nutrients> categorySetter();

    double[] ingestedTotalNutrientsGetter(List<String> eats, HashMap<String, Nutrients> categories);

    List<Foods> foodListExtractFromDB();

    List<Foods> menuRecommendation(double[] ingested, List<Foods> foodDB);

    void save(Foods food);

    void delete(Foods food);
}
