package com.example.backend.services.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Recommendable {

    String[] FOOD_FORMATS = {"치킨", "돼지구이", "소구이", "생선구이"
            , "생선회", "라면", "커피", "음료수", "빵", "피자", "김치"
            , "탕", "밥", "떡볶이", "순대", "튀김"};

    List<?> menuRecommender(double[] ingested, List<?> listAll);

    default Map<String, ?> categoryConfigurationSettings(){
        Map<String, ?> categories = new HashMap<>();

        return categories;
    }

}
