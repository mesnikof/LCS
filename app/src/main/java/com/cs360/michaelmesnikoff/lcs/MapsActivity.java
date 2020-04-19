package com.cs360.michaelmesnikoff.lcs;

/**
 * Created by mm on 3/20/2020.
 */

/**
 * Created by Vishal on 10/20/2018.
 */

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.content.Context;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.annotation.Nullable;

import android.Manifest;

import android.content.pm.PackageManager;

import android.view.View;
import android.widget.ImageButton;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.cs360.michaelmesnikoff.lcs.mapHelpers.FetchURL;
import com.cs360.michaelmesnikoff.lcs.mapHelpers.TaskLoadedCallback;

import java.util.ArrayList;

/*
 * This class contains all the elements of the Google Maps activities for the LCS app.
 *
 * Note: many of the comments are the pre-generated comments coming from Android Studio.
 */
public class MapsActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap myMap;
    private LatLng currentPos;
    private LatLng LCSflagship;
    private MarkerOptions markerOrig;
    private MarkerOptions markerDest;
    private Polyline routeToLCS;
    private PolylineOptions polyLineOptions;

    protected LocationManager locationManager;
    Location currentLocation;

    ImageButton getDriving;
    ImageButton getWalking;

    ArrayList<LatLng> pointsList;


    /*
     * The required empty constructor method.
     */
    public MapsActivity() {
    }

    /*
     * The onCreate method.  The real entry point into the class.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /*
         * An arraylist to hold the origin and destination map point locations.
         */
        pointsList = new ArrayList<>();

        /*
         * Create the references for the two "Get Directions" buttons.
         */
        getDriving = findViewById(R.id.buttonDriving);
        getWalking = findViewById(R.id.buttonWalking);

        /*
         * Set the OnClickListener callback methods for the two "Get Directions" buttons.
         */
        getDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(MapsActivity.this).execute(getUrl(markerOrig.getPosition(), markerDest.getPosition(), "driving"), "driving");
            }
        });

        getWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(MapsActivity.this).execute(getUrl(markerOrig.getPosition(), markerDest.getPosition(), "walking"), "walking");
            }
        });

        /*
         * Check for the required Location/Mapping permissions.  If they are not available request them.
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

        /*
         * Get the LastLocation for to set the current location.
         */
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        /*
         * Create a map marker for the present position of this device.
         *
         * Note: The icon is very large.  A better-sized one need to be found/created.
         */
        currentPos = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        markerOrig = new MarkerOptions()
                .position(currentPos)
                .title("You are here!")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.you_are_here_red))
                .anchor(0.0f, 1.0f)
                .flat(false);
        pointsList.add(currentPos);

        /*
         * Now create a marker for the LCS flagship location, and move the camera.
         * This just happens to be on the SNHU campus.
         *
         * Note: The icon is very large.  A better-sized one need to be found/created.
         */
        LCSflagship = new LatLng(43.0, -71.4);
        markerDest = new MarkerOptions()
                .position(LCSflagship)
                .title("LCS Flagship Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.lcs_location))
                .anchor(1.0f, 1.0f)
                .flat(false);
        pointsList.add(LCSflagship);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(MapsActivity.this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        //myMap.addMarker(markerOrig);
        myMap.addMarker(markerDest);

        /*
         * Now set the zoom level of the map to something usefull.
         */
        //float zoomLevel = 10.0f; // This goes up to 21
        float zoomLevel = 8.0f; // This goes up to 21

        /*
         * Finally, place the view over our current location.
         */
        myMap.animateCamera(CameraUpdateFactory.newLatLng(LCSflagship));
        //myMap.animateCamera(CameraUpdateFactory.newLatLng(currentPos));
        //myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 5));
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LCSflagship, zoomLevel));
        //myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, zoomLevel));
        //myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, zoomLevel));

        /*
         * Enable/display the Map Toolbar and Zoom controls.
         */
        myMap.getUiSettings().setMapToolbarEnabled(true);
        myMap.getUiSettings().setZoomControlsEnabled(true);

        /*
         * Check for permission, then enable the My Location button if permission is granted.
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myMap.setMyLocationEnabled(true);

        /*
         * The callback function for when a user "Long Clicks/Presses" a new location.
         */
        myMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                /*
                 * If there are already two points in the list, clear_icon them.
                 *
                 * Also, for normal operation, we would probably want to clear_icon all the markers points
                 * off of the map.  As such, we will do that here.  Then we will reload the LCS
                 * location marker, assuming that is where we want to get directions to.
                 */
                if (pointsList.size() == 2) {
                    pointsList.clear();
                    myMap.clear();
                }

                /*
                 * Create a new map marker point at the LatLng location being "LongClicked", then
                 * add it to the pointList ListArray as the 0th point (the start point) for
                 * directions.
                 */
                currentPos = latLng;
                markerOrig = new MarkerOptions()
                        .position(currentPos)
                        .title("You are here!")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.you_are_here_green))
                        .anchor(0.5f, 1.0f)
                        .flat(false);
                myMap.addMarker(markerOrig);
                pointsList.add(currentPos);

                /*
                 * Now add a marker for the LCS flagship location, and move the camera.
                 * This just happens to be on the SNHU campus.
                 *
                 * Note: The icon is very large.  A better-sized one need to be found/created.
                 */
                LCSflagship = new LatLng(43.0, -71.4);
                markerDest = new MarkerOptions()
                        .position(LCSflagship)
                        .title("LCS Flagship Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.lcs_location))
                        .anchor(1.0f, 1.0f)
                        .flat(false);
                myMap.addMarker(markerDest);
                pointsList.add(LCSflagship);
            }
        });
    }


    /*
     * This method builds and returns a URL string to send to the Google Maps/Directions API
     * for getting a route between the two points.
     */
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }


    /*
     * This method is called when the URL for directions has been generated and executed.
     * The returned data is used to create a polyline of directions on the map.
     */
    @Override
    public void onTaskDone(Object... values) {
        if (routeToLCS != null)
            routeToLCS.remove();
        routeToLCS = myMap.addPolyline((PolylineOptions) values[0]);
    }


    /*
     * These empty methods are required for implementation of the location listener.
     * They may be used for some purpose in the future,  For now, they are required to tie
     * up loose ends.
     */
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
