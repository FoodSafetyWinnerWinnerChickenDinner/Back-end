package com.example.backend.services;

import com.example.backend.configurations.NutrientsConfig;
import com.example.backend.models.Foods;
import com.example.backend.models.data_enums.Nutrients;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    private final NutrientsConfig nutrientsConfig;

    @Autowired
    public FoodServiceImpl(FoodRepository foodRepository, NutrientsConfig nutrientsConfig) {
        this.foodRepository = foodRepository;
        this.nutrientsConfig = nutrientsConfig;
    }

    @Override
    public HashMap<String, Nutrients> categorySetter() {
        HashMap<String, Nutrients> categories = new HashMap<>();

        categories.put("치킨", nutrientsConfig.getChicken());
        categories.put("돼지구이", nutrientsConfig.getPig());
        categories.put("소구이", nutrientsConfig.getCow());
        categories.put("생선구이", nutrientsConfig.getFish());
        categories.put("생선회", nutrientsConfig.getRawFish());
        categories.put("라면", nutrientsConfig.getRamen());
        categories.put("커피", nutrientsConfig.getCoffee());
        categories.put("음료수", nutrientsConfig.getDrink());
        categories.put("빵", nutrientsConfig.getBread());
        categories.put("피자", nutrientsConfig.getPizza());
        categories.put("김치", nutrientsConfig.getKimchi());
        categories.put("탕", nutrientsConfig.getSoup());
        categories.put("밥", nutrientsConfig.getRice());
        categories.put("떡볶이", nutrientsConfig.getRedRiceCake());
        categories.put("순대", nutrientsConfig.getSundae());
        categories.put("튀김", nutrientsConfig.getFried());

        return categories;
    }

    @Override
    public double[] ingestedTotalNutrientsGetter(List<String> eats, HashMap<String, Nutrients> categories) {
        double[] ingestedTotal = new double[10];

        for(String eat: eats) {
            StringTokenizer separator = new StringTokenizer(eat);
            String category = separator.nextToken();
            int amount = Integer.parseInt(separator.nextToken());

            if(!categories.containsKey(category)) return null;

            Nutrients nutrients = categories.get(category);

            ingestedTotal[0] += nutrients.getTotal() * amount;
            ingestedTotal[1] += nutrients.getKcal() * amount;
            ingestedTotal[2] += nutrients.getCarbohydrate() * amount;
            ingestedTotal[3] += nutrients.getProtein() * amount;
            ingestedTotal[4] += nutrients.getFat() * amount;
            ingestedTotal[5] += nutrients.getSugar() * amount;
            ingestedTotal[6] += nutrients.getSodium() * amount;
            ingestedTotal[7] += nutrients.getCholesterol() * amount;
            ingestedTotal[8] += nutrients.getSaturatedFattyAcid() * amount;
            ingestedTotal[9] += nutrients.getTransFat() * amount;
        }

        return ingestedTotal;
    }

    @Override
    public List<Foods> foodListExtractFromDB() {
        List<Foods> foodDB = new ArrayList<>();
        foodDB.addAll((Collection<? extends Foods>) foodRepository.findAll());

        return foodDB;
    }

    @Override
    public List<Foods> menuRecommendation(double[] ingested, List<Foods> foodDB) {
        /**
         *
         * ingested: 섭취한 영양 성분 분류별 총량
         * foodDB: local db data
         *
         * TF-IDF
         * try: 범주화
         * - 타당한 근거: 각 성분별 표준 편차(평균에서 떨어진 정도) 활용, 또는 사분위 수 (평균, 간격 = 상한 - 하한)
         * - 유용한 범주: 연속형 변수 범주화
         *
         * -> 주요 영양소를 뽑아낸다.
         *
         */

        List<Foods> recommend = new ArrayList<>();

        return recommend;
    }

    @Override
    public boolean edible(double[] remained, Foods food) {
        if(remained[0] < food.getCarbohydrate()) {
            return food.getCarbohydrate() > 10 || remained[1] < food.getProtein() || remained[2] < food.getFat();
        }

        if(remained[1] < food.getProtein()) {
            return food.getProtein() > 10 || remained[0] < food.getCarbohydrate() || remained[2] < food.getFat();
        }

        if(remained[2] < food.getFat()) {
            return food.getFat() > 5 || remained[1] < food.getProtein() || remained[0] < food.getCarbohydrate();
        }

        return false;
    }

    @Override
    public void save(Foods food) {
        foodRepository.save(food);
    }

    @Override
    public void delete(Foods food) {
        foodRepository.delete(food);
    }

}
