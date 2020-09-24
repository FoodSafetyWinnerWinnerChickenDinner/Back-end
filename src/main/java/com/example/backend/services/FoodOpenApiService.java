package com.example.backend.services;

import com.example.backend.property_config.ApiConfigs;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
//@EnableScheduling
public class FoodOpenApiService {
    @Autowired
    private ApiConfigs foodApi;

    private static final String SERVICE_NAME = "I2790", TYPE = "json", NEW_LINE = "\n";

//    @Scheduled(cron = "0 0 18 * * *")
//    public void requestFoods() {
    public JSONObject requestFoods(){
        StringBuffer result = new StringBuffer();
        JSONObject json = new JSONObject();

        try {
            StringBuilder urlBuilder = new StringBuilder(foodApi.getUrl());

            urlBuilder.append("/" + URLEncoder.encode(foodApi.getKey(), "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode(SERVICE_NAME, "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode(TYPE, "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode("5", "UTF-8"));

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            InputStream in = url.openStream();
            CachedOutputStream bos = new CachedOutputStream();
            IOUtils.copy(in, bos);
            in.close();
            bos.close();

            String jsonText = bos.getOut().toString();
            JSONParser parser = new JSONParser();

            try {
                json = (JSONObject) parser.parse(jsonText);

                JSONObject jsonFood = (JSONObject) json.get(SERVICE_NAME);
                JSONArray jsonArray = (JSONArray) jsonFood.get("row");

                for(int i = 0; i < 5; i++) {
                    JSONObject food = (JSONObject) jsonArray.get(i);

                    System.out.println("food_name \t" + food.get("DESC_KOR"));
                    System.out.println("total \t" + food.get("SERVING_SIZE"));
                    System.out.println("kcal \t" + food.get("NUTR_CONT1"));
                    System.out.println("carbohydrate \t" + food.get("NUTR_CONT2"));
                    System.out.println("protein \t" + food.get("NUTR_CONT3"));
                    System.out.println("fat \t" + food.get("NUTR_CONT4"));
                    System.out.println("sugar \t" + food.get("NUTR_CONT5"));
                    System.out.println("sodium \t" + food.get("NUTR_CONT6"));
                    System.out.println("cholesterol \t" + food.get("NUTR_CONT7"));
                    System.out.println("saturated_fatty_acid \t" + food.get("NUTR_CONT8"));
                    System.out.println("trans_fat \t" + food.get("NUTR_CONT9"));
                    System.out.println();

                    // DB update logic
                }

            } catch (ParseException e) {
                System.out.println("failed!");
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }
}
