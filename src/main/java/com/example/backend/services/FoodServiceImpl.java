package com.example.backend.services;

import com.example.backend.models.Foods;
import com.example.backend.models.data_enums.Nutrients;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.Recommendable;
import com.example.backend.services.interfaces.Selectable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements Recommendable, Selectable {

    private final FoodRepository foodRepository;

    @Override
    public List<Foods> menuRecommender(double[] ingested, List<?> listAll) {
        List<Foods> dbFields = (List<Foods>) listAll;

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

    public Map<String, Nutrients> categoryConfigurationSettings(){
        Map<String, Nutrients> categories = new HashMap<>();

        for(Nutrients nutrient:
                Stream
                .of(Nutrients.values())
                .collect(Collectors.toList())) {

            categories.put(FOOD_FORMATS[nutrient.getSequence()], nutrient);
        }

        return categories;
    }

//    @Override
//    public double[] ingestedTotalNutrientsGetter(List<String> eats, HashMap<String, Nutrients> categories) {
//        double[] ingestedTotal = new double[10];
//
//        for(String eat: eats) {
//            StringTokenizer separator = new StringTokenizer(eat);
//            String category = separator.nextToken();
//            int amount = Integer.parseInt(separator.nextToken());
//
//            if(!categories.containsKey(category)) return null;
//
//            Nutrients nutrients = categories.get(category);
//
//            ingestedTotal[0] += nutrients.getTotal() * amount;
//            ingestedTotal[1] += nutrients.getKcal() * amount;
//            ingestedTotal[2] += nutrients.getCarbohydrate() * amount;
//            ingestedTotal[3] += nutrients.getProtein() * amount;
//            ingestedTotal[4] += nutrients.getFat() * amount;
//            ingestedTotal[5] += nutrients.getSugar() * amount;
//            ingestedTotal[6] += nutrients.getSodium() * amount;
//            ingestedTotal[7] += nutrients.getCholesterol() * amount;
//            ingestedTotal[8] += nutrients.getSaturatedFattyAcid() * amount;
//            ingestedTotal[9] += nutrients.getTransFat() * amount;
//        }
//
//        return ingestedTotal;
//    }

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
