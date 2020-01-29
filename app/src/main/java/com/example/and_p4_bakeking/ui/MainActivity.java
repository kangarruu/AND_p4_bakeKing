package com.example.and_p4_bakeking.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Recipe;
import com.example.and_p4_bakeking.models.Step;
import com.example.and_p4_bakeking.utilities.BakingRetrofitApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private TextView textViewJsonResults;
    private BakingRetrofitApi bakingRetrofitApi;

    //Base URL For Retrofit http call
    private static final String JSON_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewJsonResults = findViewById(R.id.temp_json_results);

        //Retrofit instantiation
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JSON_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bakingRetrofitApi = retrofit.create(BakingRetrofitApi.class);

        if (isNetworkConnected()) {
            getRecipesWithRetrofit();
        } else {
            //Log error and show internet connectivity error message
        }


    }

    private void getRecipesWithRetrofit() {
        Call<ArrayList<Recipe>> call = bakingRetrofitApi.getRecipes();
        //Make the call off the main thread
        call.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    if (!response.isSuccessful()) {
                        textViewJsonResults.setText("Http response code: " + response.code());
                        return;
                    }

                    ArrayList<Recipe> recipes = response.body();

                    for (Recipe recipe : recipes) {
                        String content = "";
                        content += "Id: " + recipe.getId() + "\n";
                        content += "Name: " + recipe.getName() + "\n";
                        for (Step step : recipe.getSteps()){
                            content += "Step number: " + step.getId() + "\n";
                            content += "Video: " + step.getVideoURL() + "\n\n";
                        }

                        textViewJsonResults.append(content);
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    textViewJsonResults.setText(t.getMessage());

                }
        });
    }

    //Helper method for checking for internet connectivity
    private boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetwork() != null && manager.getActiveNetworkInfo().isConnected();
    }
}
