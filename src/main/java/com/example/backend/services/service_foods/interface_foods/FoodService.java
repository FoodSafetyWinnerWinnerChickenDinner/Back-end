package com.example.backend.services.service_foods.interface_foods;

import com.example.backend.services.interfaces.db_access.Readable;
import com.example.backend.services.interfaces.recommend.Recommendable;

public interface FoodService extends Recommendable, Readable {

    int PACK = 10;
    int NUTRIENT_TYPES = 10;

}
