package com.example.backend.models;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter
@Builder
@Table(name = "foods", schema = "food_safety")
public class Foods {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;                // TODO: remove auto-increment & reset id by NUM

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "category")
    private String category;

    @Column(name = "total")
    private double total;

    @Column(name = "kcal")
    private double kcal;

    @Column(name = "carbohydrate")
    private double carbohydrate;

    @Column(name = "protein")
    private double protein;

    @Column(name = "fat")
    private double fat;

    @Column(name = "sugar")
    private double sugar;

    @Column(name = "sodium")
    private double sodium;

    @Column(name = "cholesterol")
    private double cholesterol;

    @Column(name = "saturated_fatty_acid")
    private double saturatedFattyAcid;

    @Column(name = "trans_fat")
    private double transFat;
}
