package com.example.backend.services.interfaces;

import com.example.backend.models.ManualImages;
import com.example.backend.models.Recipes;

import java.util.List;

public interface ManualImageService {
    void manualImageListSaver(Recipes recipe, List<String> manualImageList);

    void save(ManualImages manualImage);

    void delete(ManualImages manualImage);
}
