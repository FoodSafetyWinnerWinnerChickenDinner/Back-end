package com.example.backend.services.interfaces;

import java.util.List;

public interface DataBaseAccessible {

    boolean isContainsField(Object object);

    void saveAll(List<?> list);

}
