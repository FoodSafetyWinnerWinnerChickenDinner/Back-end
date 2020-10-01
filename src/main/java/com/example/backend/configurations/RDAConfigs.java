package com.example.backend.configurations;

import com.example.backend.models.data_enums.RDA;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rda")
@Getter @Setter
public class RDAConfigs {
    private RDA recommendedDailyAllowance;

    public RDAConfigs(RDA recommendedDailyAllowance) {
        this.recommendedDailyAllowance = recommendedDailyAllowance;
    }
}
