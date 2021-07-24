package com.example.backend.services.interfaces.openapi;

public interface OpenApiConnectable extends JsonDataAccessible {

    int INTERVAL = 1_000;
    int INIT = 1_000_000_000;

    void updateByOpenApiData();

}
