package com.example.backend.util_components.interfaces.string.parsing;

import org.json.simple.JSONArray;

import java.util.Map;

public interface JsonParsable {

    String LIST_FLAG = "row";

    String TOTAL = "total_count";

    int INIT = 1_000_000_000;

    Map<JSONArray, Integer> jsonDataParser(String name, String jsonText, int last);

}
