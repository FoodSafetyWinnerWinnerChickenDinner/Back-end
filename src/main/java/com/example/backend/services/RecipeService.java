package com.example.backend.services;

import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeRepository;
import com.example.backend.services.interfaces.db_access.Readable;
import com.example.backend.services.interfaces.recommend.Recommendable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipeService implements Recommendable, Readable {

    private final RecipeRepository recipeRepository;

    @Override
    public List<Recipes> menuRecommender(double[] ingested) {
        Map<Recipes, Double> similar = new HashMap<>();
        List<Recipes> dbFields = getListAll();


        /**
         *
         * ingestedAge: 사용자의 특정 기간 섭취 영양 성분 평균
         * dbFields: local db data
         *
         * dbFields :: ingestedAge 유사도 계산
         * vertex 선정: 전체 유사도 (양수) 중 특정 범위 내 위치하는 요소 추출 (range 양수 유사도의 평균: 0.5 ~ 1)
         * edge 셋팅: vertex 표준 편차 get, 임의의 두 요소의 차이가 표준 편차보다 작다면 linked
         * Text Rank 알고리즘을 통한 메인 요소 추출, 반환
         *
         */

        List<Recipes> recommend = new ArrayList<>();

        return recommend;
    }

    @Override
    public boolean isContainsField(Object object) {
        Recipes recipe = (Recipes) object;
        long id = recipe.getId();

        return recipeRepository.existsById(id);
    }

    @Override
    public List<Recipes> getListAll() {
        List<Recipes> recipeDB = new ArrayList<>();
        recipeDB.addAll((Collection<? extends Recipes>) recipeRepository.findAll());

        return recipeDB;
    }
}
