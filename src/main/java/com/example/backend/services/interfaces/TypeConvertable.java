package com.example.backend.services.interfaces;

public interface TypeConvertable {

    String IS_NUMERIC = "-?\\d+(\\.\\d+)?";
    String EMPTY_STRING = "";

    Object valueValidator(Object value);

    String toString(Object value);

    Double toDouble(Object value);

    Long toLong(Object value);
}
