package com.example.backend.services;

import com.example.backend.models.Manuals;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.ManualRepository;
import com.example.backend.services.interfaces.ManualService;
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
    public void manualListSaver(Recipes recipe, List<String> manualList) {
        Manuals manuals = new Manuals();
        manuals.setRecipeId(recipe.getId());
        manuals.setManual1(manualList.get(0)); manuals.setManual2(manualList.get(1));
        manuals.setManual3(manualList.get(2)); manuals.setManual4(manualList.get(3)); manuals.setManual5(manualList.get(4));
        manuals.setManual6(manualList.get(5)); manuals.setManual7(manualList.get(6)); manuals.setManual8(manualList.get(7));
        manuals.setManual9(manualList.get(8)); manuals.setManual10(manualList.get(9)); manuals.setManual11(manualList.get(10));
        manuals.setManual12(manualList.get(11)); manuals.setManual13(manualList.get(12)); manuals.setManual14(manualList.get(13));
        manuals.setManual15(manualList.get(14)); manuals.setManual16(manualList.get(15)); manuals.setManual17(manualList.get(16));
        manuals.setManual18(manualList.get(17)); manuals.setManual19(manualList.get(18)); manuals.setManual20(manualList.get(19));

        save(manuals);
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
