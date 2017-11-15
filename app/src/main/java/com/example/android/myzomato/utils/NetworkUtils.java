package com.example.android.myzomato.utils;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Create network connection and retrieve data
 *
 */

// make it final to prevent subclassing
public final class NetworkUtils {


    // it avoids access from outside (reflection)
    private NetworkUtils() {
        throw new AssertionError("No instances for you!");
    }

    /**
     * Builds the URL.
     */
    public static URL buildUrl(String stringUrl) {


        Uri builtUri = Uri.parse(stringUrl).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e("NetworkUtils", e.getMessage(), e);
        }

        System.out.println(url.toString());
        return url;
    }

    public static URL buildUrlMenu(String id) {

        String menuUrl = "https://developers.zomato.com/api/v2.1/dailymenu?res_id=";

        menuUrl = menuUrl + id;

        Uri builtUri = Uri.parse(menuUrl).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e("NetworkUtils", e.getMessage(), e);
        }

        System.out.println(url.toString());
        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     * Vrati response zo stranky (cely JSON)
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String user_key = "ce3ebff8fc6db1d6714583670a7d4434";
        urlConnection.setRequestProperty("user-key", user_key); // added header
        try {
            urlConnection.setConnectTimeout(5000);  // if there are some connection problems hold on 5 seconds
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}