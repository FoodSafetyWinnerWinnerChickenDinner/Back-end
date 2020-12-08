package com.example.backend.services;

import com.example.backend.configurations.NutrientsConfig;
import com.example.backend.configurations.RDAConfig;
import com.example.backend.models.Foods;
import com.example.backend.models.data_enums.Nutrients;
import com.example.backend.models.data_enums.RDA;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    private final NutrientsConfig nutrientsConfig;

    private final RDAConfig rdaConfig;

    @Autowired
    public FoodServiceImpl(FoodRepository foodRepository, NutrientsConfig nutrientsConfig, RDAConfig rdaConfig) {
        this.foodRepository = foodRepository;
        this.nutrientsConfig = nutrientsConfig;
        this.rdaConfig = rdaConfig;
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

    /**
     * save DB data in list
     */
    @Override
    public List<Foods> foodListExtractFromDB() {
        List<Foods> foodDB = new ArrayList<>();
        foodDB.addAll((Collection<? extends Foods>) foodRepository.findAll());

        return foodDB;
    }

    @Override
    public List<Foods> menuRecommendation(double[] ingested, List<Foods> foodDB) {
        RDA rda = rdaConfig.getRecommendedDailyAllowance();

        double[] remained = new double[3];
        remained[0] = rda.getCarbohydrate() * 2 - ingested[2];
        remained[1] = rda.getProtein() * 2 - ingested[3];
        remained[2] = rda.getFat() * 2 - ingested[4];

        PriorityQueue<Foods> dataArranger = new PriorityQueue<>((f1, f2) ->
                        f1.getTotal() > f2.getTotal() ? -1: 1);

        for(Foods dbFood: foodDB) {
            Foods element = dbFood;

            double priors = element.getCarbohydrate() + element.getProtein() + element.getFat();
            element.setTotal(priors);
            dataArranger.offer(element);
        }

        HashSet<String> alreadyCategory = new HashSet<>();
        List<Foods> recommend = new ArrayList<>();

        while(!dataArranger.isEmpty()) {
            Foods current = dataArranger.poll();
            if(alreadyCategory.contains(current.getCategory())) continue;
            if(edible(remained, current)) continue;

            alreadyCategory.add(current.getCategory());
            recommend.add(current);
        }

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
