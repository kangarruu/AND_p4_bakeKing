package com.example.and_p4_bakeking.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Recipe;
import com.example.and_p4_bakeking.models.Step;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    public static final String RECIPE_PARCEL = "recipe_parcel_key";
    private static final String BUNDLE_KEY = "bundle_key";
    private static final String STEP_BUNDLE_KEY = "step_bundle_key";
    private Recipe mRecipeSelected;
    private Step mCurrentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Get intent and extras from MainActivity
        Intent getClickedRecipeFromMAin = getIntent();
        if (getClickedRecipeFromMAin.hasExtra(RECIPE_PARCEL)){
            mRecipeSelected = getClickedRecipeFromMAin.getParcelableExtra(RECIPE_PARCEL);
        }
        mCurrentStep = mRecipeSelected.getSteps().get(0);

        //Create and instances of  ingredientsFragment, ExoplayerFragment and stepsFragment and display them using the FragmentManager
        //Pass in the selected recipe as a bundle
        FragmentManager fragmentManager  = getSupportFragmentManager();

        //Send the recipe to the IngredientsFragment & stepsFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, mRecipeSelected);

        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.ingredient_frag_container, ingredientsFragment)
                .commit();

        StepsFragment stepsFragment = new StepsFragment();
        stepsFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.steps_frag_container, stepsFragment)
                .commit();

        Bundle stepBundle = new Bundle();
        stepBundle.putParcelable(STEP_BUNDLE_KEY, mCurrentStep);
        ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
        exoPlayerFragment.setArguments(stepBundle);
        fragmentManager.beginTransaction()
                .add(R.id.exoPlayer_frag_container, exoPlayerFragment)
                .commit();

    }

}
