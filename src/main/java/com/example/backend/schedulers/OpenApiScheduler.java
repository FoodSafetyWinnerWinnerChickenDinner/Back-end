package com.example.backend.schedulers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OpenApiScheduler {

    private final Job job;
    private final JobLauncher jobLauncher;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Scheduled(cron = "0 30 17 * * *")
    public void executeOpenApiConnection() {

        try {

            jobLauncher.run(
                    job,
                    new JobParametersBuilder()
                        .addString("datetime", LocalDateTime.now().toString())
                    .toJobParameters()
            );

        } catch (JobInstanceAlreadyCompleteException
                | JobRestartException
                | JobParametersInvalidException
                | JobExecutionAlreadyRunningException jobException) {

            LOGGER.error(">>> FoodOpenApi >> exception >> ", jobException);

        }

    }
}
