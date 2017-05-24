package com.waqaansari.neuralnetworkexample.enums;

/**
 * Created by KFMWA916 on 4/26/2017.
 */

public enum Functions {
    SIGMOID {
        public double function(double value) {
            return 1 / (1 + Math.exp(0 - value));
        }
        public double derivative(double value) {
            return ( Math.exp(0 - value) / ( (1 + Math.exp(0 - value)) * (1 + Math.exp(0 - value)) ) );
        }
    },
    LINEAR {
        public double function(double value) {
            return value;
        }
        public double derivative(double value) {
            return 1;
        }
    };

    public abstract double function(double value);
    public abstract double derivative(double value);
}
