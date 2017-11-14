package com.example.android.myzomato.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.myzomato.R;
import com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry;


public class WidgetService extends IntentService{


    public static final String SOME_ACTION = "com.example.android.myzomato.action.do_something";
    public static final String SHOW_RESTAURANTS = "update_restaurants_widgets";

    public WidgetService() {
        super("WidgetService");
    }



    /**
     * Starts this service to perform UpdatePlantWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateRestautantsWidgets(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(SHOW_RESTAURANTS);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (SOME_ACTION.equals(action)) {
                //something
            } else if (SHOW_RESTAURANTS.equals(action)) {
                handleActionUpdateRestaurantWidgets();
            }
        }
    }



    private void handleActionUpdateRestaurantWidgets() {

        Uri forecastQueryUri = RestaurantEntry.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        String select = "("+ RestaurantEntry.COLUMN_FAVORITE + "=1)";
        Cursor cursor = getContentResolver().query(forecastQueryUri,
                null,
                select,
                null,
                null);


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        //Now update all widgets
        BakingWidgetProvider.updateRestaurantWidgets(this, appWidgetManager, appWidgetIds, cursor);
    }



}
