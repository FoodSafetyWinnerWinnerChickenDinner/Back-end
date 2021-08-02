package com.example.backend.util_components.util_math;

import com.example.backend.util_components.interfaces.math.Similarity;
import org.springframework.stereotype.Component;

@Component
public class CosineSimilarity implements Similarity {

    @Override
    public double division(double A, double B) {
        if(B == 0) return 0;
        return A / B;
    }

    @Override
    public double scalarProduct(double[] ingested, double[] fields) {
        double left = 0;
        double right = 0;

        for(int i = 0; i < ingested.length; i++) {
            left += Math.pow(ingested[i], 2);
        }

        for(int i = 0; i < fields.length; i++) {
            right += Math.pow(fields[i], 2);
        }

        return Math.sqrt(left) * Math.sqrt(right);
    }

    @Override
    public double vectorProduct(double[] ingested, double[] fields) {
        double innerProduct = 0;

        for(int i = 0; i < ingested.length; i++) {
            innerProduct += ingested[i] * fields[i];
        }

        return innerProduct;
    }
}
