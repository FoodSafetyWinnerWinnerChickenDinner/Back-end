package com.example.backend.utils.interfaces.connecting;

public interface RestTemplateConnectable {

    String STATUS_CODE = "statusCode";
    String HEADER = "header";
    String BODY = "body";

    String requestOpenApiData(String url, String key, String name, int start, int end);

}
