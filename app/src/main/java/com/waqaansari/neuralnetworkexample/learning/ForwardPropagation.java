package com.waqaansari.neuralnetworkexample.learning;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.waqaansari.neuralnetworkexample.enums.Functions;
import com.waqaansari.neuralnetworkexample.utils.MatrixOperations;

/**
 * Created by KFMWA916 on 4/26/2017.
 */

public class ForwardPropagation extends AsyncTask<Void, Void, Void> {
    private Context context;
    private ProgressDialog dialog;

    private int noOfInputs;
    private int noOfOutputs;
    private int depth;              //0 means one hidden layer
    private int noOfHiddenNodes;

    private int[] inputs;
    private int actualOutput;
    private double calculatedOutput;

    private double outputSum;


    private double[][] inputWeights;
    private double[][][] hiddenLayersWeights;
    private double[][] outputWeights;
    private double[][] hiddenLayerSum;
    private double[][] hiddenLayerResults;

    private double[][] tempOutputWeights;
    private double changeInOutputValue;

    private double[][][] tempHiddenLayerWeights;

    private BackwardPropagation backward;


    private double[][] assignWeights(int row, int column) {
        double[][] values = new double[row][column];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                values[i][j] = Math.random();
            }
        }
        return values;
    }

    public ForwardPropagation(Context context, int depth, int noOfHiddenNodes,
                              int[] inputs, int actualOutput) {
        this.context = context;

        this.depth = depth;
        this.noOfHiddenNodes = noOfHiddenNodes;
        this.inputs = inputs;
        this.actualOutput = actualOutput;

        this.noOfInputs = inputs.length;
        this.noOfOutputs = 1;

        backward = new BackwardPropagation();
    }

    private void applyActivationFunctionToHiddenLayerSum(int layer) {
        for(int j=0; j<hiddenLayerResults[0].length; j++) {
            hiddenLayerResults[layer+1][j] = Functions.SIGMOID.function(hiddenLayerSum[layer+1][j]);
        }
    }

    private void prepareLearning() {

        inputWeights = assignWeights(noOfInputs, noOfHiddenNodes);
        outputWeights = assignWeights(noOfHiddenNodes, noOfOutputs);
        tempOutputWeights = assignWeights(noOfHiddenNodes, noOfOutputs);

        if(depth > 0) {
            hiddenLayerSum = new double[depth+1][noOfHiddenNodes];
            hiddenLayerResults = new double[depth+1][noOfHiddenNodes];
            hiddenLayersWeights = new double[depth][noOfHiddenNodes][noOfHiddenNodes];
            tempHiddenLayerWeights = new double[depth][noOfHiddenNodes][noOfHiddenNodes];
            for(int i=0; i<depth; i++)
                hiddenLayersWeights[i] = assignWeights(depth, noOfHiddenNodes);
        } else {
            hiddenLayerSum = new double[1][noOfHiddenNodes];
            hiddenLayerResults = new double[1][noOfHiddenNodes];
        }

    }

    private void calculateFirstHiddenLayerSumAndResult() {
        hiddenLayerSum = MatrixOperations.multiplyMatrix(inputs, inputWeights);
        applyActivationFunctionToHiddenLayerSum(0);
    }

    private void calculateNHiddenLayerSumAndResult() {
        if(depth > 0) {
            for(int depth = 0; depth <= this.depth; depth++) {
                double[][] tempHiddenLayerSum = MatrixOperations.multiplyMatrix(hiddenLayerResults[depth], hiddenLayersWeights[depth]);
                //Assign values to hiddenLayerSum[depth+1]
                System.arraycopy(tempHiddenLayerSum[0], 0, hiddenLayerSum[depth + 1], 0, tempHiddenLayerSum[0].length);
                applyActivationFunctionToHiddenLayerSum(depth+1);
            }
        }
    }

    private void calculateOutput() {
        double[][] tempAnswer = MatrixOperations.multiplyMatrix(hiddenLayerResults[depth], outputWeights);
        outputSum = tempAnswer[0][0];
        calculatedOutput = Functions.SIGMOID.function(outputSum);
    }



    private void putAllTogether() {
        goForward();
        if(calculatedOutput == (double) actualOutput)
            return;
        goBackward();
        putAllTogether();
    }

    private void goForward() {
        prepareLearning();
        calculateFirstHiddenLayerSumAndResult();
        calculateNHiddenLayerSumAndResult();
        calculateOutput();
    }

    private void goBackward() {
        backward.reCalculateHiddenToOutputWeights();
        backward.reCalculateFirstHiddenToHiddenWeights();
        backward.reCalculateNHiddenToHiddenWeights();
        backward.reCalculateInputToHiddenWeights();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Learning...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        putAllTogether();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
    }

    private class BackwardPropagation {

        private void reCalculateHiddenToOutputWeights() {
            double margin = actualOutput - calculatedOutput;
            changeInOutputValue = Functions.SIGMOID.derivative(outputSum) * margin;

            double[][] changeInWeights = new double[noOfHiddenNodes][noOfOutputs];

            for(int i=0; i<changeInWeights.length; i++) {
                changeInWeights = MatrixOperations.multiplyScalarAndMakeMatrix(hiddenLayerResults[depth], changeInOutputValue,
                        noOfHiddenNodes, noOfOutputs);
            }

            tempOutputWeights = outputWeights;
            outputWeights = MatrixOperations.addMatrix(outputWeights, changeInWeights);

        }

        private void reCalculateFirstHiddenToHiddenWeights() {
            if(depth > 0) {
                double[] changeInHiddenLayerValues = new double[noOfHiddenNodes];
                tempHiddenLayerWeights[depth] = hiddenLayersWeights[depth];

                for(int i=0; i<noOfHiddenNodes; i++) {
                    double temp = 0f;
                    for(int j=0; j<noOfOutputs; j++) {
                        temp += tempOutputWeights[i][j];
                    }
                    changeInHiddenLayerValues[i] =
                            changeInOutputValue * temp * Functions.SIGMOID.derivative(hiddenLayerSum[depth][i]);
                }

                double[][] changeInWeights = MatrixOperations.multiplyMatrix(changeInHiddenLayerValues, hiddenLayerResults[depth-1]);
                hiddenLayersWeights[depth] = MatrixOperations.addMatrix(hiddenLayersWeights[depth], changeInWeights);
            }
        }

        private void reCalculateNHiddenToHiddenWeights() {
            if(depth > 1) {
                for(int a = depth; a >= 1; a--) {
                    double[] changeInHiddenLayerValues = new double[noOfHiddenNodes];
                    tempHiddenLayerWeights[a] = hiddenLayersWeights[a];

                    for(int i=0; i<noOfHiddenNodes; i++) {
                        double temp = 0f;
                        for(int j=0; j<noOfHiddenNodes; j++) {
                            temp += tempHiddenLayerWeights[a][i][j];
                        }
                        changeInHiddenLayerValues[i] =
                                changeInOutputValue * temp * Functions.SIGMOID.derivative(hiddenLayerSum[a][i]);
                    }

                    double[][] changeInWeights = MatrixOperations.multiplyMatrix(changeInHiddenLayerValues, hiddenLayerResults[a-1]);
                    hiddenLayersWeights[a] = MatrixOperations.addMatrix(hiddenLayersWeights[a], changeInWeights);
                }

            }
        }

        private void reCalculateInputToHiddenWeights() {
            double[] changeInHiddenLayerValues = new double[noOfHiddenNodes];

            for(int i=0; i<noOfHiddenNodes; i++) {
                double temp = 0f;
                for(int j=0; j<noOfOutputs; j++) {
                    temp += tempHiddenLayerWeights[0][i][j];
                }
                changeInHiddenLayerValues[i] =
                        changeInOutputValue * temp * Functions.SIGMOID.derivative(hiddenLayerSum[0][i]);
            }

            double[][] changeInWeights = MatrixOperations.multiplyMatrix(inputs, changeInHiddenLayerValues);
            inputWeights = MatrixOperations.addMatrix(inputWeights, changeInWeights);
        }

    }


}
