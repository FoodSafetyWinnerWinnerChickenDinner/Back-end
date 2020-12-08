package com.example.backend.services;

import com.example.backend.configurations.RDAConfig;
import com.example.backend.models.Foods;
import com.example.backend.models.Recipes;
import com.example.backend.models.data_enums.RDA;
import com.example.backend.repositories.RecipeRepository;
import com.example.backend.services.interfaces.RecipeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final RDAConfig rdaConfig;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RDAConfig rdaConfig) {
        this.recipeRepository = recipeRepository;
        this.rdaConfig = rdaConfig;
    }

    @Override
    public List<Recipes> recipeListExtractFromDB() {
        List<Recipes> recipeDB = new ArrayList<>();
        recipeDB.addAll((Collection<? extends Recipes>) recipeRepository.findAll());

        return recipeDB;
    }

    @Override
    public Recipes termFrequencyInverseDocumentFrequency(double[] ingested, List<Recipes> recipesArrayList) {
        RDA rda = rdaConfig.getRecommendedDailyAllowance();
        double car = rda.getCarbohydrate() * 2 - ingested[2];
        double pro = rda.getProtein() * 2 - ingested[3];
        double fat = rda.getFat() * 2 - ingested[4];

        double[] sum = new double[3];
        double[] max = new double[3];
        int size = recipesArrayList.size();
        for(Recipes recipe: recipesArrayList) {
            sum[0] = recipe.getCarbohydrate();
            sum[1] = recipe.getProtein();
            sum[2] = recipe.getFat();

            for(int index = 0; index < sum.length; index++) {
                max[index] = Math.max(max[index], sum[index]);
            }
        }

        double avg = (sum[0] * sum[1] * sum[2] / size);
        double nutrientsMax = max[0] * max[1] * max[2];
        double current = car * pro * fat;

        double user = Math.log(current / avg) * (0.5 + (0.5 * current / nutrientsMax));

        PriorityQueue<Double> tfidf = new PriorityQueue<>();
        for(Recipes recipe: recipesArrayList) {
            current = recipe.getCarbohydrate() * recipe.getProtein() * recipe.getFat();
            tfidf.offer(Math.abs(user - Math.log(current / avg) * (0.5 + (0.5 * current / nutrientsMax))));
        }

        return null;
    }

    @Override
    public void save(Recipes recipe) {

    }

    @Override
    public void delete(Recipes recipe) {

    }
}
