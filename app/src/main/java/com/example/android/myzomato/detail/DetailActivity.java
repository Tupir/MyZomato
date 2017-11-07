package com.example.android.myzomato.detail;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myzomato.R;
import com.example.android.myzomato.data.RestaurantDbHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_AVERAGE_COST;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_CUISINES;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_IMAGE;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_NAME;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_RATING;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_STREET;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.MAIN_RESTAURANT_PROJECTION;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{





    public DetailActivity(){
    }

    private static final int LOADER_ID = 5;
    private Toolbar toolbar;
    private int id;
    private Uri forecastQueryUri;
    private String intentActivity;
    private Boolean isInDatabaseAlready = false;

    @BindView(R.id.adress) TextView textAdress;
    @BindView(R.id.cuisines) TextView textCuisines;
    @BindView(R.id.price) TextView textPrice;
    @BindView(R.id.rating) TextView textRating;
    @BindView(R.id.photo) ImageView imagePhoto;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.add_favorite) ImageButton favorite_button;
    @BindView(R.id.textFavorite) TextView favorite_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Intent intent = getIntent();
        if(intent.hasExtra("id")){
            id = intent.getIntExtra("id", 0);
        }


        displayImage();


        forecastQueryUri = RestaurantEntry.buildOneRestaurantUri(String.valueOf(id));


        getSupportLoaderManager().initLoader(LOADER_ID, null, this);



    }


    public void displayImage(){

        /**
         * Checking if movie is in database already
         * I am working here with getContentResolver, this method calls (query,update,delete,insert)
         *  functions that are implemented in Provider
         * getContentResolver calls code by URIs
         */
        String select = "((" + RestaurantEntry.COLUMN_ID + " =" + id + ") AND ("
                        + RestaurantEntry.COLUMN_FAVORITE + "=1))";


        Cursor cursor = this.getContentResolver().query(RestaurantEntry.CONTENT_URI,
                null,
                select,
                null,
                null);

        if(cursor.moveToFirst()) {
            favorite_text.setText("Remove from favorites:");
            favorite_button.setImageResource(R.drawable.ic_delete_black_24dp);
            isInDatabaseAlready = true;
        }else{
            favorite_text.setText("Add to favorites:");
            favorite_button.setImageResource(R.drawable.heee);
        }



    }



    public void insertIntoDB(View view){

        String backgroundImageName = String.valueOf(favorite_button.getTag());
        System.out.println(backgroundImageName);
        Drawable obr = favorite_button.getDrawable();


        String input;
        if(!isInDatabaseAlready) {
            input = "1";
        }else{
            input="0";
        }
        SQLiteDatabase mDb;
        RestaurantDbHelper dbHelper = RestaurantDbHelper.getInstance(this);
        mDb = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("favorite", input); //These Fields should be your String values of actual column names
        mDb.update(RestaurantEntry.TABLE_NAME, cv, RestaurantEntry.COLUMN_ID + " = " + Integer.toString(id), null);
        displayImage();

    }



    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {


        switch (loaderId) {

            case LOADER_ID:

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_RESTAURANT_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.moveToFirst()) {
            textAdress.setText(data.getString(INDEX_COLUMN_STREET));
            textCuisines.setText(data.getString(INDEX_COLUMN_CUISINES));
            textRating.setText(data.getString(INDEX_COLUMN_RATING));
            textPrice.setText(data.getString(INDEX_COLUMN_AVERAGE_COST) + "€");
            collapsingToolbarLayout.setTitle(data.getString(INDEX_COLUMN_NAME));

            String photo = data.getString(INDEX_COLUMN_IMAGE);

            if(!photo.isEmpty()) {
                Picasso.with(this)
                        .load(data.getString(INDEX_COLUMN_IMAGE))
                        .into(imagePhoto);
            }

        }


    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}