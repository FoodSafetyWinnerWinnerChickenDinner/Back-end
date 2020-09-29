package com.example.backend.services;

import com.example.backend.api_config.FoodOpenApiConfigs;
import com.example.backend.services.interfaces.FoodOpenApiService;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class FoodOpenApiServiceImpl implements FoodOpenApiService {
    @Autowired
    private FoodOpenApiConfigs foodApi;

    private static final String SERVICE_NAME = "I2790";
    private static final String TYPE = "json";
    private static final String FORWARD_SLASH = "/";
    private static final String ENCODING_TYPE = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public String requestFoods(String startIndex, String endIndex){
        String result = "";

        try {
            StringBuilder urlBuilder = new StringBuilder(foodApi.getUrl());

            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(foodApi.getKey(), ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(SERVICE_NAME, ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(TYPE, ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(startIndex, ENCODING_TYPE));
            urlBuilder.append(FORWARD_SLASH).append(URLEncoder.encode(endIndex, ENCODING_TYPE));

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
            LOGGER.error(">>> FoodOpenApiServiceImpl >> exception >> ", exception);
        }

        return result;
    }
}
