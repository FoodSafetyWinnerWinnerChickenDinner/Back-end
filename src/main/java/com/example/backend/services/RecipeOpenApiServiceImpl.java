package com.example.backend.services;

import com.example.backend.configurations.RestTemplateConfig;
import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.RecipeOpenApiRepository;
import com.example.backend.services.interfaces.RecipeOpenApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class RecipeOpenApiServiceImpl implements RecipeOpenApiService {

    private final OpenApiConfig recipeApi;

    private final ManualServiceImpl manualService;

    private final ManualImageServiceImpl manualImageService;

    private final RecipeOpenApiRepository recipeOpenApiRepository;

    private final RestTemplateConfig restTemplate;

    private static final String TYPE = "json";
    private static final String FORWARD_SLASH = "/";
    private static final String ENCODING_TYPE = "UTF-8";

    private final String LIST_FLAG = "row";
    private final int INTERVAL = 200;
    private final int LAST_INDEX = 1_237;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Autowired
    public RecipeOpenApiServiceImpl(OpenApiConfig recipeApi, ManualServiceImpl manualService, ManualImageServiceImpl manualImageService, RecipeOpenApiRepository recipeOpenApiRepository, RestTemplateConfig restTemplate) {
        this.recipeApi = recipeApi;
        this.manualService = manualService;
        this.manualImageService = manualImageService;
        this.recipeOpenApiRepository = recipeOpenApiRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public void recipesDataBaseUpdateProcessorByRecipeOpenApi() {
        int start = 1;
        int end = INTERVAL;

        while(start <= LAST_INDEX) {
            String jsonText = requestRecipeLists(start, end);
            JSONParser parser = new JSONParser();

            try {
                JSONObject json = (JSONObject) parser.parse(jsonText);

                JSONObject jsonFood = (JSONObject) json.get(recipeApi.getRecipeServiceName());
                JSONArray jsonArray = (JSONArray) jsonFood.get(LIST_FLAG);

                int size = jsonArray.size();
                for (int i = 0; i < size; i++) {
                    JSONObject recipe = (JSONObject) jsonArray.get(i);

                    Recipes apiData = new Recipes();
                    String recipeName = recipe.get("RCP_NM").toString();
                    if(exceptDuplicatedData(recipeName)) continue;

                    String category = recipe.get("RCP_PAT2").toString();
                    String cookingMaterialExample = recipe.get("ATT_FILE_NO_MK").toString();
                    String cookingCompletionExample = recipe.get("ATT_FILE_NO_MAIN").toString();
                    String ingredient = recipe.get("RCP_PARTS_DTLS").toString();
                    String cookingMethod = recipe.get("RCP_WAY2").toString();
                    double kcal = validation(recipe.get("INFO_ENG").toString());
                    double carbohydrate = validation(recipe.get("INFO_CAR").toString());
                    double protein = validation(recipe.get("INFO_PRO").toString());
                    double fat = validation(recipe.get("INFO_FAT").toString());
                    double sodium = validation(recipe.get("INFO_NA").toString());

//                    apiData.setRecipeName(recipeName);
//                    apiData.setCategory(category);
//                    apiData.setCookingMaterialExample(cookingMaterialExample);
//                    apiData.setCookingCompletionExample(cookingCompletionExample);
//                    apiData.setIngredients(ingredient);
//                    apiData.setCookingMethod(cookingMethod);
//                    apiData.setKcal(kcal);
//                    apiData.setCarbohydrate(carbohydrate);
//                    apiData.setProtein(protein);
//                    apiData.setFat(fat);
//                    apiData.setFat(sodium);

                    save(apiData);

                    ArrayList<String> manualList = manualBuilder("MANUAL");
                    manualService.manualListSaver(recipe, apiData.getId(), manualList);

                    ArrayList<String> manualImageList = manualBuilder("MANUAL_IMG");
                    manualImageService.manualImageListSaver(recipe, apiData.getId(), manualImageList);
                }

            } catch (ParseException parseException) {
                LOGGER.error(">>> FoodsServiceImpl >> exception >> ", parseException);
                parseException.printStackTrace();
            }

            start += INTERVAL;
            end += INTERVAL;
        }
    }

    @Override
    public String requestRecipeLists(int startIndex, int endIndex) {
        String jsonInString = null;
        Map<String, Object> jsonData = new HashMap<>();
        StringBuilder urlBuilder = new StringBuilder(recipeApi.getUrl());

        try {
            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(recipeApi.getKey(), ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(recipeApi.getRecipeServiceName(), ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(TYPE, ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(startIndex);
            urlBuilder.append(FORWARD_SLASH).append(endIndex);

            ResponseEntity<Map> resultMap = restTemplate.getCustomRestTemplate()
                    .exchange(urlBuilder.toString(), HttpMethod.GET, entity, Map.class);

            jsonData.put("statusCode", resultMap.getStatusCodeValue());   // http status
            jsonData.put("header", resultMap.getHeaders());               // header
            jsonData.put("body", resultMap.getBody());                    // body

            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());
        }
        catch (UnsupportedEncodingException | JsonProcessingException unsupportedEncodingException) {
            LOGGER.error(">>> FoodOpenApiServiceImpl >> exception >> ", unsupportedEncodingException);
        }

        return jsonInString;
    }

    @Override
    public double validation(String data) {
        if(data.length() == 0) return 0.0;
        else return Double.parseDouble(data);
    }

    @Override
    public ArrayList<String> manualBuilder(String target) {
        ArrayList<String> list = new ArrayList<>();
        StringBuilder keyBuilder;

        for(int prev = 0; prev <= 2; prev++) {
            for(int post = 1; post <= 9; post++) {
                keyBuilder = new StringBuilder();
                list.add(keyBuilder.append(target).append(prev).append(post).toString());
            }
        }

        return list;
    }

    @Override
    public boolean exceptDuplicatedData(String name) {
        return recipeOpenApiRepository.findByName(name) != null;
    }

    @Override
    public void save(Recipes recipe) {
        recipeOpenApiRepository.save(recipe);
    }

    @Override
    public void delete(Recipes recipe) {
        recipeOpenApiRepository.delete(recipe);
    }
}
