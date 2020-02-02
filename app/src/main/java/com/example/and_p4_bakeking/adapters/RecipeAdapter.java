package com.example.and_p4_bakeking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();

    private ArrayList<Recipe> mRecipeList;
    private RecipeAdapterClickHandler mClickHandler;

    //Constructor for creating a RecipeAdapter
    public RecipeAdapter(ArrayList<Recipe> recipeList, RecipeAdapterClickHandler clickHandler) {
        mRecipeList = recipeList;
        mClickHandler = clickHandler;
    }

    //Inflate the list item layout
    //@return a new RecipeAdapterViewHolder that will hold the view for each list item
    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int list_item_layout = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View viewToInflate = inflater.inflate(list_item_layout, parent, false);
        return new RecipeAdapterViewHolder(viewToInflate);
    }

    //Bind the data to the views in the viewHolder
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeAdapterViewHolder holder, int position) {
        Recipe currentRecipe = mRecipeList.get(position);
        //get the name of the current recipe and set it on the tv
        String recipeName = currentRecipe.getName();
        holder.mRecipeNameTv.setText(recipeName);
        holder.mRecipeIv.setImageResource(R.drawable.recipe_default_img);                        //Photo by Lukas from Pexels

    }

    @Override
    public int getItemCount() {
        if(null == mRecipeList){
            return 0;
        } else {
            return mRecipeList.size();
        }
    }

    //Locates and stores the necessary views for each Recipe list item
    public class RecipeAdapterViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mRecipeIv;
        private TextView mRecipeNameTv;

        //Constructor for creating RecipeAdapterViewHolder
        public RecipeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeIv = itemView.findViewById(R.id.list_item_recipe_image);
            mRecipeNameTv = itemView.findViewById(R.id.list_item_recipe_name_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();
            Recipe clickedRecipe = mRecipeList.get(itemPosition);
            mClickHandler.onListItemClick(clickedRecipe);
        }
    }

    public interface RecipeAdapterClickHandler {
        void onListItemClick(Recipe clickedRecipe);
    }

    public void refreshRecipeData(ArrayList<Recipe> recipeData){
        mRecipeList = recipeData;
        notifyDataSetChanged();
    }
}
