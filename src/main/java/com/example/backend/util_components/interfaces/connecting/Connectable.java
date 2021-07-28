package com.example.backend.util_components.interfaces.connecting;

import java.util.concurrent.ExecutionException;

public interface Connectable {

    String STATUS_CODE = "statusCode";
    String HEADER = "header";
    String BODY = "body";

    String requestOpenApiData(String key, String name, int start, int end) throws ExecutionException, InterruptedException;

    String responseTotalIndex(String key, String name, int start, int end);
}
