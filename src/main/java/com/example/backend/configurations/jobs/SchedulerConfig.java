package com.example.backend.configurations.jobs;

import com.example.backend.services.service_foods.FoodOpenApiServiceImpl;
import com.example.backend.services.service_recipes.RecipeOpenApiServiceImpl;
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
    public static final String JOB_NAME = "Open-API_Batch";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final FoodOpenApiServiceImpl foodOpenApiServiceImpl;
    private final RecipeOpenApiServiceImpl recipeOpenApiServiceImpl;

    @Bean(name = JOB_NAME)
    public Job openApiJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step1())
                .next(step2())
                .preventRestart()
                .build();
    }

    @Bean(name = JOB_NAME + "_Recipe Open-API")
    public Step step1() {
        return stepBuilderFactory.get(JOB_NAME + "_Recipe Open-API")
                .tasklet((contribution, chunkContext) -> {
                    recipeOpenApiServiceImpl.updateByOpenApiData();

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = JOB_NAME + "_Food Open-API")
    public Step step2() {
        return stepBuilderFactory.get(JOB_NAME + "_Food Open-API")
                .tasklet((contribution, chunkContext) -> {
                    foodOpenApiServiceImpl.updateByOpenApiData();

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
