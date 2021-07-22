package com.example.backend.services.interfaces;

import java.util.ArrayList;
import java.util.List;

public interface Selectable {

    boolean isContainsField(Object object);

    default List<?> getListAll(){ return new ArrayList<>(); }

}
