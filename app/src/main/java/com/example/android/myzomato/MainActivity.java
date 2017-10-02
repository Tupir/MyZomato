package com.example.android.myzomato;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.example.android.myzomato.Utils.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private static final int FORECAST_LOADER_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(FORECAST_LOADER_ID, null, this).forceLoad();

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String recepts = null;

            @Override
            protected void onStartLoading() {
                System.out.println("zacalo");
            }


            @Override
            public String loadInBackground() {


                URL receptRequestUrl = NetworkUtils.buildUrl();

                try {
                    String jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(receptRequestUrl);


                    System.out.println(jsonWeatherResponse);
                    return jsonWeatherResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }


        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
