package com.example.backend.services.interfaces;

import java.util.List;

public interface DataBaseAccessible extends Selectable{

    void saveAll(List<?> list);

}
