package com.example.backend.services.interfaces.categorize;

import com.example.backend.models.data_enums.Nutrients;

import java.util.List;
import java.util.Map;

public interface Categorizable {

    String[] FOOD_FORMATS = {"치킨", "돼지구이", "소구이", "생선구이"
            , "생선회", "라면", "커피", "음료수", "빵", "피자", "김치"
            , "탕", "밥", "떡볶이", "순대", "튀김"};

    Map<String, ?> configurationSettingsCategorize();

    List<?> ingestedNutrientsCategorize(Nutrients nutrient);

}
