package com.example.backend.services;

import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeRepository;
import com.example.backend.services.interfaces.RecipeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipes> recipeListExtractFromDB() {
        List<Recipes> recipeDB = new ArrayList<>();
        recipeDB.addAll((Collection<? extends Recipes>) recipeRepository.findAll());

        return recipeDB;
    }

    @Override
    public List<Optional> menuRecommendation(double[] ingested, List<Recipes> recipesArrayList, double responseSize) {
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

        List<Optional> recommend = new ArrayList<>();

        return recommend;
    }

    @Override
    public void save(Recipes recipe) {

    }

    @Override
    public void delete(Recipes recipe) {

    }
}
