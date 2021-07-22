package com.example.backend.services;

import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeRepository;
import com.example.backend.services.interfaces.recommend.Recommendable;
import com.example.backend.services.interfaces.db_access.Readable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements Recommendable, Readable {

    private final RecipeRepository recipeRepository;

    @Override
    public List<Recipes> menuRecommender(double[] ingested) {
        List<Recipes> dbFields = getListAll();

        /**
         *
         * ingested: 섭취한 영양 성분 분류별 총량
         * foodDB: local db data
         *
         * TF-IDF
         * try: 범주화, 전체 영양 성분 정규 분포 배치
         * - 타당한 근거: 6시그마 분할? -> 영양 성분 별 collecting -> tf-idf
         * - 유용한 범주: 연속형 변수 범주화
         *
         * -> 주요 영양소를 뽑아낸다.
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
