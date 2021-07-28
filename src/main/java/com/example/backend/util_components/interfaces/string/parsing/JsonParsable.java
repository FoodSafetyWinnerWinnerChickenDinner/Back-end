package com.example.backend.util_components.interfaces.string.parsing;

import org.json.simple.JSONArray;

public interface JsonParsable {

    String LIST_FLAG = "row";

    String TOTAL = "total_count";

    JSONArray jsonDataParser(String name, String jsonText);

    int onlyTakeIndex(String name, String jsonText);
}
