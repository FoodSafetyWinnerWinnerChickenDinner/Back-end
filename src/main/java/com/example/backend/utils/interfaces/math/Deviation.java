package com.example.backend.utils.interfaces.math;

import java.util.List;

public interface Deviation extends Average{

    double getDeviation(List<Double> value, double avg);

}
