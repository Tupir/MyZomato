/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.myzomato.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.myzomato.Utils.NetworkUtils;
import com.example.android.myzomato.Utils.RestaurantJsonParser;
import com.example.android.myzomato.data.RestaurantTableContents;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class ZomatoSyncTask {


    /**
     * Performs the network request for updated weather, parses the JSON from that request, and
     * inserts the new weather information into our ContentProvider. Will notify the user that new
     * weather has been loaded if the user hasn't been notified of the weather within the last day
     * AND they haven't disabled notifications in the preferences screen.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncRestaurant(Context context) throws IOException, JSONException {

        URL receptRequestUrl2 = NetworkUtils.buildUrl("https://developers.zomato.com/api/v2.1/search?entity_id=219&entity_type=city&start=0");
        int seq = 0;
        ContentValues[] vals2 = new ContentValues[0];
        ContentResolver sunshineContentResolver = context.getContentResolver();

        sunshineContentResolver.delete(
                RestaurantTableContents.RestaurantEntry.CONTENT_URI,
                null,
                null);

        getNumberOfResults(receptRequestUrl2);
        int forSequence = RestaurantJsonParser.number_of_results / 20 + 1;

        for(int i=0; i<forSequence; i++){

            String jsonRestaurantResponse = NetworkUtils
                    .getResponseFromHttpUrl(receptRequestUrl2);

                ContentValues[] vals = RestaurantJsonParser.getRestaurantDataFromJson(jsonRestaurantResponse);
                vals2 = concatArrays(vals, vals2);
            receptRequestUrl2 = NetworkUtils.buildUrl("https://developers.zomato.com/api/v2.1/search?entity_id=219&entity_type=city&start="+seq);
            seq += 20;
    }

        sunshineContentResolver.bulkInsert(
                RestaurantTableContents.RestaurantEntry.CONTENT_URI,
                vals2);

    }

    // just to get nuber of results (120 in our case)
    public static void getNumberOfResults(URL receptRequestUrl2) throws IOException, JSONException {
        RestaurantJsonParser.getRestaurantDataFromJson(NetworkUtils
                .getResponseFromHttpUrl(receptRequestUrl2));
    }



    public static ContentValues[] concatArrays(ContentValues[] one, ContentValues[] two){
        ContentValues[] array1and2 = new ContentValues[one.length + two.length];
        System.arraycopy(one, 0, array1and2, 0, one.length);
        System.arraycopy(two, 0, array1and2, one.length, two.length);
        return array1and2;

    }



}