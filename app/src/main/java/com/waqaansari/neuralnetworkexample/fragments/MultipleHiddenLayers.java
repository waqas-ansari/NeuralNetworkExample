package com.waqaansari.neuralnetworkexample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waqaansari.neuralnetworkexample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultipleHiddenLayers extends Fragment {


    public MultipleHiddenLayers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_hidden_layers, container, false);
    }

}
