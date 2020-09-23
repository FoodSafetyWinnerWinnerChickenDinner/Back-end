package com.example.backend.services;

import com.example.backend.models.AuthorizationKey;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FoodOpenApiService {
    @Autowired
    private AuthorizationKey authKey;

    private static final String SERVICE_NAME = "I2790", TYPE = "json", NEW_LINE = "\n";

    public JSONObject requestFoods() {
        StringBuffer result = new StringBuffer();
        JSONObject json = new JSONObject();

        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.foodsafetykorea.go.kr/api");

            urlBuilder.append("/" + URLEncoder.encode(authKey.getKey(), "UTF-8"));
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

                    System.out.println("1 ==> " + food.get("NUTR_CONT1"));
                    System.out.println("2 ==> " + food.get("NUTR_CONT2"));
                    System.out.println("3 ==> " + food.get("NUTR_CONT3"));
                    System.out.println("4 ==> " + food.get("NUTR_CONT4"));
                    System.out.println("5 ==> " + food.get("NUTR_CONT5"));
                    System.out.println("6 ==> " + food.get("NUTR_CONT6"));
                    System.out.println("7 ==> " + food.get("NUTR_CONT7"));
                    System.out.println("8 ==> " + food.get("NUTR_CONT8"));
                    System.out.println("9 ==> " + food.get("NUTR_CONT9"));
                    System.out.println();
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
