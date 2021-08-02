package com.example.backend.services.service_foods;

import com.example.backend.configurations.ComparableConfig;
import com.example.backend.models.Foods;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.service_foods.interface_foods.FoodService;
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
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    private final CosineSimilarity cosineSimilarity;

    @Override
    public List<Foods> menuRecommender(double[] ingested) {
        List<Foods> fields = getListAll();
        PriorityQueue<ComparableConfig> similar = new PriorityQueue<>();

        for (Foods field: fields) {

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
                .map(i -> similar.poll().getFoods())
                .limit(size)
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
        foodDB.addAll(foodRepository.findAll());

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
