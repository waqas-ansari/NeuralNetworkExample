package com.waqaansari.neuralnetworkexample.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waqaansari.neuralnetworkexample.R;
import com.waqaansari.neuralnetworkexample.enums.Functions;
import com.waqaansari.neuralnetworkexample.utils.ActivationFunction;
import com.waqaansari.neuralnetworkexample.utils.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleHiddenLayer extends Fragment {
    int NO_OF_INPUTS = 2;
    int NO_OF_OUTPUTS = 1;
    int NO_OF_HIDDEN_LAYERS = 4;

    int SIZE_OF_INPUT_WEIGHTS;
    int SIZE_OF_OUTPUT_WEIGHTS;

    double margin;
    double outputTemp;

    int[] INPUTS;
    int ANSWER;

    double[] inputWeights;
    double[] outputWeights;
    double[] hiddenLayerSum;
    double[] hiddenLayerResults;

    double[] tempOutputWeights;
    double changeInOutputValue;

    int steps = 0;

    LinearLayout parentLayout;
    TextView txtSteps;
    EditText edtInputOne, edtInputTwo, edtOutput, edtNoOfHiddenLayers;

    public SingleHiddenLayer() {
        // Required empty public constructor
    }

    private void showData(double answer) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.detail_view, null, false);
        TextView txtMargin = (TextView) view.findViewById(R.id.txtMargin);
        TextView txtAnswer = (TextView) view.findViewById(R.id.txtAnswer);
        ListView lstInputWeights = (ListView) view.findViewById(R.id.lstInputWeights);
        ListView lstHiddenLayerValues = (ListView) view.findViewById(R.id.lstHiddenLayerValues);
        ListView lstOutputWeights = (ListView) view.findViewById(R.id.lstOutputWeights);

        txtAnswer.setText(txtAnswer.getText().toString().concat(String.valueOf(answer)));
        txtMargin.setText(txtMargin.getText().toString().concat(String.valueOf(margin)));

        List<String> tempList = new ArrayList<>();
        for(double value : inputWeights) {
            tempList.add(String.valueOf(value));
        }
        lstInputWeights.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tempList));

        tempList = new ArrayList<>();
        for(double value : hiddenLayerResults) {
            tempList.add(String.valueOf(value));
        }
        lstHiddenLayerValues.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tempList));

        tempList = new ArrayList<>();
        for(double value : outputWeights) {
            tempList.add(String.valueOf(value));
        }
        lstOutputWeights.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tempList));
        parentLayout.addView(view);


        steps++;
        txtSteps.setText("Steps: ".concat(String.valueOf(steps)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_hidden_layer, container, false);

        parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);

        txtSteps = (TextView) view.findViewById(R.id.txtSteps);
        edtInputOne = (EditText) view.findViewById(R.id.edtInputOne);
        edtInputTwo = (EditText) view.findViewById(R.id.edtInputTwo);
        edtOutput = (EditText) view.findViewById(R.id.edtOutput);
        edtNoOfHiddenLayers = (EditText) view.findViewById(R.id.edtHiddenLayers);

        double[][] matrixA = {{1, 1}};
        double[][] matrixB = {{2, 4, 6}, {3, 5, 7}};

        MatrixOperations.displayMatrix(MatrixOperations.multiplyMatrix(matrixA, matrixB));



        view.findViewById(R.id.btnStartLearning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLayout.removeAllViews();
                steps = 0;

                if(edtInputOne.getText().toString().isEmpty() ||
                        edtInputTwo.getText().toString().isEmpty() ||
                        edtNoOfHiddenLayers.getText().toString().isEmpty() ||
                        edtOutput.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "No Input", Toast.LENGTH_SHORT).show();
                }

                int inputOne = Integer.valueOf(edtInputOne.getText().toString());
                int inputTwo = Integer.valueOf(edtInputTwo.getText().toString());


                INPUTS = new int[]{inputOne, inputTwo};
                ANSWER = Integer.valueOf(edtOutput.getText().toString());
                NO_OF_HIDDEN_LAYERS = Integer.valueOf(edtNoOfHiddenLayers.getText().toString());
                prepareLearning();
            }
        });
        return view;
    }


    private void prepareLearning() {

        SIZE_OF_INPUT_WEIGHTS = NO_OF_HIDDEN_LAYERS * NO_OF_INPUTS;
        SIZE_OF_OUTPUT_WEIGHTS = NO_OF_HIDDEN_LAYERS * NO_OF_OUTPUTS;

        inputWeights = new double[SIZE_OF_INPUT_WEIGHTS];
        outputWeights = new double[SIZE_OF_OUTPUT_WEIGHTS];
        tempOutputWeights = new double[SIZE_OF_OUTPUT_WEIGHTS];
        hiddenLayerSum = new double[NO_OF_HIDDEN_LAYERS];
        hiddenLayerResults = new double[NO_OF_HIDDEN_LAYERS];

        for(int i=0; i< inputWeights.length; i++)
            inputWeights[i] = Math.random();
        for(int i=0; i<outputWeights.length; i++)
            outputWeights[i] = Math.random();

        new StartLearning().execute();

    }

    private void applyActivationFunctionToHiddenLayer() {
        for (int i=0; i<NO_OF_HIDDEN_LAYERS; i++) {
            hiddenLayerSum[i] = INPUTS[0] * inputWeights[NO_OF_INPUTS*i] + INPUTS[1] * inputWeights[NO_OF_INPUTS*i + 1];
            hiddenLayerResults[i] = Functions.SIGMOID.function(hiddenLayerSum[i]);
        }
    }
    private double getAnswer() {
        double answer = 0;
        for(int i=0; i<NO_OF_HIDDEN_LAYERS; i++) {
            answer += hiddenLayerResults[i] * outputWeights[i];
        }
        outputTemp = answer;
        return Functions.SIGMOID.function(answer);
    }
    private void reCalculateHiddenToOutputWeights(double answer) {
        margin = ANSWER - answer;
        changeInOutputValue = Functions.SIGMOID.derivative(outputTemp) * margin;

        double[] changeInWeights = new double[SIZE_OF_OUTPUT_WEIGHTS];
        for(int i=0; i<changeInWeights.length; i++) {
            changeInWeights[i] = changeInOutputValue * hiddenLayerResults[i];
        }

        for(int i=0; i<outputWeights.length; i++) {
            tempOutputWeights[i] = outputWeights[i];
            outputWeights[i] = outputWeights[i] + changeInWeights[i];
        }
    }
    private void reCalculateInputToHiddenWeights() {
        double[] changeInHiddenLayerValues = new double[NO_OF_HIDDEN_LAYERS];
        for(int i=0; i<changeInHiddenLayerValues.length; i++) {
            changeInHiddenLayerValues[i] = changeInOutputValue * tempOutputWeights[i] * Functions.SIGMOID.derivative(hiddenLayerSum[i]);
        }

        double[] changeInWeights = new double[SIZE_OF_INPUT_WEIGHTS];
        for(int i=0; i<NO_OF_INPUTS; i++) {
            for(int j=0; j<changeInHiddenLayerValues.length; j++) {
                changeInWeights[NO_OF_INPUTS*j + i] = INPUTS[i] * changeInHiddenLayerValues[j];
            }
        }

        for(int i=0; i<inputWeights.length; i++) {
            inputWeights[i] = inputWeights[i] + changeInWeights[i];
        }

    }


    private void makeSystemLearn() {
        applyActivationFunctionToHiddenLayer();
        final double answer = getAnswer();
        showLog(answer);
        double HIGH_FACTOR = ANSWER + 0.09;
        double LOW_FACTOR = ANSWER - 0.09;
        if(answer <= HIGH_FACTOR && answer >= LOW_FACTOR) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showData(ANSWER);
                }
            });
            return;
        }
        reCalculateHiddenToOutputWeights(answer);
        reCalculateInputToHiddenWeights();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showData(answer);
            }
        });
        makeSystemLearn();
    }


    private class StartLearning extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            makeSystemLearn();
            return null;
        }
    }

    private void showLog(double answer) {
        String text = "";
        for(double value : outputWeights)
            text = text.concat(String.valueOf(value)).concat("\t");
        Log.d("OUTPUT WEIGHTS", text);

        text = "";
        for(double value : inputWeights)
            text = text.concat(String.valueOf(value)).concat("\t");
        Log.d("INPUT WEIGHTS", text);
        Log.d("ANSWER", String.valueOf(answer));
    }


}
