package com.example.backend.services.service_nutrients;

import com.example.backend.models.data_enums.Nutrients;
import com.example.backend.services.service_nutrients.interface_nutrients.NutrientService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NutrientServiceImpl implements NutrientService {

    @Override
    public Map<String, Nutrients> configurationSettingsCategorize(){
        Map<String, Nutrients> categories = new HashMap<>();

        for(Nutrients nutrient:
                Stream
                        .of(Nutrients.values())
                        .collect(Collectors.toList())) {

            categories.put(NUT_FORMATS[nutrient.getSequence()], nutrient);
        }

        return categories;
    }

    @Override
    public List<Object> ingestedNutrientsCategoryArranger(Nutrients nutrient) {
        return Stream.builder()
                .add(nutrient.getTotal()).add(nutrient.getKcal())
                .add(nutrient.getCarbohydrate()).add(nutrient.getProtein()).add(nutrient.getFat())
                .add(nutrient.getSugar()).add(nutrient.getSodium()).add(nutrient.getCholesterol())
                .add(nutrient.getSaturatedFattyAcid()).add(nutrient.getTransFat())
                .build()
                .collect(Collectors.toList());
    }

    public double[] ingestedNutrientsAvg(Map<String, Integer> ate) {
        Map<String, Nutrients> categories = configurationSettingsCategorize();
        double size = ate.size();

        double[] ingestedTotal = new double[10];

        for(Map.Entry<String, Integer> entry: ate.entrySet()) {
            String category = entry.getKey();
            int amount = entry.getValue();
            if(!categories.containsKey(category)) return null;

            List<Double> nutrientList = ingestedNutrientsCategoryArranger(categories.get(category))
                    .stream()
                    .map(i -> new Double((Double) i))
                    .collect(Collectors.toList());

            int index = 0;
            for(double ing: nutrientList) {
                ingestedTotal[index++] += ing * amount;
            }
        }

        return Arrays
                .stream(ingestedTotal)
                .map(i -> i / size)
                .toArray();
    }

}
