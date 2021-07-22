package com.example.backend.services.interfaces.openapi;

import java.io.UnsupportedEncodingException;

public interface OpenApiConnectable {

    String FORMAT_TYPE = "json";
    String ENCODING_TYPE = "UTF-8";

    String FORWARD_SLASH = "/";
    String LIST_FLAG = "row";
    String TOTAL = "total_count";

    int INTERVAL = 1_000;
    int INIT = 1_000_000_000;

    String requestOpenApiData(int START, int END);

    void updateByOpenApiData();

    String openApiUrlBuilder(int START, int END) throws UnsupportedEncodingException;

}
