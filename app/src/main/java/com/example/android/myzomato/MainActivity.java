package com.example.android.myzomato;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.myzomato.Utils.NetworkUtils;
import com.example.android.myzomato.data.RestaurantDbHelper;
import com.example.android.myzomato.data.RestaurantTableContents;
import com.example.android.myzomato.sync.ZomatoSyncUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        RestaurantAdapter.ForecastAdapterOnClickHandler {


    public static final String[] MAIN_RESTAURANT_PROJECTION = {
            RestaurantTableContents.RestaurantEntry.COLUMN_ID,
            RestaurantTableContents.RestaurantEntry.COLUMN_NAME,
            RestaurantTableContents.RestaurantEntry.COLUMN_IMAGE,
    };

    public static final int INDEX_COLUMN_ID = 0;
    public static final int INDEX_COLUMN_NAME = 1;
    public static final int INDEX_COLUMN_IMAGE = 2;



    private static final int FORECAST_LOADER_ID = 0;
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
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        restaurantAdapter = new RestaurantAdapter(this, this);
        mRecyclerView.setAdapter(restaurantAdapter);



        RestaurantDbHelper dbHelper = RestaurantDbHelper.getInstance(this);
        mDb = dbHelper.getWritableDatabase();


        getSupportLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);
        ZomatoSyncUtils.initialize(this);

    }

    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
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
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String recepts = null;
            String jsonRestaurantResponse;
            @Override
            protected void onStartLoading() {
                showLoading();
                System.out.println("zacalo");
            }


            @Override
            public String loadInBackground() {


                URL receptRequestUrl = NetworkUtils.buildUrl("https://developers.zomato.com/api/v2.1/search?entity_id=219&entity_type=city");
                URL receptRequestUrl2 = NetworkUtils.buildUrl("https://developers.zomato.com/api/v2.1/search?entity_id=219&entity_type=city&start=20");

//                try {
//                    jsonRestaurantResponse = NetworkUtils
//                            .getResponseFromHttpUrl(receptRequestUrl);
//
//                    String jsonRestaurantResponse2 = NetworkUtils
//                            .getResponseFromHttpUrl(receptRequestUrl2);
//
//                    ContentValues[] vals = RestaurantJsonParser.getRestaurantDataFromJson(jsonRestaurantResponse);
//                    int jop = vals.length;
//                    ContentValues[] vals2 = RestaurantJsonParser.getRestaurantDataFromJson(jsonRestaurantResponse2);
//                    jop = vals.length;
//                    ContentValues[] array1and2 = concatArrays(vals, vals2);
//
//
//                    if (vals != null && vals.length != 0) {
//                        ContentResolver sunshineContentResolver = getContext().getContentResolver();
//
//                        sunshineContentResolver.delete(
//                                RestaurantTableContents.RestaurantEntry.CONTENT_URI,
//                                null,
//                                null);
//
//                        sunshineContentResolver.bulkInsert(
//                                RestaurantTableContents.RestaurantEntry.CONTENT_URI,
//                                array1and2);
//                    }
//
//
//
//                    System.out.println(jsonRestaurantResponse);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }

                return jsonRestaurantResponse;
            }


        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        Cursor cursor = getAllFavoriteMovies();
        int ops = cursor.getCount();
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public ContentValues[] concatArrays(ContentValues[] one, ContentValues[] two){
        ContentValues[] array1and2 = new ContentValues[one.length + two.length];
        System.arraycopy(one, 0, array1and2, 0, one.length);
        System.arraycopy(two, 0, array1and2, one.length, two.length);
        return array1and2;

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onClick(long date) {

    }
}
