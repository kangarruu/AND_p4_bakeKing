package com.example.and_p4_bakeking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.and_p4_bakeking.ui.MainActivity;
import com.example.and_p4_bakeking.utilities.WidgetRemoteViewsService;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    private static final String LOG_TAG = RecipeWidgetProvider.class.getSimpleName();
    public static final String ACTION_WIDGET_UPDATE = "com.example.and_p4_bakeking.action.widget_update";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        //Create an intent to launch MainActivity when widget is clicked
        Intent clickIntent = new Intent(context, MainActivity.class);
        PendingIntent launchMainActivity = PendingIntent.getActivity(context, 0, clickIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_parent, launchMainActivity);

        Intent intent = new Intent(context, WidgetRemoteViewsService.class);
        //Add the app widget ID to the intent extras
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        //set the remoteViews object to use a RemoteViews adapter
        //Connects to WidgetRemoteViewsService via the specified intent
        views.setRemoteAdapter(R.id.widget_listview, intent);
        views.setEmptyView(R.id.widget_listview, R.id.widget_empty_view);

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

