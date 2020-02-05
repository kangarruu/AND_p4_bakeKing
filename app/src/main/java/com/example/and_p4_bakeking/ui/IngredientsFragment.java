package com.example.and_p4_bakeking.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.adapters.IngredientAdapter;
import com.example.and_p4_bakeking.models.Ingredient;
import com.example.and_p4_bakeking.models.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {
    private static final String LOG_TAG = IngredientsFragment.class.getSimpleName();

    private RecyclerView mIngredientRecylerView;
    private IngredientAdapter mIngredientAdapter;
    private Recipe mCurrentRecipe;
    private ArrayList<Ingredient> mIngredientsList;
    private static final String BUNDLE_KEY = "bundle_key";

    // Required empty public constructor
    public IngredientsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            mCurrentRecipe = bundle.getParcelable(BUNDLE_KEY);
            mIngredientsList = mCurrentRecipe.getIngredients();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_ingredients, container, false);
        mIngredientRecylerView = rootView.findViewById(R.id.frag_ingredients_rv);

        //Initialize the RecipeAdapter and set it on the RecyclerView
        mIngredientAdapter = new IngredientAdapter(mIngredientsList);
        mIngredientRecylerView.setAdapter(mIngredientAdapter);
        //Set a LayoutManager on the RecyclerView
        mIngredientRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIngredientRecylerView.hasFixedSize();

        //return root View
        return rootView;
    }

    private void setIngredientsListOnAdapter() {
        if(mIngredientsList != null){
            mIngredientAdapter.refreshIngredientsData(mIngredientsList);
        }
    }
}
