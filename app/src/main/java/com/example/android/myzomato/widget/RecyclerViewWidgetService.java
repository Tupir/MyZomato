package com.example.android.myzomato.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.myzomato.R;
import com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry;


public class RecyclerViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecyclerViewRemoteViewsFactory(this.getApplicationContext());
    }
}

class RecyclerViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;

    public RecyclerViewRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Uri forecastQueryUri = RestaurantEntry.CONTENT_URI;
        String select = "("+ RestaurantEntry.COLUMN_FAVORITE + "=1)";
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(forecastQueryUri,
                null,
                select,
                null,
                null);
    }

    @Override
    public void onDestroy() {
            mCursor.close();
    }

    @Override
    public int getCount(){
            if (mCursor == null) return 0;
            return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {

        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);

        int waterTimeIndex = mCursor.getColumnIndex( RestaurantEntry.COLUMN_ID);
        int plantTypeIndex = mCursor.getColumnIndex( RestaurantEntry.COLUMN_NAME);

        int id = mCursor.getInt(waterTimeIndex);
        String name = mCursor.getString(plantTypeIndex);




        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_view_item);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("id", id);
        views.setOnClickFillInIntent(R.id.widget_grid_view_item, fillInIntent);

        //views.setTextViewText(R.id.menu_title, Integer.toString(id));
        views.setTextViewText(R.id.widget_grid_view_item, String.valueOf(name));


        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

