package com.example.backend.services;

import com.example.backend.models.ManualImages;
import com.example.backend.models.Recipes;
import com.example.backend.repositories.ManualImageRepository;
import com.example.backend.services.interfaces.ManualImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManualImageServiceImpl implements ManualImageService {

    private final ManualImageRepository manualImageRepository;

    public ManualImageServiceImpl(ManualImageRepository manualImageRepository) {
        this.manualImageRepository = manualImageRepository;
    }

    @Override
    public void manualImageListSaver(Recipes recipe, List<String> manualImageList) {
        ManualImages manualImages = new ManualImages();
        manualImages.setRecipeId(recipe.getId());
        manualImages.setManualImage1(manualImageList.get(0)); manualImages.setManualImage2(manualImageList.get(1));
        manualImages.setManualImage3(manualImageList.get(2)); manualImages.setManualImage4(manualImageList.get(3)); manualImages.setManualImage5(manualImageList.get(4));
        manualImages.setManualImage6(manualImageList.get(5)); manualImages.setManualImage7(manualImageList.get(6)); manualImages.setManualImage8(manualImageList.get(7));
        manualImages.setManualImage9(manualImageList.get(8)); manualImages.setManualImage10(manualImageList.get(9)); manualImages.setManualImage11(manualImageList.get(10));
        manualImages.setManualImage12(manualImageList.get(11)); manualImages.setManualImage13(manualImageList.get(12)); manualImages.setManualImage14(manualImageList.get(13));
        manualImages.setManualImage15(manualImageList.get(14)); manualImages.setManualImage16(manualImageList.get(15)); manualImages.setManualImage17(manualImageList.get(16));
        manualImages.setManualImage18(manualImageList.get(17)); manualImages.setManualImage19(manualImageList.get(18)); manualImages.setManualImage20(manualImageList.get(19));

        save(manualImages);
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
