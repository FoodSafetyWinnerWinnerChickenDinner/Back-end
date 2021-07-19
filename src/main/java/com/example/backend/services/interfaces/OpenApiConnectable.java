package com.example.backend.services.interfaces;

import java.io.UnsupportedEncodingException;

public interface OpenApiConnectable {

    String FORMAT_TYPE = "json";
    String ENCODING_TYPE = "UTF-8";

    String FORWARD_SLASH = "/";
    String LIST_FLAG = "row";
    String TOTAL = "total_count";
    String CURRENT_NO = "NUM";

    int INTERVAL = 200;

    String requestOpenApiData(int START, int END);

    void updateByOpenApiData();

    String openApiUrlBuilder(int START, int END) throws UnsupportedEncodingException;

    boolean isEndIndex(Object source, String sink);

}
