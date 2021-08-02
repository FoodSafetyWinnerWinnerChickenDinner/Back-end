package com.example.backend.util_components.util_string;

import com.example.backend.util_components.interfaces.string.casting.Castable;
import org.springframework.stereotype.Component;

@Component
public class Casting implements Castable {

    @Override
    public Object valueValidator(Object value) {
        if(value == null){
            return EMPTY_STRING;
        }

        String str = value.toString();
        if(str.length() == 0){
            return EMPTY_STRING;
        }

        return value;
    }

    @Override
    public String toString(Object value) {
        return value.toString();
    }

    @Override
    public Double toDouble(Object value) {
        String valueString = value.toString();

        if(valueString.matches(IS_NUMERIC)) {
            return Double.parseDouble(valueString);
        }

        return 0.0;
    }

    @Override
    public Long toLong(Object value) {
        String valueString = toString(value);

        if(valueString.matches(IS_NUMERIC)) {
            return Long.parseLong(valueString);
        }

        return 0L;
    }

}
