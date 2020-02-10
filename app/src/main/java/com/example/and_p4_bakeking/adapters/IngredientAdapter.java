package com.example.and_p4_bakeking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Ingredient;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.ceil;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {
    private static final String LOG_TAG = IngredientAdapter.class.getSimpleName();

    private ArrayList<Ingredient> mIngredientsList;

    //constructor
    public IngredientAdapter(ArrayList<Ingredient> ingredientsList) {
        mIngredientsList = ingredientsList;
    }


    //Inflate the list item layout
    //@return a new IngredientAdapterViewHolder that will hold the view for each list item
    @NonNull
    @Override
    public IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int list_item_layout = R.layout.ingredients_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View viewToInflate = inflater.inflate(list_item_layout, parent, false);
        return new IngredientAdapterViewHolder(viewToInflate);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapterViewHolder holder, int position) {
        Ingredient currentIngredient = mIngredientsList.get(position);
        //Bind the ingredient details to the UI
        String ingredient = currentIngredient.getIngredient();
        holder.ingredientView.setText(ingredient);

        Double quantity = currentIngredient.getQuantity();
        //Remove trailing 0s
        DecimalFormat format = new DecimalFormat("0.##");
        holder.quantityView.setText(String.valueOf(format.format(quantity)));

        String measure = currentIngredient.getMeasure();
        holder.measureView.setText(measure);

    }

    @Override
    public int getItemCount() {
        if (null == mIngredientsList) {
            return 0;
        } else {
            return mIngredientsList.size();
        }
    }

    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientView;
        private TextView quantityView;
        private TextView measureView;

        public IngredientAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientView = itemView.findViewById(R.id.ing_list_item_ingredient_tv);
            quantityView = itemView.findViewById(R.id.ing_list_item_quantity_tv);
            measureView = itemView.findViewById(R.id.ing_list_item_measure_tv);
        }
    }

    public void refreshIngredientsData(ArrayList<Ingredient> ingredientData){
        mIngredientsList = ingredientData;
        notifyDataSetChanged();
    }

}
