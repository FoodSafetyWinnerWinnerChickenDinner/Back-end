package com.example.backend.services.interfaces.db_access;

import java.util.ArrayList;
import java.util.List;

public interface Readable {

    boolean isContainsField(Object object);

    default List<?> getListAll(){ return new ArrayList<>(); }

}
