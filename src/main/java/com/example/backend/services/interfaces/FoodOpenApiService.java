package com.example.backend.services.interfaces;

import java.net.MalformedURLException;

public interface FoodOpenApiService {
    public String requestFoodLists(int startIndex, int endIndex) throws MalformedURLException;
}
