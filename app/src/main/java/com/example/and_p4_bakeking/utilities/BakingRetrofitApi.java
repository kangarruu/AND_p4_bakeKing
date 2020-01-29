package com.example.and_p4_bakeking.utilities;

import com.example.and_p4_bakeking.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingRetrofitApi {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
