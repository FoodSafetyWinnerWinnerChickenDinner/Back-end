package com.example.backend.models;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@Table(name = "recipes", schema = "food_safety")
public class Recipes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;                // TODO: remove auto-increment & reset id by RCP_SEQ

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "category")
    private String category;

    @Column(name = "cooking_method")
    private String cookingMethod;

    @Column(name = "cooking_material_example")
    private String cookingMaterialExample;

    @Column(name = "cooking_completion_example")
    private String cookingCompletionExample;

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

    @OneToOne
    @JoinColumn(name = "manual_id")
    private Manuals manuals;

    @OneToOne
    @JoinColumn(name = "manual_image_id")
    private ManualImages manualImages;

}
