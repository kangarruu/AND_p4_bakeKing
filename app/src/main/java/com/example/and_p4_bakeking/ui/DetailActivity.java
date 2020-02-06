package com.example.and_p4_bakeking.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Recipe;
import com.example.and_p4_bakeking.models.Step;

import java.util.ArrayList;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;
import moe.feng.common.stepperview.VerticalStepperView;

public class DetailActivity extends AppCompatActivity implements IStepperAdapter {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    public static final String RECIPE_PARCEL = "recipe_parcel_key";
    private static final String BUNDLE_KEY = "bundle_key";
    private static final String STEP_BUNDLE_KEY = "step_bundle_key";
    private Recipe mRecipeSelected;
    private Step mCurrentStep;
    ArrayList<Step> mStepsList;
    VerticalStepperView mStepperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mStepperView = findViewById(R.id.vertical_stepper_view);
        mStepperView.setStepperAdapter(this);


        //Get intent and extras from MainActivity
        Intent getClickedRecipeFromMAin = getIntent();
        if (getClickedRecipeFromMAin.hasExtra(RECIPE_PARCEL)){
            mRecipeSelected = getClickedRecipeFromMAin.getParcelableExtra(RECIPE_PARCEL);
        }

        mStepsList = mRecipeSelected.getSteps();
//        mCurrentStep = mRecipeSelected.getSteps().get(0);

        //Create and instances of  ingredientsFragment, ExoplayerFragment and stepsFragment and display them using the FragmentManager
        //Pass in the selected recipe as a bundle
//        FragmentManager fragmentManager  = getSupportFragmentManager();
//
//        //Send the recipe to the IngredientsFragment & stepsFragment
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(BUNDLE_KEY, mRecipeSelected);
//
//        IngredientsFragment ingredientsFragment = new IngredientsFragment();
//        ingredientsFragment.setArguments(bundle);
//        fragmentManager.beginTransaction()
//                .add(R.id.ingredient_frag_container, ingredientsFragment)
//                .commit();
//
//        StepsFragment stepsFragment = new StepsFragment();
//        stepsFragment.setArguments(bundle);
//        fragmentManager.beginTransaction()
//                .add(R.id.steps_frag_container, stepsFragment)
//                .commit();
//
//        Bundle stepBundle = new Bundle();
//        stepBundle.putParcelable(STEP_BUNDLE_KEY, mCurrentStep);
//        ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
//        exoPlayerFragment.setArguments(stepBundle);
//        fragmentManager.beginTransaction()
//                .add(R.id.exoPlayer_frag_container, exoPlayerFragment)
//                .commit();

    }

    @NonNull
    @Override
    public CharSequence getTitle(int position) {
        return mStepsList.get(position).getShortDescription();
    }

    @Nullable
    @Override
    public CharSequence getSummary(int position) {
        return mStepsList.get(position).getDescription();
    }

    @Override
    public int size() {
        if(null == mStepsList){
            return 0;
        } else {
            return mStepsList.size();
        }

    }

    @Override
    public View onCreateCustomView(int position, Context context, VerticalStepperItemView parent) {
        View viewToInflate = LayoutInflater.from(context).inflate(R.layout.stepper_list_item, parent, false);
        TextView contentView = viewToInflate.findViewById(R.id.stepper_content_tv);
        contentView.setText(mStepsList.get(position).getDescription());

        Button nextButton = viewToInflate.findViewById(R.id.stepper_next_btn);
        Button backButton = viewToInflate.findViewById(R.id.stepper_back_btn);

        //Set the initial button text to "Let's go"
        //Remove tint during last step
        if (position == 0){
            nextButton.setText(getString(R.string.step_list_item_btn_lets_go));
        }else if(position == mStepsList.size() -1) {
            nextButton.setBackgroundTintList(getResources().getColorStateList(R.color.material_grey_500));
            backButton.setBackgroundTintList(getResources().getColorStateList(R.color.material_blue_500));
        }

        //Set the clickListeners
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepperView.nextStep();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != 0) {
                    mStepperView.prevStep();
                } else {
                    mStepperView.setAnimationEnabled(!mStepperView.isAnimationEnabled());
                }

            }
        });

        return viewToInflate;
    }

    @Override
    public void onShow(int i) {

    }

    @Override
    public void onHide(int i) {

    }

}
