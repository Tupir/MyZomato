package com.example.android.myzomato.all_restaurants;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.example.android.myzomato.R;
import com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry;
import com.example.android.myzomato.detail.DetailActivity;
import com.example.android.myzomato.sync.ZomatoSyncUtils;

public class AllRestaurantFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        AllRestaurantAdapter.ForecastAdapterOnClickHandler{


    public static final String[] MAIN_RESTAURANT_PROJECTION = {
            RestaurantEntry.COLUMN_ID,
            RestaurantEntry.COLUMN_NAME,
            RestaurantEntry.COLUMN_CUISINES,
            RestaurantEntry.COLUMN_AVERAGE_COST,
            RestaurantEntry.COLUMN_IMAGE,
            RestaurantEntry.COLUMN_STREET,
            RestaurantEntry.COLUMN_LATITUDE,
            RestaurantEntry.COLUMN_LONGITUDE,
            RestaurantEntry.COLUMN_RATING,
            RestaurantEntry.COLUMN_FAVORITE,
    };

    public static final int INDEX_COLUMN_ID = 0;
    public static final int INDEX_COLUMN_NAME = 1;
    public static final int INDEX_COLUMN_CUISINES = 2;
    public static final int INDEX_COLUMN_AVERAGE_COST = 3;
    public static final int INDEX_COLUMN_IMAGE = 4;
    public static final int INDEX_COLUMN_STREET = 5;
    public static final int INDEX_COLUMN_LATITUDE = 6;
    public static final int INDEX_COLUMN_LONGITUDE = 7;
    public static final int INDEX_COLUMN_RATING = 8;
    public static final int INDEX_COLUMN_FAVORITE = 9;



    public AllRestaurantFragment(){
    }

    private static final int LOADER_ID = 0;
    private static final int LOADER_ID2 = 33;
    private static final int LOADER_ID3 = 34;
    private AllRestaurantAdapter restaurantAdapter;
    private AllRestaurantAdapter restaurantAdapter2;
    private AllRestaurantAdapter restaurantAdapter3;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private RecyclerView mRecyclerView3;
    private ScrollView scrollView;
    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.all_restaurant_activity, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_forecast);
        mRecyclerView2 = (RecyclerView) rootView.findViewById(R.id.recyclerview_forecast2);
        mRecyclerView3 = (RecyclerView) rootView.findViewById(R.id.recyclerview_forecast3);
        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);
        scrollView = rootView.findViewById(R.id.scroll);
        scrollView.smoothScrollTo(0,0);
        showLoading();


        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView2.setLayoutManager(layoutManager2);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager3 =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView3.setLayoutManager(layoutManager3);
        mRecyclerView3.setHasFixedSize(true);
        mRecyclerView3.setNestedScrollingEnabled(false);

        restaurantAdapter = new AllRestaurantAdapter(getContext(), this);
        mRecyclerView.setAdapter(restaurantAdapter);

        restaurantAdapter2 = new AllRestaurantAdapter(getContext(), this);
        mRecyclerView2.setAdapter(restaurantAdapter2);

        restaurantAdapter3 = new AllRestaurantAdapter(getContext(), this);
        mRecyclerView3.setAdapter(restaurantAdapter3);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        getLoaderManager().initLoader(LOADER_ID2, null, this);
        getLoaderManager().initLoader(LOADER_ID3, null, this);
        ZomatoSyncUtils.initialize(getContext());

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID2, null, this).forceLoad();
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID3, null, this).forceLoad();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showData() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri forecastQueryUri = RestaurantEntry.CONTENT_URI;
        String sortOrder = "RANDOM() LIMIT 5";

        switch (id) {

            case LOADER_ID:
                /* URI for all rows of all data in our weather table */
                return new CursorLoader(getContext(),
                        forecastQueryUri,
                        MAIN_RESTAURANT_PROJECTION,
                        null,
                        null,
                        sortOrder);

            case LOADER_ID2:
                return new CursorLoader(getContext(),
                        forecastQueryUri,
                        MAIN_RESTAURANT_PROJECTION,
                        null,
                        null,
                        sortOrder);

            case LOADER_ID3:
                return new CursorLoader(getContext(),
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


        if(loader.getId() == LOADER_ID) {
            restaurantAdapter.swapCursor(data);
        }else if(loader.getId() == LOADER_ID2){
            restaurantAdapter2.swapCursor(data);
        }else {
            restaurantAdapter3.swapCursor(data);
        }



        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        int i = data.getCount();
        if(data.getCount() != 0)
            showData();
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        restaurantAdapter.swapCursor(null);
    }


    @Override
    public void onClick(int id) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
