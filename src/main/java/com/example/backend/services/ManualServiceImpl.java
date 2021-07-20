package com.example.backend.services;

import com.example.backend.models.Manuals;
import com.example.backend.repositories.ManualRepository;
import com.example.backend.services.interfaces.ManualService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManualServiceImpl implements ManualService {

    private final ManualRepository manualRepository;

    @Autowired
    public ManualServiceImpl(ManualRepository manualRepository) {
        this.manualRepository = manualRepository;
    }

    @Override
    public void manualListSaver(JSONObject json, long id, List<String> manualList) {
        Manuals manuals = new Manuals();
//        manuals.setManual1(nullChecker(0, json, manualList)); manuals.setManual2(nullChecker(1, json, manualList));
//        manuals.setManual3(nullChecker(2, json, manualList)); manuals.setManual4(nullChecker(3, json, manualList));
//        manuals.setManual5(nullChecker(4, json, manualList)); manuals.setManual6(nullChecker(5, json, manualList));
//        manuals.setManual7(nullChecker(6, json, manualList)); manuals.setManual8(nullChecker(7, json, manualList));
//        manuals.setManual9(nullChecker(8, json, manualList)); manuals.setManual10(nullChecker(9, json, manualList));
//        manuals.setManual11(nullChecker(10, json, manualList)); manuals.setManual12(nullChecker(11, json, manualList));
//        manuals.setManual13(nullChecker(12, json, manualList)); manuals.setManual14(nullChecker(13, json, manualList));
//        manuals.setManual15(nullChecker(14, json, manualList)); manuals.setManual16(nullChecker(15, json, manualList));
//        manuals.setManual17(nullChecker(16, json, manualList)); manuals.setManual18(nullChecker(17, json, manualList));
//        manuals.setManual19(nullChecker(18, json, manualList)); manuals.setManual20(nullChecker(19, json, manualList));

        save(manuals);
    }

    @Override
    public String nullChecker(int idx, JSONObject object, List<String> list) {
        Object result = object.get(list.get(idx));
        return result == null ? "": result.toString();
    }

    @Override
    public void save(Manuals manual) {
        manualRepository.save(manual);
    }

    @Override
    public void delete(Manuals manual) {
        manualRepository.delete(manual);
    }
}
