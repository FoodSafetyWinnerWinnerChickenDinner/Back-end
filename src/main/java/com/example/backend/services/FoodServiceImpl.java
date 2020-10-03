package com.example.backend.services;

import com.example.backend.configurations.NutrientsConfigs;
import com.example.backend.configurations.RDAConfigs;
import com.example.backend.models.Foods;
import com.example.backend.models.data_enums.Nutrients;
import com.example.backend.models.data_enums.RDA;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.FoodService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodOpenApiServiceImpl openApiService;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private NutrientsConfigs nutrientsConfigs;

    @Autowired
    private RDAConfigs rdaConfigs;

    private ArrayList<Foods> foodDB;
    private HashMap<String, Nutrients> categories;

    private static final String SERVICE_NAME = "I2790";
    private static final String LIST_FLAG = "row";
    private static final int INTERVAL = 200;
    private static final int LAST_INDEX = 29_274;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public Foods findById(Long id) {
        return foodRepository.findById(id).orElse(null);
    }

    @Override
    public void dataUpdateProcessorByFoodOpenApi() {
        int start = 1;
        int end = INTERVAL;

        while(start <= LAST_INDEX) {
            String jsonText = openApiService.requestFoods(start, end);
            JSONParser parser = new JSONParser();

            try {
                JSONObject json = (JSONObject) parser.parse(jsonText);

                JSONObject jsonFood = (JSONObject) json.get(SERVICE_NAME);
                JSONArray jsonArray = (JSONArray) jsonFood.get(LIST_FLAG);

                int size = jsonArray.size();
                for (int i = 0; i < size; i++) {
                    JSONObject food = (JSONObject) jsonArray.get(i);

                    Foods apiData = new Foods();

                    apiData.setFoodName(food.get("DESC_KOR").toString());
                    apiData.setCategory(food.get("GROUP_NAME").toString());
                    apiData.setTotal(validation(food.get("SERVING_SIZE").toString()));
                    apiData.setKcal(validation(food.get("NUTR_CONT1").toString()));
                    apiData.setCarbohydrate(validation(food.get("NUTR_CONT2").toString()));
                    apiData.setProtein(validation(food.get("NUTR_CONT3").toString()));
                    apiData.setFat(validation(food.get("NUTR_CONT4").toString()));
                    apiData.setSugar(validation(food.get("NUTR_CONT5").toString()));
                    apiData.setSodium(validation(food.get("NUTR_CONT6").toString()));
                    apiData.setCholesterol(validation(food.get("NUTR_CONT7").toString()));
                    apiData.setSaturatedFattyAcid(validation(food.get("NUTR_CONT8").toString()));
                    apiData.setTransFat(validation(food.get("NUTR_CONT9").toString()));

                    save(apiData);
                }
                System.out.println(size);
            } catch (ParseException parseException) {
                LOGGER.error(">>> FoodsServiceImpl >> exception >> ", parseException);
                parseException.printStackTrace();
            }

            start += INTERVAL;
            end += INTERVAL;
        }
    }

    @Override
    public void categorySetter() {
        categories = new HashMap<>();
        categories.put("치킨", nutrientsConfigs.getChicken());
        categories.put("돼지구이", nutrientsConfigs.getPig());
        categories.put("소구이", nutrientsConfigs.getCow());
        categories.put("생선구이", nutrientsConfigs.getFish());
        categories.put("생선회", nutrientsConfigs.getRawFish());
        categories.put("라면", nutrientsConfigs.getRamen());
        categories.put("커피", nutrientsConfigs.getCoffee());
        categories.put("음료수", nutrientsConfigs.getDrink());
        categories.put("빵", nutrientsConfigs.getBread());
        categories.put("피자", nutrientsConfigs.getPizza());
        categories.put("김치", nutrientsConfigs.getKimchi());
        categories.put("탕", nutrientsConfigs.getSoup());
        categories.put("밥", nutrientsConfigs.getRice());
        categories.put("떡볶이", nutrientsConfigs.getRedRiceCake());
        categories.put("순대", nutrientsConfigs.getSundae());
        categories.put("튀김", nutrientsConfigs.getFried());
    }

    /**
     *
     * @param eats: foods type
     * @return user's total ingested nutrients
     *
     */
    @Override
    public double[] ingestedTotalNutrientsGetter(List<String> eats) {
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
    public void foodListUpdater() {
        foodDB = new ArrayList<>();

        for(Long idx = 1L; idx <= LAST_INDEX; idx++) {
            foodDB.add(findById(idx));
        }
    }

    @Override
    public ArrayList<String> menuRecommendation(double[] ingested) {
        System.out.println("의 총 영양소는 아래와 같습니다.");
        System.out.println("총: " + ingested[0] + ", 칼로리: " + ingested[1]);
        System.out.println("탄수화물: " + ingested[2] + ", 단백질: " + ingested[3] + ", 지방: " + ingested[4]);
        System.out.println("당: " + ingested[5] + ", 나트륨: " + ingested[6]);
        System.out.println("콜레스테롤: " + ingested[7] + ", 포화지방산: " + ingested[8] + ", 트랜스지방: " + ingested[9]);

        foodListUpdater();
        RDA rda = rdaConfigs.getRecommendedDailyAllowance();
        double[] needs = {
                rda.getCarbohydrate() - ingested[2], rda.getProtein() - ingested[3], rda.getFat() - ingested[4]
        };

        for(int i = 0; i < needs.length; i++) {
            if(needs[i] < 0) needs[i] = 0;
        }

        PriorityQueue<Foods> carboPrior =
                new PriorityQueue<>((f1, f2) -> f1.getCarbohydrate() < f2.getCarbohydrate() ? -1: 1);

        for(Foods dbFood: foodDB) {
            Foods element = dbFood;
            element.setCarbohydrate(Math.abs(needs[0] - dbFood.getCarbohydrate()));
            element.setProtein(Math.abs(needs[0] - dbFood.getProtein()));
            element.setFat(Math.abs(needs[0] - dbFood.getFat()));

            carboPrior.offer(element);
        }

        int size = 10;
        while(size-- > 0) {
            Foods current = carboPrior.poll();
            System.out.println(current.getFoodName() + " "
                    + current.getCarbohydrate() + " " + current.getProtein() + " " + current.getFat());
        }

        return null;
    }

    @Override
    public void save(Foods food) {
        foodRepository.save(food);
    }

    @Override
    public void delete(Foods food) {
        foodRepository.delete(food);
    }

    @Override
    public double validation(String data) {
        if(data.length() == 0) return 0.0;
        else return Double.parseDouble(data);
    }
}
