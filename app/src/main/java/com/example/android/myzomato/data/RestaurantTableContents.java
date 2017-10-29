package com.example.android.myzomato.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by PepovPC on 8/6/2017.
 */

public class RestaurantTableContents {

    /*
 * The "Content authority" is a name for the entire content provider, similar to the
 * relationship between a domain name and its website. A convenient string to use for the
 * content authority is the package name for the app, which is guaranteed to be unique on the
 * Play Store.
 */
    public static final String CONTENT_AUTHORITY = "com.example.android.myzomato";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RESTAURANT = "restaurant";

    /* Inner class that defines the table contents of the movie table */
    public static final class RestaurantEntry implements BaseColumns {


        /* The base CONTENT_URI used to query the Restaurant table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RESTAURANT)
                .build();


        /* Used internally as the name of our restaurant table. */
        public static final String TABLE_NAME = "restaurants";

        public static final String COLUMN_ID = "id";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_CUISINES = "cuisines";

        public static final String COLUMN_AVERAGE_COST = "average_cost";

        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_STREET = "locality_verbose";

        public static final String COLUMN_LATITUDE = "latitude";

        public static final String COLUMN_LONGITUDE = "longitude";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_FAVORITE = "favorite";

        public static Uri buildOneRestaurantUri(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
        


    }



}
