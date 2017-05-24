package com.waqaansari.neuralnetworkexample.utils;

import com.waqaansari.neuralnetworkexample.enums.Functions;

/**
 * Created by KFMWA916 on 4/26/2017.
 */

public class ActivationFunction {

    public static double function(double value, Functions functions) {
        if(functions.equals(Functions.SIGMOID)) {
            return sigmoid(value);
        }
        return 0f;
    }
    public static double derivativeOfFunction(double value, Functions functions) {
        if(functions.equals(Functions.SIGMOID))
            return sigmoidDerivative(value);
        return 0f;
    }

    private static double sigmoid(double value) {
        return 1 / (1 + Math.exp(0 - value));
    }
    private static double sigmoidDerivative(double value) {
        return ( Math.exp(0 - value) / ( (1 + Math.exp(0 - value)) * (1 + Math.exp(0 - value)) ) );
    }

}
