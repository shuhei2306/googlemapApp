package com.example.shuhei.googlemap;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public GoogleMap mMap2;

    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private LatLng mKansai = new LatLng(34.435912, 135.243496);
    private LatLng mItami = new LatLng(34.785500, 135.438004);
    private LatLng hankyurokkou = new LatLng(34.71985,135.234388);

    private GoogleApiClient mLocationClient = null;
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)
            .setFastestInterval(16)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mLocationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if(mLocationClient != null){
            Log.d("testdesu", "d");

            mLocationClient.connect();
            Log.d("testdesu", "e");
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        //thread
      //  (new Thread(new Task())).start();


    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {

       // Log.d("testdesu", "1010");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }

        if (mMap2 == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap2 = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap2 != null) {
                setUpMap2();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
       // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        LatLng latLng = new LatLng(35.684699, 139.753897);
        float zoom = 13; // 2.0～21.0
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


    }
    private void setUpMap2() {
       // mMap2.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        // 初期座標、拡大率設定
        LatLng latLng = new LatLng(35.684699, 139.753897);
        float zoom = 13; // 2.0～21.0
        mMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        // マーカー設置
        //mMap2.addMarker(new MarkerOptions().position(latLng).title("皇居"));
    }

    public void plus(View v) {
        LatLng latLng = new LatLng(35.684699, 139.753897);
        mMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));

    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("testdesu", "coonect");
        fusedLocationProviderApi.requestLocationUpdates(mLocationClient, REQUEST, (com.google.android.gms.location.LocationListener) this);
        Log.d("testdesu", "connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("testdesu", "a");

        CameraPosition cameraPos = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(17.0f)
                .bearing(0).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));

        //マーカー設定
        MarkerOptions options = new MarkerOptions();
        Intent intent = getIntent();
        String selectedA = intent.getStringExtra("SELECTED_ITEM");
        if(selectedA !=null && selectedA.equals("関西国際空港")){
            options.position(mKansai);
            mMap.addMarker(options);
        }else if(selectedA !=null && selectedA.equals("伊丹空港")){
            options.position(mItami);
            mMap.addMarker(options);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}


class Task implements Runnable {


    @Override
    public void run() {
        // Default setting
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);

        while (true) {
            Log.d("testdesu", "1000");
           sleep(1000);

        }


    }

    public synchronized void sleep(long msec)
    {
        try
        {
            wait(msec);
        }catch(InterruptedException e){}
    }
}


