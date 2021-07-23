package com.example.backend.services;

import com.example.backend.configurations.ComparableConfig;
import com.example.backend.models.Foods;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeRepository;
import com.example.backend.services.interfaces.db_access.Readable;
import com.example.backend.services.interfaces.recommend.Recommendable;
import com.example.backend.utils.CosineSimilarity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RecipeService implements Recommendable, Readable {

    private final RecipeRepository recipeRepository;

    private final CosineSimilarity cosineSimilarity;

    @Override
    public List<Recipes> menuRecommender(double[] ingested) {
        List<Recipes> fields = getListAll();
        PriorityQueue<ComparableConfig> similar = new PriorityQueue<>();

        for (Recipes field: fields) {

            double[] contains = new double[10];
            List<Object> nutrientList = categorize(field);

            int index = 0;
            for(Object ing: nutrientList) {
                contains[index++] += Double.parseDouble(ing.toString());
            }

            double scalar = cosineSimilarity.scalarProduct(ingested, contains);
            double vector = cosineSimilarity.vectorProduct(ingested, contains);

            similar.offer(new ComparableConfig(field, cosineSimilarity.division(vector, scalar)));

        }

        int min = Math.min(10, similar.size());
        return Arrays
                .stream(similar.toArray())
                .map(i -> similar.poll().getRecipes())
                .limit(min)
                .collect(Collectors.toList());

    }

    @Override
    public boolean isContainsField(Object object) {
        Recipes recipe = (Recipes) object;
        long id = recipe.getId();

        return recipeRepository.existsById(id);
    }

    @Override
    public List<Recipes> getListAll() {
        List<Recipes> recipeDB = new ArrayList<>();
        recipeDB.addAll((Collection<? extends Recipes>) recipeRepository.findAll());

        return recipeDB;
    }

    private List<Object> categorize(Recipes recipe) {
        return Stream.builder()
                .add(recipe.getKcal())
                .add(recipe.getCarbohydrate()).add(recipe.getProtein()).add(recipe.getFat())
                .add(recipe.getSodium())
                .build()
                .collect(Collectors.toList());
    }
}
