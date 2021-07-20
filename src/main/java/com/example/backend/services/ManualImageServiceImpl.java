package com.example.backend.services;

import com.example.backend.models.ManualImages;
import com.example.backend.repositories.ManualImageRepository;
import com.example.backend.services.interfaces.ManualImageService;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManualImageServiceImpl implements ManualImageService {

    private final ManualImageRepository manualImageRepository;

    public ManualImageServiceImpl(ManualImageRepository manualImageRepository) {
        this.manualImageRepository = manualImageRepository;
    }

    @Override
    public void manualImageListSaver(JSONObject json, long id, List<String> manualImageList) {
        ManualImages manualImages = new ManualImages();
        manualImages.setManualImage1(nullChecker(0, json, manualImageList)); manualImages.setManualImage2(nullChecker(1, json, manualImageList));
        manualImages.setManualImage3(nullChecker(2, json, manualImageList)); manualImages.setManualImage4(nullChecker(3, json, manualImageList));
        manualImages.setManualImage5(nullChecker(4, json, manualImageList)); manualImages.setManualImage6(nullChecker(5, json, manualImageList));
        manualImages.setManualImage7(nullChecker(6, json, manualImageList)); manualImages.setManualImage8(nullChecker(7, json, manualImageList));
        manualImages.setManualImage9(nullChecker(8, json, manualImageList)); manualImages.setManualImage10(nullChecker(9, json, manualImageList));
        manualImages.setManualImage11(nullChecker(10, json, manualImageList)); manualImages.setManualImage12(nullChecker(11, json, manualImageList));
        manualImages.setManualImage13(nullChecker(12, json, manualImageList)); manualImages.setManualImage14(nullChecker(13, json, manualImageList));
        manualImages.setManualImage15(nullChecker(14, json, manualImageList)); manualImages.setManualImage16(nullChecker(15, json, manualImageList));
        manualImages.setManualImage17(nullChecker(16, json, manualImageList)); manualImages.setManualImage18(nullChecker(17, json, manualImageList));
        manualImages.setManualImage19(nullChecker(18, json, manualImageList)); manualImages.setManualImage20(nullChecker(19, json, manualImageList));

        save(manualImages);
    }

    @Override
    public String nullChecker(int idx, JSONObject object, List<String> list) {
        Object result = object.get(list.get(idx));
        return result == null ? "": result.toString();
    }

    @Override
    public void save(ManualImages manualImage) {
        manualImageRepository.save(manualImage);
    }

    @Override
    public void delete(ManualImages manualImage) {
        manualImageRepository.delete(manualImage);
    }
}
