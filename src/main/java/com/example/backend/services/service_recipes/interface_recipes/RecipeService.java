package com.example.backend.services.service_recipes.interface_recipes;

import com.example.backend.services.interfaces.db_access.Readable;
import com.example.backend.services.interfaces.recommend.Recommendable;

public interface RecipeService extends Recommendable, Readable {

    int PACK = 10;
    int NUTRIENT_TYPES = 10;

}
