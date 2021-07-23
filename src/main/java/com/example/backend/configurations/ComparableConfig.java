package com.example.backend.configurations;

import com.example.backend.models.Foods;
import com.example.backend.models.Recipes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class ComparableConfig implements Comparable<ComparableConfig>{

    private Foods foods;

    private Recipes recipes;

    private double similarity;

    public ComparableConfig(Foods field, double similarity) {
        this.foods = field;
        this.similarity = similarity;
    }

    public ComparableConfig(Recipes field, double similarity) {
        this.recipes = field;
        this.similarity = similarity;
    }

    @Override
    public int compareTo(ComparableConfig cc) {
        return this.similarity > cc.similarity ? -1: 1;
    }

}
