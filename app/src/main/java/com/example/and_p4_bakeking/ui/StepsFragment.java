package com.example.and_p4_bakeking.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Step;


public class StepsFragment extends Fragment {
    private static final String LOG_TAG = StepsFragment.class.getSimpleName();

    private static final String CURRENT_STEP_PARCEL = "parcel_key";
    private static final String STEP_BUNDLE_KEY = "step_bundle_key";
    private static final String CURRENT_STEP_STATE = "step_state";

    private Step mCurrentStep;
    private TextView stepTv;

    // Required empty public constructor
    public StepsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentStep = savedInstanceState.getParcelable(CURRENT_STEP_PARCEL);
        } else {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mCurrentStep = bundle.getParcelable(STEP_BUNDLE_KEY);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        stepTv = rootView.findViewById(R.id.step_tv);
        stepTv.setText(mCurrentStep.getDescription());

        return rootView;
    }

    //Save the state of the app so video could be retrieved on rotation
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_STEP_STATE, mCurrentStep);
    }


}
