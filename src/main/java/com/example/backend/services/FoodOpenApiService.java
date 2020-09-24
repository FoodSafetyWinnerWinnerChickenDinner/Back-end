package com.example.backend.services;

import com.example.backend.property_config.ApiConfigs;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class FoodOpenApiService {
    @Autowired
    private ApiConfigs foodApi;

    private static final String SERVICE_NAME = "I2790", TYPE = "json", NEW_LINE = "\n";

    public String requestFoods(){
        String result = "";

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

            CachedOutputStream cached = new CachedOutputStream();
            InputStream in = url.openStream();
            IOUtils.copy(in, cached);
            in.close();
            cached.close();

            result = cached.getOut().toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
