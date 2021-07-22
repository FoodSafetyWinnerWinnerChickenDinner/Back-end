package com.example.backend.configurations;

import com.example.backend.models.data_enums.Nutrients;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nut")
@Getter
@RequiredArgsConstructor
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

}
