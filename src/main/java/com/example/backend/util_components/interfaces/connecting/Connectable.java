package com.example.backend.util_components.interfaces.connecting;

public interface Connectable {

    String STATUS_CODE = "statusCode";
    String HEADER = "header";
    String BODY = "body";

    String requestOpenApiData(String key, String name, int start, int end);

}
