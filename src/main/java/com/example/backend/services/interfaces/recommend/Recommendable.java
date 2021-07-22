package com.example.backend.services.interfaces.recommend;

import java.util.List;

public interface Recommendable {

    List<?> menuRecommender(double[] ingested);

}
