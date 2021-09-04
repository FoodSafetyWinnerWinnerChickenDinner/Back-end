package com.example.backend.util_components.interfaces.connecting;

public interface Connectable {

    String requestOpenApiData(String key, String name, int start, int end);

    String responseTotalIndex(String key, String name, int start, int end);
}
