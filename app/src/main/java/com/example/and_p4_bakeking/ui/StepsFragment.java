package com.example.and_p4_bakeking.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Recipe;
import com.example.and_p4_bakeking.models.Step;

import java.util.ArrayList;


public class StepsFragment extends Fragment {
    private static final String LOG_TAG = StepsFragment.class.getSimpleName();

    private Recipe mCurrentRecipe;
    private ArrayList<Step> mStepsList;
    private static final String BUNDLE_KEY = "bundle_key";
    TextView tempTv;
    int stepNumber;
    String shortDescription;
    String directions;
    String videoUrl;

    // Required empty public constructor
    public StepsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            mCurrentRecipe = bundle.getParcelable(BUNDLE_KEY);
            mStepsList = mCurrentRecipe.getSteps();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        tempTv = rootView.findViewById(R.id.temp_tv);
        for(Step step : mStepsList){
            stepNumber = step.getId();
            shortDescription = step.getShortDescription();
            directions = step.getDescription();
            videoUrl = step.getVideoURL();
            tempTv.append("Step " + stepNumber + "\n" +
                    "Description: " + shortDescription + "\n" +
                    "Directions: " + directions + "\n\n");
       }


        return rootView;
    }
}
