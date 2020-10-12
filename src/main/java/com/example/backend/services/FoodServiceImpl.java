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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodOpenApiServiceImpl openApiService;

    private final FoodRepository foodRepository;

    private final NutrientsConfigs nutrientsConfigs;

    private final RDAConfigs rdaConfigs;

    private ArrayList<Foods> foodDB;
    private HashMap<String, Nutrients> categories;
    private HashSet<String> exceptCategories;
    private double[] needs;

    private static final String SERVICE_NAME = "I2790";
    private static final String LIST_FLAG = "row";
    private static final String DELIM = ", ";
    private static final int INTERVAL = 200;
    private static final int LAST_INDEX = 29_274;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    public FoodServiceImpl(FoodOpenApiServiceImpl openApiService, FoodRepository foodRepository, NutrientsConfigs nutrientsConfigs, RDAConfigs rdaConfigs) {
        this.openApiService = openApiService;
        this.foodRepository = foodRepository;
        this.nutrientsConfigs = nutrientsConfigs;
        this.rdaConfigs = rdaConfigs;
    }

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
    public void exceptCategorySetter() {
        exceptCategories = new HashSet<>();
        exceptCategories.add("잼류"); exceptCategories.add("특수용도식품");
        exceptCategories.add("초콜릿류");  exceptCategories.add("음료류");
        exceptCategories.add("조미식품");  exceptCategories.add("기타식품류");
        exceptCategories.add("즉석식품류");  exceptCategories.add("유가공식품");
        exceptCategories.add("빵류");  exceptCategories.add("주류");
        exceptCategories.add("기타");  exceptCategories.add("장류");
        exceptCategories.add("벌꿀 및 화분가공품류");  exceptCategories.add("유가공품류");
        exceptCategories.add("유가공품");  exceptCategories.add("코코아가공품류");
        exceptCategories.add("조미료류");  exceptCategories.add("당류");
        exceptCategories.add("차류");  exceptCategories.add("빙과류");
        exceptCategories.add("식용유지류");  exceptCategories.add("유지류");
        exceptCategories.add("과자류, 빵류 또는 떡류"); exceptCategories.add("가루 우유 및 유제품류");
        exceptCategories.add("동물성가공식품류"); exceptCategories.add("농산가공식품류");
        exceptCategories.add("감자 및 전분류"); exceptCategories.add("수산가공품");
        exceptCategories.add("견과류 및 종실류"); exceptCategories.add("조리가공품류");
        exceptCategories.add("곡류 및 그 제품");
    }

    @Override
    public double priorCalculator(Foods food, double carbohydrate, double protein, double fat) {
        if((carbohydrate -= food.getCarbohydrate()) < 0) return -1;
        if((protein -= food.getProtein()) < 0) return -1;
        if((fat -= food.getFat()) < 0) return -1;

        return carbohydrate + protein + fat;
    }

    @Override
    public ArrayList<Foods>[] extractCandidates(double[] ingested) {
        System.out.println("총: " + ingested[0] + ", 칼로리: " + ingested[1]);
        System.out.println("탄수화물: " + ingested[2] + ", 단백질: " + ingested[3] + ", 지방: " + ingested[4]);
        System.out.println("당: " + ingested[5] + ", 나트륨: " + ingested[6]);
        System.out.println("콜레스테롤: " + ingested[7] + ", 포화지방산: " + ingested[8] + ", 트랜스지방: " + ingested[9]);

        RDA rda = rdaConfigs.getRecommendedDailyAllowance();
        needs = new double[3];
        needs[0] = rda.getCarbohydrate() - ingested[2];
        needs[1] = rda.getProtein() - ingested[3];
        needs[2] = rda.getFat() - ingested[4];

        for(int i = 0; i < needs.length; i++) {
            if(needs[i] < 0) needs[i] = 0;
        }

        PriorityQueue<Foods> dataArranger =
                new PriorityQueue<>((f1, f2) -> f1.getCategory().equals(f2.getCategory())
                        ? f1.getTotal() < f2.getTotal() ? -1: 1: f1.getCategory().compareTo(f2.getCategory()));

        for(Foods dbFood: foodDB) {
            Foods element = dbFood;
            double priors = priorCalculator(element, needs[0], needs[1], needs[2]);
            if (priors == -1) continue;

            element.setTotal(priors);
            dataArranger.offer(element);
        }

        HashSet<String> alreadyContains = new HashSet<>();
        int index = -1;

        ArrayList<Foods>[] candidate = new ArrayList[19];
        for(int i = 0; i < candidate.length; i++) {
            candidate[i] = new ArrayList<>();
        }

        while(!dataArranger.isEmpty()) {
            Foods current = dataArranger.poll();

            if(current.getCategory().isEmpty() || exceptCategories.contains(current.getCategory())) continue;
            if(alreadyContains.contains(current.getCategory())){
                candidate[index].add(current);
                continue;
            }

            alreadyContains.add(current.getCategory());

            index++;
            candidate[index].add(current);
        }

        return candidate;
    }

    private static Foods menuConverter(Foods menu) {
        Foods converted = menu;

        if(menu.getFoodName().contains("라면") || menu.getFoodName().contains("우동")
                || menu.getFoodName().contains("냉면") || menu.getFoodName().contains("탕면")
                || menu.getFoodName().contains("청정원") || menu.getFoodName().contains("샘표")) {

            System.out.println(menu.getFoodName() + " " + menu.getCarbohydrate());

            converted.setFoodName("밥");
            converted.setCategory("누룽지 곡류 및 그 제품");
            converted.setCarbohydrate(90.98);
            converted.setProtein(6.64);
            converted.setFat(0.84);
        }

        return converted;
    }

    @Override
    public ArrayList<Foods> menuRecommendation(ArrayList<Foods>[] candidates) {
        PriorityQueue<Foods> recommender =
                new PriorityQueue<>((f1, f2) -> f1.getCarbohydrate() + f1.getProtein() + f1.getFat()
                    > f2.getCarbohydrate() + f2.getProtein() + f2.getFat() ? -1: 1);

        for(int i = 0; i < candidates.length; i++) {
            Collections.sort(candidates[i], (f1, f2) -> f1.getCarbohydrate() + f1.getProtein() + f1.getFat()
                    >= f2.getCarbohydrate() + f2.getProtein() + f2.getFat() ? -1: 1);

            int mod = candidates[i].size();
            if(mod != 1) mod /= 2;

            Foods rec = candidates[i].get((int) (Math.random() * LAST_INDEX) % mod);
            while(rec.getCarbohydrate() + rec.getProtein() + rec.getFat() < 1) {
                rec = candidates[i].get((int) (Math.random() * LAST_INDEX) % mod);
            }

            if(rec.getCarbohydrate() == 0 && rec.getProtein() == 0 && rec.getFat() == 0) continue;
            recommender.offer(rec);
        }

        ArrayList<Foods> result = new ArrayList<>();

        while(!recommender.isEmpty()) {                 // save data in list and return
            Foods menu = recommender.poll();

            menu = menuConverter(menu);

            if(needs[0] - menu.getCarbohydrate() < 0) continue;
            if(needs[1] - menu.getProtein() < 0) continue;
            if(needs[2] - menu.getFat() < 0) continue;

            needs[0] -= menu.getCarbohydrate();
            needs[1] -= menu.getProtein();
            needs[2] -= menu.getFat();

            result.add(menu);
        }

        System.out.println("탄수화물 나머지: " + needs[0] + ", 단백질 나머지: " + needs[1] + ", 지방 나머지: " + needs[2]);
        return result;
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
