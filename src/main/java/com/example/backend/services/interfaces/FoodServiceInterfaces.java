package com.example.backend.services.interfaces;

import com.example.backend.models.Foods;

import java.util.Collection;

public interface FoodServiceInterfaces {
    public Foods findById(Long id);

    public void foodOpenApiProcessor();

    public void save(Foods account);

    public void delete(Foods account);
}
