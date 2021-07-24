package com.example.backend.services;

import com.example.backend.configurations.ComparableConfig;
import com.example.backend.models.Foods;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.db_access.Readable;
import com.example.backend.services.interfaces.recommend.Recommendable;
import com.example.backend.util_components.CosineSimilarity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FoodService implements Recommendable, Readable {

    private final FoodRepository foodRepository;

    private final CosineSimilarity cosineSimilarity;

    @Override
    public List<Foods> menuRecommender(double[] ingested) {
        List<Foods> fields = getListAll();
        PriorityQueue<ComparableConfig> similar = new PriorityQueue<>();

        for (Foods field: fields) {

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
                .map(i -> similar.poll().getFoods())
                .limit(min)
                .collect(Collectors.toList());

    }

    @Override
    public boolean isContainsField(Object object) {
        Foods food = (Foods) object;
        long id = food.getId();

        return foodRepository.existsById(id);
    }

    @Override
    public List<Foods> getListAll() {
        List<Foods> foodDB = new ArrayList<>();
        foodDB.addAll((Collection<? extends Foods>) foodRepository.findAll());

        return foodDB;
    }

    private List<Object> categorize(Foods food) {
        return Stream.builder()
                .add(food.getTotal()).add(food.getKcal())
                .add(food.getCarbohydrate()).add(food.getProtein()).add(food.getFat())
                .add(food.getSugar()).add(food.getSodium()).add(food.getCholesterol())
                .add(food.getSaturatedFattyAcid()).add(food.getTransFat())
                .build()
                .collect(Collectors.toList());
    }

}
