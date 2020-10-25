package com.example.backend.services;

import com.example.backend.configurations.FoodOpenApiConfig;
import com.example.backend.models.Foods;
import com.example.backend.repositories.FoodOpenApiRepository;
import com.example.backend.services.interfaces.FoodOpenApiService;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class FoodOpenApiServiceImpl implements FoodOpenApiService {

    private final FoodOpenApiConfig foodApi;

    private final FoodOpenApiRepository foodOpenApiRepository;

    private static final String SERVICE_NAME = "I2790";
    private static final String TYPE = "json";
    private static final String FORWARD_SLASH = "/";
    private static final String ENCODING_TYPE = "UTF-8";

    private final String LIST_FLAG = "row";
    private final int INTERVAL = 200;
    private final int LAST_INDEX = 28_488;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    public FoodOpenApiServiceImpl(FoodOpenApiConfig foodApi, FoodOpenApiRepository foodOpenApiRepository) {
        this.foodApi = foodApi;
        this.foodOpenApiRepository = foodOpenApiRepository;
    }

    @Override
    public void dataUpdateProcessorByFoodOpenApi() {
        int start = 1;
        int end = INTERVAL;

        while(start <= LAST_INDEX) {
            String jsonText = requestFoodLists(start, end);
            JSONParser parser = new JSONParser();

            try {
                JSONObject json = (JSONObject) parser.parse(jsonText);

                JSONObject jsonFood = (JSONObject) json.get(SERVICE_NAME);
                JSONArray jsonArray = (JSONArray) jsonFood.get(LIST_FLAG);

                int size = jsonArray.size();
                for (int i = 0; i < size; i++) {
                    JSONObject food = (JSONObject) jsonArray.get(i);

                    Foods apiData = new Foods();
                    String foodName = food.get("DESC_KOR").toString();
                    String category = food.get("GROUP_NAME").toString();
                    double total = validation(food.get("SERVING_SIZE").toString());

                    if(findByNameAndCategory(foodName, category, total) != null) continue;

                    apiData.setFoodName(foodName);
                    apiData.setCategory(category);
                    apiData.setTotal(total);
                    apiData.setKcal(validation(food.get("NUTR_CONT1").toString()));
                    apiData.setCarbohydrate(validation(food.get("NUTR_CONT2").toString()));
                    apiData.setProtein(validation(food.get("NUTR_CONT3").toString()));
                    apiData.setFat(validation(food.get("NUTR_CONT4").toString()));
                    apiData.setSugar(validation(food.get("NUTR_CONT5").toString()));
                    apiData.setSodium(validation(food.get("NUTR_CONT6").toString()));
                    apiData.setCholesterol(validation(food.get("NUTR_CONT7").toString()));
                    apiData.setSaturatedFattyAcid(validation(food.get("NUTR_CONT8").toString()));
                    apiData.setTransFat(validation(food.get("NUTR_CONT9").toString()));

                    save(apiData);
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
    public String requestFoodLists(int startIndex, int endIndex) {
        String result = "";
        StringBuilder urlBuilder = new StringBuilder(foodApi.getUrl());

        try {
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(foodApi.getKey(), ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(SERVICE_NAME, ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(TYPE, ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(startIndex);
            urlBuilder.append(FORWARD_SLASH).append(endIndex);

            URL url = new URL(urlBuilder.toString());

            try (CachedOutputStream cached = new CachedOutputStream();
                 InputStream in = url.openStream()) {

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                IOUtils.copy(in, cached);
                result = cached.getOut().toString();

                conn.disconnect();
            } catch (Exception exception) {
                LOGGER.error(">>> FoodOpenApiServiceImpl >> exception >> ", exception);
            }
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            LOGGER.error(">>> FoodOpenApiServiceImpl >> exception >> ", unsupportedEncodingException);
        }
        catch (MalformedURLException malformedURLException) {
            LOGGER.error(">>> FoodOpenApiServiceImpl >> exception >> ", malformedURLException);
        }

        return result;
    }

    @Override
    public void save(Foods food) {
        foodOpenApiRepository.save(food);
    }

    @Override
    public void delete(Foods food) {
        foodOpenApiRepository.delete(food);
    }

    @Override
    public double validation(String data) {
        if(data.length() == 0) return 0.0;
        else return Double.parseDouble(data);
    }

    @Override
    public Foods findByNameAndCategory(String name, String category, double total) {
        return foodOpenApiRepository.findByNameAndCategory(name, category, total);
    }
}
