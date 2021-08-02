package com.example.backend.util_components.util_string.parse;

import com.example.backend.util_components.util_connector.OpenApiConnectorByWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LastIndexTracker {

    private final OpenApiConnectorByWebClient byWebClient;

    private final OpenApiJsonDataParse jsonDataParser;

    public int findTag(String key, String name) {

        String jsonText = byWebClient
                .responseTotalIndex(key, name, 1, 2);

        return jsonDataParser
                .onlyTakeIndex(name, jsonText);

    }
}
