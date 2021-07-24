package com.example.backend.util_components.interfaces.string;

import com.example.backend.models.ManualPairs;
import org.json.simple.JSONObject;

import java.util.List;

public interface CuisinePairBuilder {

    String MAN = "MANUAL";
    String MAN_IMG = "MANUAL_IMG";
    String EMPTY_STRING = "";

    List<ManualPairs> pairListBuilder(JSONObject object);

    String tagBuilder(String HEAD, int tenth, int unit);

}
