package com.example.and_p4_bakeking.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.VisibleForTesting;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.RecipeWidgetProvider;
import com.example.and_p4_bakeking.adapters.RecipeAdapter;
import com.example.and_p4_bakeking.models.Recipe;
import com.example.and_p4_bakeking.utilities.BakingRetrofitApi;
import com.example.and_p4_bakeking.utilities.EspressoIdlingResource;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterClickHandler {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String SHARED_PREFS = "shared_preferences";
    private static final String SP_RECIPE_KEY = "shared_prefs_recipe_key";
    //Constants for defining number of columns in the layoutManager
    private final static int SPAN_COUNT_LAND = 2;
    //Base URL For Retrofit http call
    private static final String JSON_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> recipeList;
    private BakingRetrofitApi bakingRetrofitApi;
    private ProgressBar mProgressBar;
    private ConstraintLayout mErrorLayout;

    //For Espresso testing
    @Nullable
    private EspressoIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get an idlingResource object for testing
        getIdlingResource();

        mRecipeRecyclerView = findViewById(R.id.main_recipe_rv);
        mProgressBar = findViewById(R.id.main_progress_bar);
        mErrorLayout = findViewById(R.id.main_error_parent);

        //Initialize the RecipeAdapter and set it on the RecyclerView
        mRecipeAdapter = new RecipeAdapter(recipeList, this);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);

        //Set a LayoutManager on the RecyclerView and set the number of columns based on orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
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
            //Set the idlingResource to false to indicate long running task
            mIdlingResource.setIdleState(false);
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
                    Log.d(LOG_TAG, "Retrofit Http response code: " + response.code());
                    return;
                }

                recipeList = response.body();
                mRecipeAdapter.refreshRecipeData(recipeList);
                mIdlingResource.setIdleState(true);

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
                mIdlingResource.setIdleState(true);
            }
        });
    }

    //Helper method for checking for internet connectivity
    private boolean isNetworkConnected() {
        boolean result = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        result = true;
                    }
                }
            } else {
                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = true;
                    }
                }

            }
        }
        return result;

    }

    @Override
    public void onListItemClick(Recipe clickedRecipe) {
        //Save the recipe details into Shared preferences and notify the widgetManager that the data has changed
        saveRecipeUpdateWidget(clickedRecipe);

        Intent startDetailActivity = new Intent(this, StepsActivity.class);
        startDetailActivity.putExtra(StepsActivity.RECIPE_PARCEL, clickedRecipe);
        startActivity(startDetailActivity);
    }

    //helper method for saving the clicked Recipe object into shared preferences as a Json String
    public void saveRecipeUpdateWidget(Recipe recipe) {
        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String recipeJson = gson.toJson(recipe);
        editor.putString(SP_RECIPE_KEY, recipeJson);
        editor.apply();

        //get an AppWidgetManager instance and notify clicked recipe has changed
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        widgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);

    }


    //Only called from test, creates and returns a new EspressoIdlingResource
    @VisibleForTesting
    @NonNull
    public EspressoIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new EspressoIdlingResource();
        }
        return mIdlingResource;
    }


}
