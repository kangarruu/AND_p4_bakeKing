package com.example.and_p4_bakeking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.and_p4_bakeking.models.Recipe;
import com.example.and_p4_bakeking.ui.MainActivity;
import com.example.and_p4_bakeking.ui.StepsActivity;
import com.example.and_p4_bakeking.utilities.WidgetRemoteViewsService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    private static final String LOG_TAG = RecipeWidgetProvider.class.getSimpleName();
    private static final String SHARED_PREFS = "shared_preferences";
    private static final String SP_RECIPE_KEY = "shared_prefs_recipe_key";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        Intent intent = new Intent(context, WidgetRemoteViewsService.class);
        //Add the app widget ID to the intent extras
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        //set the remoteViews object to use a RemoteViews adapter
        //Connects to WidgetRemoteViewsService via the specified intent
        views.setRemoteAdapter(R.id.widget_listview, intent);
        views.setEmptyView(R.id.widget_listview, R.id.widget_empty_view);

        //get the recipe name from sharedPrefs
        SharedPreferences sharedPrefs= context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String recipeJson = sharedPrefs.getString(SP_RECIPE_KEY, null);
        Type type = new TypeToken<Recipe>() {}.getType();
        if(recipeJson != null) {
            Recipe mRecipe = gson.fromJson(recipeJson, type);
            views.setTextViewText(R.id.widget_recipe_name_tv, mRecipe.getName());
            Log.d(LOG_TAG, "Current recipe is: " + mRecipe.getName());
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}

