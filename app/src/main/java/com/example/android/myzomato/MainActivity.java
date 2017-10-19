package com.example.android.myzomato;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.myzomato.data.RestaurantDbHelper;
import com.example.android.myzomato.data.RestaurantTableContents;
import com.example.android.myzomato.sync.ZomatoSyncUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        RestaurantAdapter.ForecastAdapterOnClickHandler {


    public static final String[] MAIN_RESTAURANT_PROJECTION = {
            RestaurantTableContents.RestaurantEntry.COLUMN_ID,
            RestaurantTableContents.RestaurantEntry.COLUMN_NAME,
            RestaurantTableContents.RestaurantEntry.COLUMN_IMAGE,
    };

    public static final int INDEX_COLUMN_ID = 0;
    public static final int INDEX_COLUMN_NAME = 1;
    public static final int INDEX_COLUMN_IMAGE = 2;



    private static final int LOADER_ID = 0;
    private RestaurantAdapter restaurantAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private SQLiteDatabase mDb;

    private ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        showLoading();

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        restaurantAdapter = new RestaurantAdapter(this, this);
        mRecyclerView.setAdapter(restaurantAdapter);

        RestaurantDbHelper dbHelper = RestaurantDbHelper.getInstance(this);
        mDb = dbHelper.getWritableDatabase();

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        ZomatoSyncUtils.initialize(this);

    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showData() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private Cursor getAllFavoriteMovies() {
        return mDb.query(
                RestaurantTableContents.RestaurantEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        switch (id) {

            case LOADER_ID:
                /* URI for all rows of all data in our weather table */
                Uri forecastQueryUri = RestaurantTableContents.RestaurantEntry.CONTENT_URI;
                String sortOrder = "RANDOM() LIMIT 5";

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_RESTAURANT_PROJECTION,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        restaurantAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        int i = data.getCount();
        if(data.getCount() != 0)
            showData();
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        restaurantAdapter.swapCursor(null);
    }

    @Override
    public void onClick() {

    }
}
