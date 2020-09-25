package com.example.backend.services;

import com.example.backend.property_config.ApiConfigs;
import com.example.backend.services.interfaces.FoodOpenApiService;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class FoodOpenApiServiceImpl implements FoodOpenApiService {
    @Autowired
    private ApiConfigs foodApi;

    private static final String SERVICE_NAME = "I2790", TYPE = "json";

    @Override
    public String requestFoods(String s, String e){
        String result = "";

        try {
            StringBuilder urlBuilder = new StringBuilder(foodApi.getUrl());

            urlBuilder.append("/" + URLEncoder.encode(foodApi.getKey(), "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode(SERVICE_NAME, "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode(TYPE, "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode(s, "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode(e, "UTF-8"));

            URL url = new URL(urlBuilder.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            CachedOutputStream cached = new CachedOutputStream();
            InputStream in = url.openStream();
            IOUtils.copy(in, cached);

            result = cached.getOut().toString();

            in.close();
            cached.close();
            conn.disconnect();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return result;
    }
}
