package com.doronkakuli.gps_tracking_example.activities;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.maps.model.PolylineOptions;
import com.doronkakuli.gps_tracking_example.GRApplication;
import com.doronkakuli.gps_tracking_example.gr_doron_kakuli.R;
import com.doronkakuli.gps_tracking_example.data.DataManager;
import com.doronkakuli.gps_tracking_example.utils.Utils;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback, View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int INTERVAL = 10000;
    public static final int FASTEST_INTERVAL = 5000;
    public static final int MAP_ZOOM = 17;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private DataManager mDataManager = DataManager.getInstance();
    //upper linear params
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected TextView mLatUpdatedTV;
    protected TextView mSpeedTV;

    LocationRequest mLocationRequest;
    private GoogleMap googleMap;
    private boolean mRequestingLocationUpdates = true;
    SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
    String mLastUpdated;

    Button mEnableBtn;
    Button mDisableBtn;

    Location mNextGeoPoint;
    //Time calculation
    long mStarTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_places_fragment)).getMapAsync(this);

        mLatitudeText = (TextView) findViewById(R.id.lat);
        mLongitudeText = (TextView) findViewById(R.id.longitude);
        mLatUpdatedTV = (TextView) findViewById(R.id.last_updated);
        mSpeedTV = (TextView) findViewById(R.id.speed);
        mEnableBtn = (Button) findViewById(R.id.enable_track_btn);
        mEnableBtn.setOnClickListener(this);
        mDisableBtn = (Button) findViewById(R.id.disable_track_btn);
        mDisableBtn.setOnClickListener(this);
        buildGoogleApiClient();
        createLocationRequest();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* ****************************************************************************************** */
    /* LOCATION METHODS                                                                                  */
    /* ****************************************************************************************** */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }



    public synchronized void drawOnMap(final Location location) {
// Get back the mutable Polyline
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
                mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
                mLatUpdatedTV.setText(mLastUpdated);
                PolylineOptions rectOptions = new PolylineOptions();
                rectOptions.add(new LatLng(mNextGeoPoint.getLatitude(), mNextGeoPoint.getLongitude()));
                mNextGeoPoint =mDataManager.getQueue().poll();
                rectOptions.add(new LatLng(mNextGeoPoint.getLatitude(), mNextGeoPoint.getLongitude()));
                googleMap.addPolyline(rectOptions);
            }
        });
    }

    /* ****************************************************************************************** */
    /* CALLBACKS                                                                                  */
    /* ****************************************************************************************** */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        mNextGeoPoint =mLastLocation;
        if (mLastLocation != null && googleMap != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), MAP_ZOOM));
                    mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
                    mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
                    mSpeedTV.setText(String.valueOf(mLastLocation.getSpeed()));
                }
            });
        }
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mLastUpdated = sdf.format(new Date());
        mDataManager.addLocation(location);
        drawOnMap(location);
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    //OnMapReadyCallback callback
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (mLastLocation != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
        }
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enable_track_btn:
                mStarTime= System.currentTimeMillis();
                mEnableBtn.setEnabled(false);
                mDisableBtn.setEnabled(true);
                mGoogleApiClient.connect();
                mRequestingLocationUpdates=true;
                break;
            case R.id.disable_track_btn:
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                    mRequestingLocationUpdates=false;
                    mEnableBtn.setEnabled(true);
                    mDisableBtn.setEnabled(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            googleMap.clear();
                        }
                    });
                    mDataManager.initializeLocations();
                }
                long time=System.currentTimeMillis()-mStarTime;
                Toast.makeText(GRApplication.getAppContext(), "Timer: "+Utils.convertMilisToTime(time),Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
