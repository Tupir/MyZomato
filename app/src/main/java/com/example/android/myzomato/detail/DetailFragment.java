package com.example.android.myzomato.detail;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myzomato.R;
import com.example.android.myzomato.data.RestaurantTableContents;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_AVERAGE_COST;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_CUISINES;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_IMAGE;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_NAME;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_STREET;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.MAIN_RESTAURANT_PROJECTION;




public class DetailFragment extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{





    public DetailFragment(){
    }

    private static final int LOADER_ID = 5;
    private Toolbar toolbar;
    private int id;
    private Uri forecastQueryUri;
    private String intentActivity;

    @BindView(R.id.adress) TextView textAdress;
    @BindView(R.id.cuisines) TextView textCuisines;
    @BindView(R.id.price) TextView textPrice;
    @BindView(R.id.photo) ImageView imagePhote;
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

        if(intent.hasExtra("activity")){
            favorite_text.setText("Remove from favorites:");
            favorite_button.setImageResource(R.drawable.ic_delete_black_24dp);
        }else{
            favorite_text.setText("Add to favorites:");
            favorite_button.setImageResource(R.drawable.heee);
        }


        forecastQueryUri = RestaurantTableContents.RestaurantEntry.buildOneRestaurantUri(String.valueOf(id));


        getSupportLoaderManager().initLoader(LOADER_ID, null, this);



    }



    public void insertIntoDB(View view){


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
            textPrice.setText(data.getString(INDEX_COLUMN_AVERAGE_COST) + "€");
            collapsingToolbarLayout.setTitle(data.getString(INDEX_COLUMN_NAME));

            String photo = data.getString(INDEX_COLUMN_IMAGE);

            if(!photo.isEmpty()) {
                Picasso.with(this)
                        .load(data.getString(INDEX_COLUMN_IMAGE))
                        .into(imagePhote);
            }

        }


    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


}