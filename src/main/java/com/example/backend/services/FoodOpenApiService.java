package com.example.backend.services;

import com.example.backend.models.AuthorizationKey;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.simple.JSONObject;
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

            String data = bos.getOut().toString();
            json.put("data", data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }
}
