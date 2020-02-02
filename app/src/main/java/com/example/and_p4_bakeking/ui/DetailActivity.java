package com.example.and_p4_bakeking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Ingredient;
import com.example.and_p4_bakeking.models.Recipe;
import com.example.and_p4_bakeking.models.Step;

import org.w3c.dom.Text;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    public static final String RECIPE_PARCEL = "recipe_parcel_key";
    private Recipe recipeSelected;

    private TextView ingredientsTv;
    private TextView stepsTv;
    private TextView servingsTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ingredientsTv = findViewById(R.id.detail_ingredients_content_tv);
        servingsTv = findViewById(R.id.detail_servings_content_tv);
        stepsTv = findViewById(R.id.detail_steps_content_tv);


        //Get intent and extras from MainActivity
        Intent getClickedRecipeFromMAin = getIntent();
        if (getClickedRecipeFromMAin.hasExtra(RECIPE_PARCEL)){
            recipeSelected = getClickedRecipeFromMAin.getParcelableExtra(RECIPE_PARCEL);
        }

        //Bind the recipe object to the UI
        String steps = "";
        String ingredients = "";
        if (recipeSelected != null){
            servingsTv.setText(String.valueOf(recipeSelected.getServings()));

            //Get the ingredients
            List<Ingredient> ingredientsList = recipeSelected.getIngredients();
            try {
                for (Ingredient ingredient : ingredientsList) {
                    ingredients += ingredient.getQuantity() + " " + ingredient.getMeasure() +
                            " " + ingredient.getIngredient() + "\n";
                }
                ingredientsTv.append(ingredients);
            } catch (NullPointerException e) {
                Log.d(LOG_TAG, "recipeList is null");
            }


            //List the steps:
            List<Step> stepsList = recipeSelected.getSteps();
            try {
                for (Step step : stepsList) {
                    steps += "Step: " + String.valueOf(step.getId()) + "\n";
                    steps += "Step Description: " + step.getDescription() + "\n";
                }
                stepsTv.append(steps);
            }catch (NullPointerException e) {
                Log.d(LOG_TAG, "stepsList is null");
            }

        } else{
            Log.d(LOG_TAG, "recipeSelected is null");
        }
    }



}
