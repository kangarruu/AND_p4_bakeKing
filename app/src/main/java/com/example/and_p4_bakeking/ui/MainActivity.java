package com.example.and_p4_bakeking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.adapters.RecipeAdapter;
import com.example.and_p4_bakeking.models.Recipe;
import com.example.and_p4_bakeking.utilities.BakingRetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterClickHandler{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> recipeList;

    //Constants for defining number of columns in the layoutManager
    private final static int SPAN_COUNT_LAND = 2;


    private BakingRetrofitApi bakingRetrofitApi;
    private ProgressBar mProgressBar;
    private ConstraintLayout mErrorLayout;


    //Base URL For Retrofit http call
    private static final String JSON_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecipeRecyclerView = findViewById(R.id.main_recipe_rv);
        mProgressBar = findViewById(R.id.main_progress_bar);
        mErrorLayout = findViewById(R.id.main_error_parent);

        //Initialize the RecipeAdapter and set it on the RecyclerView
        mRecipeAdapter = new RecipeAdapter(recipeList, this);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);

        //Set a LayoutManager on the RecyclerView and set the number of columns based on orientation
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT_LAND));
        }
        mRecipeRecyclerView.hasFixedSize();

        //Retrofit instantiation
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JSON_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bakingRetrofitApi = retrofit.create(BakingRetrofitApi.class);

        if (isNetworkConnected()) {
            getRecipesWithRetrofit();
        } else {
            showErrorMessage();
        }

    }

    private void showErrorMessage() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mErrorLayout.setVisibility(View.VISIBLE);               // cloud error image from <a href="https://www.vecteezy.com/free-vector/bed">Bed Vectors by Vecteezy</a>
    }

    private void getRecipesWithRetrofit() {
        Call<ArrayList<Recipe>> call = bakingRetrofitApi.getRecipes();
        //Make the call off the main thread
        call.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    if (!response.isSuccessful()) {
                        Log.d(LOG_TAG,"Retrofit Http response code: " + response.code());
                        return;
                    }

                    recipeList = response.body();
                    mRecipeAdapter.refreshRecipeData(recipeList);

                    }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    Log.d(LOG_TAG, t.getMessage());
                }
        });
    }

    //Helper method for checking for internet connectivity
    private boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetwork() != null && manager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onListItemClick(Recipe clickedRecipe) {
        Intent startDetailActivity = new Intent(this, DetailActivity.class);
        startDetailActivity.putExtra(DetailActivity.RECIPE_PARCEL, clickedRecipe);
        startActivity(startDetailActivity);
    }


}
