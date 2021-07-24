package com.example.backend.services.interfaces.openapi;

public interface OpenApiConnectable extends JsonDataAccessible {

    String LIST_FLAG = "row";
    String TOTAL = "total_count";

    String STATUS_CODE = "statusCode";
    String HEADER = "header";
    String BODY = "body";

    int INTERVAL = 1_000;
    int INIT = 1_000_000_000;

    String requestOpenApiData(int START, int END);

    void updateByOpenApiData();

}
