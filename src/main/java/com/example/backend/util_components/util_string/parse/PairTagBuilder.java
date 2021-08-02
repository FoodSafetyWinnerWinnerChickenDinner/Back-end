package com.example.backend.util_components.util_string.parse;

import com.example.backend.models.ManualPairs;
import com.example.backend.util_components.interfaces.string.CuisinePairBuilder;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PairTagBuilder implements CuisinePairBuilder {

    @Override
    public List<ManualPairs> pairListBuilder(JSONObject object) {
        List<ManualPairs> pairsList = new ArrayList<>();

        for(int prev = 0; prev <= 2; prev++) {
            int init = prev == 0 ? 1: 0;

            for(int post = init; post <= 9; post++) {

                String man = getJsonValue(object, tagBuilder(MAN, prev, post));
                String img = getJsonValue(object, tagBuilder(MAN_IMG, prev, post));
                if(man.equals(EMPTY_STRING)) return pairsList;

                pairsList.add(
                        ManualPairs.builder()
                                .manual(man)
                                .manualImage(img)
                        .build()
                );
            }
        }

        return pairsList;
    }

    @Override
    public String tagBuilder(final String HEAD, int tenth, int unit) {
        return new StringBuilder()
                .append(HEAD).append(tenth).append(unit)
                .toString();
    }

    public String getJsonValue(JSONObject json, String tag) {
        if(json.get(tag) == null) return EMPTY_STRING;
        return json.get(tag).toString();
    }
}
