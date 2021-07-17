package com.example.backend.configurations;

import com.example.backend.models.data_enums.RDA;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rda")
@Getter
public class RDAConfig {
    private RDA recommendedDailyAllowance;

    public RDAConfig() {
        this.recommendedDailyAllowance = recommendedDailyAllowance.A_MEAL_RECOMMENDED_DAILY_ALLOWANCE;
    }
}
