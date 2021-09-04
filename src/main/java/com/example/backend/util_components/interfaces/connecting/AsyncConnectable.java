package com.example.backend.util_components.interfaces.connecting;

import java.util.concurrent.CompletableFuture;

public interface AsyncConnectable {

    int INTERVAL = 1_000;

    CompletableFuture<String> apiAsyncAccess(String key, String serviceName, int S, int E);

}
