package com.example.backend.util_components;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LastIndexTracker {

    private final OpenApiConnectorByWebClient byWebClient;

    private final OpenApiJsonDataParse jsonDataParser;

    public int findTag(String key, String name) {

        String jsonText = byWebClient
                .requestOpenApiData(key, name, 1, 2);

        return jsonDataParser
                .onlyTakeIndex(name, jsonText);

    }
}
