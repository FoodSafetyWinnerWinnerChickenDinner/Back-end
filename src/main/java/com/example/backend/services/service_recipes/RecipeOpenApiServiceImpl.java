package com.example.backend.services.service_recipes;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.models.ManualPairs;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeRepository;
import com.example.backend.services.service_recipes.interface_recipes.RecipeOpenApiService;
import com.example.backend.util_components.util_connector.OpenApiConnectorByWebClient;
import com.example.backend.util_components.util_string.Casting;
import com.example.backend.util_components.util_string.parse.LastIndexTracker;
import com.example.backend.util_components.util_string.parse.OpenApiJsonDataParse;
import com.example.backend.util_components.util_string.parse.PairTagBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.UnknownContentTypeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeOpenApiServiceImpl implements RecipeOpenApiService {

    private final RecipeRepository recipeRepository;

    private final OpenApiConfig recipeApi;

    private final PairTagBuilder pairTagBuilder;
    private final Casting casting;
    private final OpenApiJsonDataParse openApiJsonDataParse;
    private final OpenApiConnectorByWebClient byWebClient;
    private final LastIndexTracker tracker;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public void updateByOpenApiData() throws ExecutionException, InterruptedException {
        int start = 1;
        int end = INTERVAL;

        final int SIZE = tracker.findTag(recipeApi.getKey(), recipeApi.getRecipeServiceName());

        List<String> responses = new ArrayList<>();

        while(start <= SIZE) {
            end = Math.min(end, SIZE);

            try {

                responses.add(byWebClient
                        .requestOpenApiData(recipeApi.getKey(), recipeApi.getRecipeServiceName(), start ,end));

            }
            catch (UnknownContentTypeException unknownContentTypeException) {
                LOGGER.error(">>> Open API >> End of pages >> ", unknownContentTypeException);
                break;
            }

            start += INTERVAL;
            end += INTERVAL;

        }

        for(String response: responses) {

            JSONArray jsonRecipe = openApiJsonDataParse
                    .jsonDataParser(recipeApi.getRecipeServiceName(), response);

            if(jsonRecipe == null) break;
            List<Recipes> apiDataList = new ArrayList<>();

            for (Object obj : jsonRecipe) {
                JSONObject recipe = (JSONObject) obj;

                Recipes apiData = (Recipes) jsonToModel(recipe);
                if (isContainsField(apiData)) {
                    continue;
                }

                apiDataList.add(apiData);
            }

            saveAll(apiDataList);
        }
    }

    @Override
    public Object jsonToModel(JSONObject object) {
        List<Object> values = new ArrayList<>();

        for(final String FORMAT: RECIPE_JSON_FORMATS){
            values.add(casting.valueValidator(object.get(FORMAT)));
        }

        List<ManualPairs> pairsList = pairTagBuilder.pairListBuilder(object);

        return Recipes.builder()
                .id(casting.toLong(values.get(0)))
                .recipeName(casting.toString(values.get(1))).category(casting.toString(values.get(2)))
                .cookingMaterialExample(casting.toString(values.get(3))).cookingCompletionExample(casting.toString(values.get(4)))
                .ingredients(casting.toString(values.get(5))).cookingMethod(casting.toString(values.get(6)))
                .kcal(casting.toDouble(values.get(7)))
                .carbohydrate(casting.toDouble(values.get(8))).protein(casting.toDouble(values.get(9))).fat(casting.toDouble(values.get(10)))
                .sodium(casting.toDouble(values.get(11)))
                .manualPairsList(pairsList)
                .build();

    }

    @Override
    public boolean isContainsField(Object object) {
        Recipes recipe = (Recipes) object;
        long id = recipe.getId();

        return recipeRepository.existsById(id);
    }

    @Override
    public void saveAll(List<?> recipeList) {
        if(recipeList.size() == 0) return;

        recipeRepository.saveAll(Arrays.asList(
                recipeList.stream()
                        .toArray(Recipes[]::new)
                        .clone()
        ));
    }
}
