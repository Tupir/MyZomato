package com.example.android.myzomato.favorite_restaurants;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.android.myzomato.R;
import com.example.android.myzomato.detail.DetailActivity;

import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.MAIN_RESTAURANT_PROJECTION;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry;

public class FavoriteRestaurantFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        FavoriteRestaurantAdapter.ForecastAdapterOnClickHandler{




    public FavoriteRestaurantFragment(){
    }

    private static final int LOADER_ID = 33;
    private FavoriteRestaurantAdapter restaurantAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private SQLiteDatabase mDb;

    private ProgressBar mLoadingIndicator;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


//    @Override
//    public void onResume(){
//        super.onResume();
//        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.favorite_restaurant_activity, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_forecast);
        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);

        showLoading();

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        restaurantAdapter = new FavoriteRestaurantAdapter(getContext(), this);
        mRecyclerView.setAdapter(restaurantAdapter);


        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        return rootView;
    }


    @Override
    public void onResume(){
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
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


        switch (id) {

            case LOADER_ID:
                /* URI for all rows of all data in our weather table */
                Uri forecastQueryUri = RestaurantEntry.CONTENT_URI;

                String select = "("+ RestaurantEntry.COLUMN_FAVORITE + "=1)";
                //String select = "("+ RestaurantEntry.COLUMN_ID + "=16514463)";

                return new CursorLoader(getContext(),
                        forecastQueryUri,
                        MAIN_RESTAURANT_PROJECTION,
                        select,
                        null,
                        null);


            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        restaurantAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
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
        intent.putExtra("activity",  this.getClass().getSimpleName());
        startActivity(intent);

    }
}
