package com.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "recipes", schema = "food_safety")
public class Recipes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "category")
    private String category;

    @Column(name = "cooking_method")
    private String cookingMethod;

    @Column(name = "cooking_completion_example")
    private String cookingCompletionExample;

    @Column(name = "cooking_completion_example1")
    private String cookingCompletionExample1;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "kcal")
    private double kcal;

    @Column(name = "carbohydrate")
    private double carbohydrate;

    @Column(name = "protein")
    private double protein;

    @Column(name = "fat")
    private double fat;

    @Column(name = "sodium")
    private double sodium;
}
