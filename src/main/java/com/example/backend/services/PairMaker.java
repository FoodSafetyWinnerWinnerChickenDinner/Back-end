package com.example.backend.services;

import com.example.backend.models.ManualPairs;
import com.example.backend.repositories.ManualPairRepository;
import com.example.backend.services.interfaces.string_manipulation.CuisinePairBuilder;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PairMaker implements CuisinePairBuilder {

    private final ManualPairRepository manualPairRepository;

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
