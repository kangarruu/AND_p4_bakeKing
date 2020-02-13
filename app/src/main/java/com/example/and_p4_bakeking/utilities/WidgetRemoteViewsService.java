package com.example.and_p4_bakeking.utilities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.RecipeWidgetProvider;
import com.example.and_p4_bakeking.models.Ingredient;
import com.example.and_p4_bakeking.models.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;

public class WidgetRemoteViewsService extends RemoteViewsService {

    private static final String LOG_TAG = WidgetRemoteViewsService.class.getSimpleName();
    private static final String RECIPE_NAME_KEY = "intent_recipe_extra";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private static final String SHARED_PREFS = "shared_preferences";
        private static final String SP_RECIPE_KEY = "shared_prefs_recipe_key";
        SharedPreferences sharedPrefs;
        private Context mContext;
        private List<Ingredient> mIngredientsList;
        private Recipe mRecipe;
        private int mWidgetId;


        public WidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
            mContext = applicationContext;
            mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

            sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        }

        @Override
        public void onDataSetChanged() {
            Gson gson = new Gson();
            String recipeJson = sharedPrefs.getString(SP_RECIPE_KEY, null);
            Type type = new TypeToken<Recipe>() {
            }.getType();
            if (recipeJson != null) {
                mRecipe = gson.fromJson(recipeJson, type);
                mIngredientsList = mRecipe.getIngredients();
            }

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mIngredientsList == null) {
                return 0;
            } else {
                return mIngredientsList.size();
            }
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

            //Remove trailing 0s for the quantity
            DecimalFormat format = new DecimalFormat("0.##");

            String ingredient = mIngredientsList.get(position).getIngredient();
            String quantity = format.format(mIngredientsList.get(position).getQuantity());
            String measure = mIngredientsList.get(position).getMeasure();

            //Hide the measure if it == "unit"
            if (measure.equals(getString(R.string.widget_measure_text_unit))) {
                remoteViews.setViewVisibility(R.id.widget_item_measure_tv, View.INVISIBLE);
            } else {
                remoteViews.setViewVisibility(R.id.widget_item_measure_tv, View.VISIBLE);
            }

            //Set the ingredients info into the list_item
            remoteViews.setTextViewText(R.id.widget_item_ingredient_tv, ingredient);
            remoteViews.setTextViewText(R.id.widget_item_quantity_tv, quantity);
            remoteViews.setTextViewText(R.id.widget_item_measure_tv, measure);

            updateRecipeTitle();

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        //update the recipe title to match the current recipe
        private void updateRecipeTitle() {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);

            String recipeName = mRecipe.getName();
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(mContext);
            int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(mContext, RecipeWidgetProvider.class));
            remoteViews.setTextViewText(R.id.widget_recipe_name_tv, recipeName);
            widgetManager.updateAppWidget(appWidgetIds, remoteViews);


        }
    }


}
