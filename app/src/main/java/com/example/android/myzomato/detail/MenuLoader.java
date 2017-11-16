package com.example.android.myzomato.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.example.android.myzomato.R;
import com.example.android.myzomato.utils.NetworkUtils;
import com.example.android.myzomato.utils.RestaurantJsonParser;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;


/**
 * Created by PepovPC on 7/21/2017.
 */

// is ok to do it on Cursor Loader,but try to meanwhile save all other movie items into DB as SERVICE?
public class MenuLoader implements LoaderManager.LoaderCallbacks<List<List<String>>>{

    public MenuAdapter mAdapter;
    private Context context;
    private int rest_id;

    MenuLoader(Context context, MenuAdapter mAdapter, int id){
        this.context = context;
        this.mAdapter = mAdapter;
        this.rest_id = id;
    }



    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<List<String>>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<List<String>>>(context) {

            /* This Movie array will hold and help cache our movie data */
            List<List<String>> menus = null;


            @Override
            protected void onStartLoading() {
                if (menus != null) {
                    deliverResult(menus);
                } else {
                    //mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Override
            public List<List<String>> loadInBackground() {
                int op = rest_id;
                String val = Integer.toString(rest_id);
                URL weatherRequestUrl = NetworkUtils.buildUrlMenu(val);
                String jsonWeatherResponse = null;
                try{
                    jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(weatherRequestUrl);
                } catch(IOException e)

                {
                    e.printStackTrace();
                }

                System.out.println(jsonWeatherResponse);
                if(jsonWeatherResponse == null)
                    return null;

                try {
                    return RestaurantJsonParser.getMenuDataFromJson(jsonWeatherResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }


            public void deliverResult(List<List<String>> data) {
                menus = data;
                super.deliverResult(menus);
            }



        };
    }


    @Override
    public void onLoadFinished(Loader<List<List<String>>> loader, List<List<String>> data) {
        //MainActivity.mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(data == null) {
            Toast.makeText(context, R.string.no_today_menu,
                    Toast.LENGTH_SHORT).show();
        }else
            mAdapter.setMenuData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<List<String>>> loader) {

    }
}
