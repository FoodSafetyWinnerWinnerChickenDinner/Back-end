package com.example.backend.services.interfaces;

import java.net.MalformedURLException;

public interface FoodOpenApiService {
    public String requestFoods(String s, String e) throws MalformedURLException;
}
