package com.example.backend.services;

import com.example.backend.models.Foods;
import com.example.backend.models.data_enums.Nutrients;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.categorize.Categorizable;
import com.example.backend.services.interfaces.recommend.Recommendable;
import com.example.backend.services.interfaces.db_access.Readable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements Recommendable, Readable, Categorizable {

    private final FoodRepository foodRepository;

    @Override
    public List<Foods> menuRecommender(double[] ingested) {
        List<Foods> dbFields = getListAll();

        /**
         *
         * ingested: 섭취한 영양 성분 분류별 총량
         * foodDB: local db data
         *
         * TF-IDF
         * try: 범주화, 전체 영양 성분 정규 분포 배치
         * - 타당한 근거: 6시그마 분할 -> 영양 성분 별 collecting -> tf-idf
         * - 유용한 범주: 연속형 변수 범주화
         *
         * -> 주요 영양소를 뽑아낸다.
         *
         */

        List<Foods> recommend = new ArrayList<>();

        return recommend;
    }

    @Override
    public Map<String, Nutrients> configurationSettingsCategorize(){
        Map<String, Nutrients> categories = new HashMap<>();

        for(Nutrients nutrient:
                Stream
                .of(Nutrients.values())
                .collect(Collectors.toList())) {

            categories.put(FOOD_FORMATS[nutrient.getSequence()], nutrient);
        }

        return categories;
    }

    @Override
    public List<Object> ingestedNutrientsCategorize(Nutrients nutrient) {
        return Stream.builder()
                .add(nutrient.getTotal()).add(nutrient.getKcal())
                .add(nutrient.getCarbohydrate()).add(nutrient.getProtein()).add(nutrient.getFat())
                .add(nutrient.getSugar()).add(nutrient.getSodium()).add(nutrient.getCholesterol())
                .add(nutrient.getSaturatedFattyAcid()).add(nutrient.getTransFat())
                .build()
                .collect(Collectors.toList());
    }

    public double[] ingestedNutrientsTotal(List<String> ate) {
        Map<String, Nutrients> categories = configurationSettingsCategorize();
        double[] ingestedTotal = new double[10];

        for(String eat: ate) {
            StringTokenizer separator = new StringTokenizer(eat);

            String category = separator.nextToken();
            int amount = Integer.parseInt(separator.nextToken());
            if(!categories.containsKey(category)) return null;

            List<Object> nutrienntList = ingestedNutrientsCategorize(categories.get(category));

            int index = 0;
            for(Object ing: nutrienntList) {
                ingestedTotal[index++] += Double.parseDouble(ing.toString()) * amount;
            }

        }

        return ingestedTotal;
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
}
