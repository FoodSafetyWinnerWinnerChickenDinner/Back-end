package com.example.backend.util_components;

import com.example.backend.util_components.interfaces.math.Deviation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StandardDeviation implements Deviation {

    @Override
    public double getAverage(List<Double> value, int pow) {
        double avg = 0;

        for(Double v: value) {
            avg += Math.pow(v, pow);
        }

        return avg / (double) value.size();
    }

    @Override
    public double getDeviation(List<Double> value, double avg) {

        double similarityAvgPow = avg;
        if(avg == -1){
            similarityAvgPow = getAverage(value, 1);
        }

        similarityAvgPow *= similarityAvgPow;
        double similarityPowAvg = getAverage(value, 2);

        return Math.sqrt(similarityPowAvg - similarityAvgPow);

    }
}
