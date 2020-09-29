package com.example.backend.services.interfaces;

import java.net.MalformedURLException;

public interface FoodOpenApiService {
    public String requestFoods(int startIndex, int endIndex) throws MalformedURLException;
}
