package com.example.android.myzomato.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.myzomato.R;
import com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry;
import com.example.android.myzomato.detail.DetailActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_ID;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_LATITUDE;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_LONGITUDE;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.INDEX_COLUMN_NAME;
import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.MAIN_RESTAURANT_PROJECTION;

public class MapsActivity extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, LoaderManager.LoaderCallbacks<Cursor>{

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOADER_ID = 323;
    private double lat,lon;
    private boolean isInfoWindowShown = false;


    public static MapsActivity newInstance() {
        MapsActivity fragment = new MapsActivity();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, null, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker == mCurrLocationMarker)
                    return;
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id", (int) marker.getTag());
                startActivity(intent);
            }
        });


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        lat = location.getLatitude();
        lon = location.getLongitude();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your position!");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    protected Marker createMarker(double latitude, double longitude, String title, int id) {

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title));

        marker.setTag(id);

        return marker;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case LOADER_ID:
                /* URI for all rows of all data in our weather table */
                Uri forecastQueryUri = RestaurantEntry.CONTENT_URI;
                System.out.println("POZICIA: "+ lat + " " + lon);
                String sordOrder = "(("+lat+" - "+RestaurantEntry.COLUMN_LATITUDE+")*("+lat+" - "+RestaurantEntry.COLUMN_LATITUDE+""
                        +")) + (("+lon+" - "+RestaurantEntry.COLUMN_LONGITUDE+")*("+lon+" - "+RestaurantEntry.COLUMN_LONGITUDE +")) ASC LIMIT 15";


                return new CursorLoader(getContext(),
                        forecastQueryUri,
                        MAIN_RESTAURANT_PROJECTION,
                        null,
                        null,
                        sordOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();

        for(int i = 0 ; i < 15 ; i++ ) {
            String name = (data.getString(INDEX_COLUMN_NAME));
            double lat = Double.parseDouble(data.getString(INDEX_COLUMN_LATITUDE));
            double longitude = Double.parseDouble(data.getString(INDEX_COLUMN_LONGITUDE));
            int id = data.getInt(INDEX_COLUMN_ID);
            createMarker(lat, longitude, name, id);
            data.moveToNext();
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}