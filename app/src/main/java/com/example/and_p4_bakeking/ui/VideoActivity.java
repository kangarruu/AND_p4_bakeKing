package com.example.and_p4_bakeking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Step;

public class VideoActivity extends AppCompatActivity {

    private static final String STEP_STATE_KEY = "step_state";
    private static final String STEP_BUNDLE_KEY = "step_bundle_key";

    private Step mCurrentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);

        if (savedInstanceState != null) {
            mCurrentStep = savedInstanceState.getParcelable(STEP_STATE_KEY);
        } else {
            //Get intent and extras from StepActivity
            Intent intentFromStepActivity = getIntent();
            if (intentFromStepActivity.hasExtra(StepsActivity.STEP_PARCEL)) {
                mCurrentStep = intentFromStepActivity.getParcelableExtra(StepsActivity.STEP_PARCEL);
            }

            Bundle stepBundle = new Bundle();
            stepBundle.putParcelable(STEP_BUNDLE_KEY, mCurrentStep);
            ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
            exoPlayerFragment.setArguments(stepBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.exoPlayer_frag_container, exoPlayerFragment)
                    .commit();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_STATE_KEY, mCurrentStep);
    }

}
