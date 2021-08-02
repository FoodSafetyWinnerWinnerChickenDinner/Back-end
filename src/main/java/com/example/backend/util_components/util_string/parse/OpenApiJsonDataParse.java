package com.example.backend.util_components.util_string.parse;

import com.example.backend.util_components.interfaces.string.parsing.JsonParsable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OpenApiJsonDataParse implements JsonParsable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public JSONArray jsonDataParser(String name, String jsonText) {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;

        try {

            JSONObject json = (JSONObject) parser.parse(jsonText);
            JSONObject jsonData = (JSONObject) json.get(name);

            jsonArray = (JSONArray) jsonData.get(LIST_FLAG);

        }  catch(ParseException parseException) {
            LOGGER.error(">>> JsonParsing >> exception >> ", parseException);
            parseException.printStackTrace();
        }

        return jsonArray;
    }

    @Override
    public int onlyTakeIndex(String name, String jsonText) {

        JSONParser parser = new JSONParser();
        int index = 0;

        try {

            JSONObject json = (JSONObject) parser.parse(jsonText);
            JSONObject jsonData = (JSONObject) json.get(name);

            index = Integer.parseInt(jsonData.get(TOTAL).toString());

        }  catch(ParseException parseException) {
            LOGGER.error(">>> JsonParsing >> exception >> ", parseException);
            parseException.printStackTrace();
        }

        return index;
    }

}
