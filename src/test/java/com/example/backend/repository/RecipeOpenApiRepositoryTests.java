package com.example.backend.repository;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.RestTemplateConfig;
import com.example.backend.repositories.RecipeOpenApiRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class RecipeOpenApiRepositoryTests {

    @Mock private RecipeOpenApiRepository recipeOpenApiRepository;
    @Mock private OpenApiConfig recipeApi;
    @Mock private RestTemplateConfig restTemplate;


    @BeforeEach
    void setup(){
    }

    @Test
    @DisplayName("1:1 매핑 확인")
    public void save_recipe(){
        // given


        // when
        recipeOpenApiRepository.save(null);

        // then
//        assertThat(recipeOpenApiRepository.findById(1L).get()
//                .getManuals().getManual12()).isEqualTo("potato");
//
//        assertThat(recipeOpenApiRepository.findById(1L).get()
//                .getManualImages().getManualImage9()).isEqualTo("apple");
    }

}
