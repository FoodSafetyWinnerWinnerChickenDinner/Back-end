package com.example.backend.repository;

import com.example.backend.models.ManualImages;
import com.example.backend.models.Manuals;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecipeRepositoryTests {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
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
        recipeRepository.save(recipe);

        // then
        assertThat(recipe.getManuals().getManual12()).isEqualTo("potato");
        assertThat(recipe.getManualImages().getManualImage9()).isEqualTo("apple");
    }

}
