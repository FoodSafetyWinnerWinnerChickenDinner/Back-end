package com.example.backend.services;

import com.example.backend.configurations.FoodOpenApiConfig;
import com.example.backend.services.interfaces.FoodOpenApiService;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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

    private static final String SERVICE_NAME = "I2790";
    private static final String TYPE = "json";
    private static final String FORWARD_SLASH = "/";
    private static final String ENCODING_TYPE = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    public FoodOpenApiServiceImpl(FoodOpenApiConfig foodApi) {
        this.foodApi = foodApi;
    }

    @Override
    public String requestFoods(int startIndex, int endIndex) {
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
}
