package com.example.backend.configurations;

import com.example.backend.models.data_enums.Nutrients;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nut")
@Getter
public class NutrientsConfig {
    private Nutrients chicken;
    private Nutrients pig;
    private Nutrients cow;
    private Nutrients fish;
    private Nutrients rawFish;
    private Nutrients ramen;
    private Nutrients coffee;
    private Nutrients drink;
    private Nutrients bread;
    private Nutrients pizza;
    private Nutrients kimchi;
    private Nutrients soup;
    private Nutrients rice;
    private Nutrients redRiceCake;
    private Nutrients sundae;
    private Nutrients fried;

    public NutrientsConfig() {
        this.chicken = Nutrients.CHICKEN;
        this.pig = Nutrients.PIG;
        this.cow = Nutrients.COW;
        this.fish = Nutrients.FISH;
        this.rawFish = Nutrients.RAW_FISH;
        this.ramen = Nutrients.RAMEN;
        this.coffee = Nutrients.COFFEE;
        this.drink = Nutrients.DRINK;
        this.bread = Nutrients.BREAD;
        this.pizza = Nutrients.PIZZA;
        this.kimchi = Nutrients.KIMCHI;
        this.soup = Nutrients.SOUP;
        this.rice = Nutrients.RICE;
        this.redRiceCake = Nutrients.RED_RICE_CAKE;
        this.sundae = Nutrients.SUNDAE;
        this.fried = Nutrients.FRIED;
    }
}
