package com.example.backend.services.interfaces;

import com.example.backend.models.Manuals;
import com.example.backend.models.Recipes;

import java.util.List;

public interface ManualService {
    void manualListSaver(Recipes recipe, List<String> manualList);

    void save(Manuals manual);

    void delete(Manuals manual);
}
