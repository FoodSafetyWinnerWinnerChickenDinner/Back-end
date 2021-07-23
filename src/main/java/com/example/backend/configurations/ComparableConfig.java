package com.example.backend.configurations;

import com.example.backend.models.Foods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ComparableConfig implements Comparable<ComparableConfig>{

    private Foods foods;

    private double similarity;

    @Override
    public int compareTo(ComparableConfig cc) {
        return this.similarity > cc.similarity ? -1: 1;
    }

}
