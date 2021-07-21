package com.example.backend.services.interfaces;

import org.json.simple.JSONObject;

public interface DataAccessible {

    String[] FOOD_JSON_FORMATS = {"NUM", "DESC_KOR", "GROUP_NAME", "SERVING_SIZE",
            "NUTR_CONT1", "NUTR_CONT2", "NUTR_CONT3", "NUTR_CONT4",
            "NUTR_CONT5", "NUTR_CONT6", "NUTR_CONT7", "NUTR_CONT8",
            "NUTR_CONT9"};

    String[] RECIPE_JSON_FORMATS = {"RCP_SEQ", "RCP_NM", "RCP_PAT2"
            , "ATT_FILE_NO_MK", "ATT_FILE_NO_MAIN", "RCP_PARTS_DTLS", "RCP_WAY2"
            , "INFO_ENG", "INFO_CAR", "INFO_PRO", "INFO_FAT", "INFO_NA"};

    String IS_NUMERIC = "-?\\d+(\\.\\d+)?";
    String EMPTY_STRING = "";

    Object jsonToModel(JSONObject object);

    Object valueValidator(Object value);

    String toString(Object value);

    Double toDouble(Object value);

    Long toLong(Object value);

    boolean dbContainsData(Object object);
}
