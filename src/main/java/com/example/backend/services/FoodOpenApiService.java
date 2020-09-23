package com.example.backend.services;

import com.example.backend.models.AuthorizationKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class FoodOpenApiService {
    @Autowired
    private AuthorizationKey authKey;

    private static final String SERVICE_NAME = "I2790", TYPE = "json", NEW_LINE = "\n";

    public String requestFoods() {
        StringBuffer result = new StringBuffer();

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

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String apiLines;

            while ((apiLines = reader.readLine()) != null) {
                result.append(apiLines).append(NEW_LINE);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
