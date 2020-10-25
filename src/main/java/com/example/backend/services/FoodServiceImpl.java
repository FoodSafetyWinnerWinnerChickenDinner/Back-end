package com.example.backend.services;

import com.example.backend.configurations.NutrientsConfig;
import com.example.backend.configurations.RDAConfig;
import com.example.backend.models.Foods;
import com.example.backend.models.data_enums.Nutrients;
import com.example.backend.models.data_enums.RDA;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.interfaces.FoodService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    private final NutrientsConfig nutrientsConfig;

    private final RDAConfig rdaConfig;

    private double[] needs;

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

    /**
     *
     * @param eats: foods type
     * @return user's total ingested nutrients
     */
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
    public ArrayList<Foods> foodListExtractFromDB() {
        ArrayList<Foods> foodDB = new ArrayList<>();
        foodDB.addAll((Collection<? extends Foods>) foodRepository.findAll());

        return foodDB;
    }

    @Override
    public HashSet<String> exceptCategorySetter() {
        HashSet<String> exceptCategories = new HashSet<>();

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

        return exceptCategories;
    }

    @Override
    public double priorCalculator(Foods food, double carbohydrate, double protein, double fat) {
        if((carbohydrate -= food.getCarbohydrate()) < 0) return -1;
        if((protein -= food.getProtein()) < 0) return -1;
        if((fat -= food.getFat()) < 0) return -1;

        return carbohydrate + protein + fat;
    }

    @Override
    public ArrayList<Foods>[] extractCandidates(double[] ingested, ArrayList<Foods> foodDB, HashSet<String> except) {
        RDA rda = rdaConfig.getRecommendedDailyAllowance();

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

        HashSet<String> alreadyCategory = new HashSet<>();
        int index = -1;

        ArrayList<Foods>[] candidate = new ArrayList[19];
        for(int i = 0; i < candidate.length; i++) {
            candidate[i] = new ArrayList<>();
        }

        while(!dataArranger.isEmpty()) {
            Foods current = dataArranger.poll();

            if(current.getCategory().isEmpty() || except.contains(current.getCategory())) continue;
            if(alreadyCategory.contains(current.getCategory())){
                candidate[index].add(current);
                continue;
            }

            alreadyCategory.add(current.getCategory());
            candidate[++index].add(current);
        }

        return candidate;
    }

    private static Foods menuConverter(Foods menu) {
        Foods converted = menu;

        if(menu.getFoodName().contains("라면") || menu.getFoodName().contains("우동")
                || menu.getFoodName().contains("냉면") || menu.getFoodName().contains("탕면")
                || menu.getFoodName().contains("청정원") || menu.getFoodName().contains("샘표")) {

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

        int size = getTotalSize(candidates);

        for(int i = 0; i < candidates.length; i++) {
            Collections.sort(candidates[i], (f1, f2) -> f1.getCarbohydrate() + f1.getProtein() + f1.getFat()
                    >= f2.getCarbohydrate() + f2.getProtein() + f2.getFat() ? -1: 1);

            int mod = candidates[i].size();
            if(mod != 1) mod /= 2;

            Foods rec = candidates[i].get((int) (Math.random() * size) % mod);
            while(rec.getCarbohydrate() + rec.getProtein() + rec.getFat() < 1) {
                rec = candidates[i].get((int) (Math.random() * size) % mod);
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

        return result;
    }

    @Override
    public int getTotalSize(ArrayList<Foods>[] candidates) {
        int size = 0;

        for(int index = 0; index < candidates.length; index++) {
            size += candidates[index].size();
        }

        return size;
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
