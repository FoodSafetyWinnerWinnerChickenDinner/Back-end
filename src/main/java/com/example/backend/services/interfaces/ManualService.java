package com.example.backend.services.interfaces;

import com.example.backend.models.Manuals;
import com.example.backend.models.Recipes;
import org.json.simple.JSONObject;

import java.util.List;

public interface ManualService {
    void manualListSaver(JSONObject json, long id, List<String> manualList);

    String nullChecker(int idx, JSONObject object, List<String> list);

    void save(Manuals manual);

    void delete(Manuals manual);
}
