package com.example.backend.repository;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.rest_template.RestTemplateConfig;
import com.example.backend.repositories.RecipeRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class RecipeOpenApiServiceImplRepositoryTests {

    @Mock private RecipeRepository recipeRepository;
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
        recipeRepository.save(null);

        // then
//        assertThat(recipeOpenApiRepository.findById(1L).get()
//                .getManuals().getManual12()).isEqualTo("potato");
//
//        assertThat(recipeOpenApiRepository.findById(1L).get()
//                .getManualImages().getManualImage9()).isEqualTo("apple");
    }

}
