package com.waqaansari.neuralnetworkexample.utils;

import android.util.Log;

/**
 * Created by KFMWA916 on 5/1/2017.
 */
public class MatrixOperations {

    public static double[][] multiplyMatrix(double[][] matrixA, double[][] matrixB) {
        int rowsInA = matrixA.length;
        int rowsInB = matrixB.length;
        int columnsInA = matrixA[0].length; // same as rows in B
        int columnsInB = matrixB[0].length;
        double[][] result = new double[rowsInA][columnsInB];
        if(rowsInB == columnsInA) {
            for (int i = 0; i < rowsInA; i++) {
                for (int j = 0; j < columnsInB; j++) {
                    for (int k = 0; k < columnsInA; k++) {
                        result[i][j] = result[i][j] + matrixA[i][k] * matrixB[k][j];
                    }
                }
            }
        }
        return result;
    }

    public static double[][] multiplyMatrix(double[] matrixA, double[] matrixB) {
        int rowsInA = matrixA.length;
        int columnsInB = matrixB.length;
        double[][] result = new double[rowsInA][columnsInB];
        for (int i = 0; i < rowsInA; i++) {
            for (int j = 0; j < columnsInB; j++) {
                result[i][j] = result[i][j] + matrixA[i] * matrixB[j];
            }
        }
        return result;
    }

    public static double[][] multiplyMatrix(int[] matrixA, double[] matrixB) {
        int rowsInA = matrixA.length;
        int columnsInB = matrixB.length;
        double[][] result = new double[rowsInA][columnsInB];
        for (int i = 0; i < rowsInA; i++) {
            for (int j = 0; j < columnsInB; j++) {
                result[i][j] = result[i][j] + (double) matrixA[i] * matrixB[j];
            }
        }
        return result;
    }

    public static double[][] multiplyMatrix(int[] matrixA, double[][] matrixB) {
        int rowsInA = matrixA.length;
        int rowsInB = matrixB.length;
        int columnsInA = 1; // same as rows in B
        int columnsInB = matrixB[0].length;
        double[][] result = new double[rowsInA][columnsInB];
        if(rowsInB == columnsInA) {
            for (int i = 0; i < rowsInA; i++) {
                for (int j = 0; j < columnsInB; j++) {
                    for (int k = 0; k < columnsInA; k++) {
                        result[i][j] = result[i][j] + matrixA[i] * matrixB[k][j];
                    }
                }
            }
        }
        return result;
    }

    public static double[][] multiplyScalarAndMakeMatrix(double[] matrix, double factor, int rows, int columns) {
        double[][] result = new double[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                result[i][j] = factor * matrix[j];
            }
        }
        return result;
    }

    public static double[][] addMatrix(double[][] matrixA, double[][] matrixB) {
        double[][] result = new double[matrixA.length][matrixB[0].length];
        for(int row = 0; row < matrixA.length; row++) {
            for (int column = 0; column < matrixA[0].length; column++) {
                result[row][column] = matrixA[row][column] + matrixB[row][column];
            }
        }
        return result;
    }

    public static double[][] multiplyMatrix(double[] matrixA, double[][] matrixB) {
        int rowsInA = matrixA.length;
        int rowsInB = matrixB.length;
        int columnsInA = 1; // same as rows in B
        int columnsInB = matrixB[0].length;
        double[][] result = new double[rowsInA][columnsInB];
        if(rowsInB == columnsInA) {
            for (int i = 0; i < rowsInA; i++) {
                for (int j = 0; j < columnsInB; j++) {
                    for (int k = 0; k < columnsInA; k++) {
                        result[i][j] = result[i][j] + matrixA[i] * matrixB[k][j];
                    }
                }
            }
        }
        return result;
    }

    public static void displayMatrix(double[][] matrix) {
        String text = "";
        for (double[] aMatrix : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                text = text.concat(String.valueOf(aMatrix[j]).concat("\t"));
                System.out.print(aMatrix[j] + " ");
            }
            text = text.concat("\n");
            System.out.println();
        }

        Log.d("MATRIX", text);
    }

}

