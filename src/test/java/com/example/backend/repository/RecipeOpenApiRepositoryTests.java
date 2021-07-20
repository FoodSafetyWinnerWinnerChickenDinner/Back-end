package com.example.backend.repository;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.RestTemplateConfig;
import com.example.backend.models.ManualImages;
import com.example.backend.models.Manuals;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeOpenApiRepository;
import com.example.backend.services.ManualImageServiceImpl;
import com.example.backend.services.ManualServiceImpl;
import com.example.backend.services.RecipeOpenApiServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class RecipeOpenApiRepositoryTests {

    @Mock private RecipeOpenApiRepository recipeOpenApiRepository;
    @Mock private OpenApiConfig recipeApi;
    @Mock private ManualServiceImpl manualService;
    @Mock private ManualImageServiceImpl manualImageService;
    @Mock private RestTemplateConfig restTemplate;

    private RecipeOpenApiServiceImpl recipeOpenApiServiceImpl;

    @BeforeEach
    void setup(){
        this.recipeOpenApiServiceImpl = new RecipeOpenApiServiceImpl(
                recipeApi, manualService, manualImageService, recipeOpenApiRepository, restTemplate
        );
    }

    @Test
    @DisplayName("1:1 매핑 확인")
    public void save_recipe(){
        // given
        Manuals manual = Manuals.builder().id(1L)
                .manual1("").manual2("").manual3("").manual4("").manual5("").manual6("").manual7("").manual8("").manual9("").manual10("")
                .manual11("").manual12("potato").manual13("").manual14("").manual15("").manual16("").manual17("").manual18("").manual19("").manual20("")
                .build();

        ManualImages manualImage = ManualImages.builder().id(1L)
                .manualImage1("").manualImage2("").manualImage3("").manualImage4("").manualImage5("").manualImage6("")
                .manualImage7("").manualImage8("").manualImage9("apple").manualImage10("").manualImage11("").manualImage12("")
                .manualImage13("").manualImage14("").manualImage15("").manualImage16("").manualImage17("").manualImage18("")
                .manualImage19("").manualImage20("")
                .build();

        Recipes recipe = Recipes.builder().id(1L)
                .recipeName("삼양 라면").category("라면").cookingMethod("냄비")
                .cookingMaterialExample("img").cookingCompletionExample("img")
                .ingredients("라면 스프, 물, 면").kcal(200).carbohydrate(5).protein(4)
                .fat(3).sodium(90)
                .manuals(manual)
                .manualImages(manualImage)
                .build();

        // when
        recipeOpenApiRepository.save(recipe);

        // then
        assertThat(recipeOpenApiRepository.findById(1L).get()
                .getManuals().getManual12()).isEqualTo("potato");

        assertThat(recipeOpenApiRepository.findById(1L).get()
                .getManualImages().getManualImage9()).isEqualTo("apple");
    }

}
