package com.example.backend.services.service_recipes;

import com.example.backend.configurations.ComparableConfig;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeRepository;
import com.example.backend.services.service_recipes.interface_recipes.RecipeService;
import com.example.backend.util_components.util_math.CosineSimilarity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final CosineSimilarity cosineSimilarity;

    @Override
    public List<Recipes> menuRecommender(double[] ingested) {
        List<Recipes> fields = getListAll();
        PriorityQueue<ComparableConfig> similar = new PriorityQueue<>();

        for (Recipes field: fields) {

            double[] contains = new double[NUTRIENT_TYPES];
            List<Object> nutrientList = categorize(field);

            int index = 0;
            for(Object ing: nutrientList) {
                contains[index++] += Double.parseDouble(ing.toString());
            }

            double scalar = cosineSimilarity.scalarProduct(ingested, contains);
            double vector = cosineSimilarity.vectorProduct(ingested, contains);

            similar.offer(new ComparableConfig(field, cosineSimilarity.division(vector, scalar)));

        }

        int size = Math.min(PACK, similar.size());
        return Arrays
                .stream(similar.toArray())
                .map(i -> similar.poll().getRecipes())
                .limit(size)
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
        recipeDB.addAll(recipeRepository.findAll());

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
