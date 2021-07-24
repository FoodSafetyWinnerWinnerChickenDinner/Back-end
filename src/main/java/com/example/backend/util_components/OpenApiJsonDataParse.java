package com.example.backend.util_components;

import com.example.backend.util_components.interfaces.string.parsing.JsonParsable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OpenApiJsonDataParse implements JsonParsable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public Map<JSONArray, Integer> jsonDataParser(String name, String jsonText, int last) {
        JSONParser parser = new JSONParser();
        Map<JSONArray, Integer> jsonMap = new HashMap<>();

        try {

            JSONObject json = (JSONObject) parser.parse(jsonText);
            JSONObject jsonData = (JSONObject) json.get(name);

            if (last == INIT) {
                last = Integer.parseInt(jsonData.get(TOTAL).toString());
            }

            JSONArray jsonArray = (JSONArray) jsonData.get(LIST_FLAG);
            jsonMap.put(jsonArray, last);

        }  catch(ParseException parseException) {
            LOGGER.error(">>> JsonParsing >> exception >> ", parseException);
            parseException.printStackTrace();
        }

        return jsonMap;
    }

}
