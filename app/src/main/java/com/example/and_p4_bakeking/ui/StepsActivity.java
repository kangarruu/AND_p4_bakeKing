package com.example.and_p4_bakeking.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class StepsActivity extends AppCompatActivity implements IStepperAdapter{
    private static final String LOG_TAG = StepsActivity.class.getSimpleName();

    public static final String RECIPE_PARCEL = "recipe_parcel_key";
    private static final String BUNDLE_KEY = "bundle_key";
    private static final String STEP_BUNDLE_KEY = "step_bundle_key";
    private static final String RECIPE_STATE_KEY = "recipe_state";
    private Recipe mRecipeSelected;
    private Step currentStepObject;
    ArrayList<Step> mStepsList;
    VerticalStepperView mStepperView;
    FragmentManager fragmentManager;
    ExoPlayerFragment exoPlayerFragment;
    IngredientsFragment ingredientsFragment;
    StepsFragment stepsFragment;
    View mExoplayerView;
    View mIngredientsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        if (savedInstanceState != null) {
            mRecipeSelected = savedInstanceState.getParcelable(RECIPE_STATE_KEY);
        } else {
            //Get intent and extras from MainActivity
            Intent getClickedRecipeFromMAin = getIntent();
            if (getClickedRecipeFromMAin.hasExtra(RECIPE_PARCEL)){
                mRecipeSelected = getClickedRecipeFromMAin.getParcelableExtra(RECIPE_PARCEL);
            }
        }

        //Set the title of the action bar to the selected recipe name
        if (null != getSupportActionBar()){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(mRecipeSelected.getName());
        }

        mStepsList = mRecipeSelected.getSteps();

        //Instantiate the IStepperAdapter
        mStepperView = findViewById(R.id.vertical_stepper_view);
        mStepperView.setStepperAdapter(this);

        //Instantiate the fragmentManager
        fragmentManager  = getSupportFragmentManager();

        mIngredientsView = findViewById(R.id.ingredient_frag_container);

        //For tablet layouts launch the ingredientsFragment
        // for display outside of the stepper
        if (getResources().getConfiguration().smallestScreenWidthDp >= 600){
            updateCurrentStep();
            launchIngredientsFragment();
        }

    }

    //method for displaying titles in stepper
    @NonNull
    @Override
    public CharSequence getTitle(int position) {
        return mStepsList.get(position).getShortDescription();
    }

    //method for displaying summaries in stepper
    //returning null so that content is only displayed during step click
    @Nullable
    @Override
    public CharSequence getSummary(int position) {
        return null;
    }

    //method for displaying getting size of steps ArrayList in stepper
    @Override
    public int size() {
        if(null == mStepsList){
            return 0;
        } else {
            return mStepsList.size();
        }
    }

    //method for creating custom view in stepper to display ExoplayerFragment and IngredientsFragment in phone layouts
    //and for creating buttons to click through steps
    @Override
    public View onCreateCustomView(int position, Context context, VerticalStepperItemView parent) {
        //Sync the current step index in the Step object with the current step in the adapter
        updateCurrentStep();

        View viewToInflate = LayoutInflater.from(context).inflate(R.layout.stepper_list_item, parent, false);

        //For phone layouts, inflate the exoplayerFragment inside the stepper
        //If the selected step has no video, set the fragment visibility to GONE
        if (getResources().getConfiguration().smallestScreenWidthDp <= 600) {
            mExoplayerView = viewToInflate.findViewById(R.id.exoPlayer_frag_container);
            if (currentStepObject.hasVideo()) {
                mExoplayerView.setVisibility(View.VISIBLE);
            } else {
                mExoplayerView.setVisibility(View.GONE);
            }
        }

        //set the contents textview to display the step description
        TextView contentView = viewToInflate.findViewById(R.id.stepper_content_tv);
        contentView.setText(currentStepObject.getDescription());

        Button nextButton = viewToInflate.findViewById(R.id.stepper_next_btn);
        Button backButton = viewToInflate.findViewById(R.id.stepper_back_btn);

        if (currentStepObject.getId() == 0){
            //change the initial text of the nextButton & hide the content textview
            nextButton.setText(getString(R.string.step_list_item_btn_lets_go));
            contentView.setVisibility(View.GONE);
            //For phone layouts create the fragment that displays the ingredients in the first step
            if (getResources().getConfiguration().smallestScreenWidthDp <= 600){
                launchIngredientsFragment();
            //For tablets, change the contentView text
            } else {
                contentView.setVisibility(View.VISIBLE);
                contentView.setText(R.string.step_gather_ingredients);
            }
        //Adjust tint of the buttons during last step
        } else if(currentStepObject.getId() == mStepsList.size() -1) {
            nextButton.setBackgroundTintList(getResources().getColorStateList(R.color.material_grey_500));
            backButton.setBackgroundTintList(getResources().getColorStateList(R.color.material_blue_500));
        //hide the ingredientsFragment if it's not the first step
        } else {
            hideIngredientsFragment();
        }

        //Set the clickListeners
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepperView.nextStep();
                updateCurrentStep();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != 0) {
                    mStepperView.prevStep();
                    updateCurrentStep();
                } else {
                    mStepperView.setAnimationEnabled(!mStepperView.isAnimationEnabled());
                }

            }
        });

         return viewToInflate;
    }

    private void launchIngredientsFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BUNDLE_KEY, mRecipeSelected.getIngredients());

        ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.ingredient_frag_container, ingredientsFragment)
                .commit();
    }

    private void hideIngredientsFragment() {
        if (getResources().getConfiguration().smallestScreenWidthDp >= 600){
            mIngredientsView.setVisibility(View.GONE);
            launchStepFragment();
        }
    }


    private void launchExoplayerFragment() {
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelable(STEP_BUNDLE_KEY, currentStepObject);
        exoPlayerFragment = new ExoPlayerFragment();
        exoPlayerFragment.setArguments(stepBundle);
        fragmentManager.beginTransaction()
                .replace(R.id.exoPlayer_frag_container, exoPlayerFragment)
                .commit();
    }

    private void launchStepFragment() {
        updateCurrentStep();
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelable(STEP_BUNDLE_KEY, currentStepObject);
        stepsFragment = new StepsFragment();
        stepsFragment.setArguments(stepBundle);
        fragmentManager.beginTransaction()
                .replace(R.id.steps_frag_container, stepsFragment)
                .commit();
    }

    private void updateCurrentStep(){
        int stepPosition = mStepperView.getCurrentStep();
        currentStepObject = mStepsList.get(stepPosition);
        if (currentStepObject.hasVideo()) {
            launchExoplayerFragment();
        }


    }


    @Override
    public void onShow(int i) {

    }

    @Override
    public void onHide(int i) {

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_STATE_KEY, mRecipeSelected);
    }
}
