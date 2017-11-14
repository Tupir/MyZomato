package com.example.android.myzomato.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.example.android.myzomato.R;
import com.example.android.myzomato.detail.DetailActivity;

public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Cursor data) {


        RemoteViews views = null;
        if(data.moveToFirst() == false) {
            // Construct the RemoteViews object
            views = new RemoteViews(context.getPackageName(), R.layout.base_widget);
        }
        else{
            views = getFavoriteRestaurantsRemoteView(context);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        WidgetService.startActionUpdateRestautantsWidgets(context);
    }

    public static void updateRestaurantWidgets(Context context, AppWidgetManager appWidgetManager,
                                               int[] appWidgetIds, Cursor data) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, data);
        }
    }


    /**
     * Creates and returns the RemoteViews to be displayed in the GridView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the GridView mode widget
     */
    private static RemoteViews getFavoriteRestaurantsRemoteView(Context context) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_view);
        // Set the RecyclerViewWidgetService intent to act as the adapter for the RecyclerView
        Intent intent = new Intent(context, RecyclerViewWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        // Set the DetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, DetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        // Handle empty list
        views.setEmptyView(R.id.widget_grid_view, R.id.emptyyyy);
        return views;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

}
