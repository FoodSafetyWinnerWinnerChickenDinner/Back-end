package com.example.backend.configurations.jobs;

import com.example.backend.services.FoodOpenApi;
import com.example.backend.services.RecipeOpenApi;
import com.example.backend.tasklets.OpenApiTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final FoodOpenApi foodOpenApi;
    private final RecipeOpenApi recipeOpenApi;

    @Bean
    public Job openApiJob() {
        return jobBuilderFactory.get("Open-API connecting")
                .start(initStep())
                .next(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step initStep() {
        return stepBuilderFactory.get("initializr")
                .tasklet(new OpenApiTasklet())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("Recipe Open-API")
                .tasklet((contribution, chunkContext) -> {
                    recipeOpenApi.updateByOpenApiData();

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("Food Open-API")
                .tasklet((contribution, chunkContext) -> {
                    foodOpenApi.updateByOpenApiData();

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
