package com.example.and_p4_bakeking.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Recipe;
import com.example.and_p4_bakeking.models.Step;

import java.util.ArrayList;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;
import moe.feng.common.stepperview.VerticalStepperView;


public class StepsFragment extends Fragment implements IStepperAdapter {
    private static final String LOG_TAG = StepsFragment.class.getSimpleName();

    private Recipe mCurrentRecipe;
    ArrayList<Step> mStepsList;
    private static final String BUNDLE_KEY = "bundle_key";
    private VerticalStepperView mStepperView;

    // Required empty public constructor
    public StepsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCurrentRecipe = bundle.getParcelable(BUNDLE_KEY);
            mStepsList = mCurrentRecipe.getSteps();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        mStepperView = rootView.findViewById(R.id.vertical_stepper_view);
        mStepperView.setStepperAdapter(this);

        return rootView;
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
