package com.example.backend.services.interfaces;

import com.example.backend.models.ManualImages;
import com.example.backend.models.Recipes;
import org.json.simple.JSONObject;

import java.util.List;

public interface ManualImageService {
    void manualImageListSaver(JSONObject json, long id, List<String> manualImageList);

    String nullChecker(int idx, JSONObject object, List<String> list);

    void save(ManualImages manualImage);

    void delete(ManualImages manualImage);
}
