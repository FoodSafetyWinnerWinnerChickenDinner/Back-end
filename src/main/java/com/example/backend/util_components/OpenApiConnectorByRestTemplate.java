package com.example.backend.util_components;

import com.example.backend.configurations.rest_template.RestTemplateConfig;
import com.example.backend.util_components.interfaces.connecting.RestTemplateConnectable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenApiConnectorByRestTemplate implements RestTemplateConnectable {

    private final RestTemplateConfig restTemplate;

    private final OpenApiUrlBuilder urlBuilder;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public String requestOpenApiData(String url, String key, String name, int start, int end) {
        String jsonInString = null;
        Map<String, Object> jsonData = new HashMap<>();

        try {
            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            String openApiUrl = urlBuilder.openApiUrlBuilder(url, key, name ,start ,end);

            ResponseEntity<Map> resultMap = restTemplate.getCustomRestTemplate()
                    .exchange(openApiUrl, HttpMethod.GET, entity, Map.class);

            jsonData.put(STATUS_CODE, resultMap.getStatusCodeValue());   // http status
            jsonData.put(HEADER, resultMap.getHeaders());               // header
            jsonData.put(BODY, resultMap.getBody());                    // body

            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());
        }
        catch (UnsupportedEncodingException | JsonProcessingException exception) {
            LOGGER.error(">>> OpenApi >> exception >> ", exception);
        }

        return jsonInString;
    }

}
