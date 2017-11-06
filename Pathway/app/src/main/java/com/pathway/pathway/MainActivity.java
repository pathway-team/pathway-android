package com.pathway.pathway;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.BottomSheetDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback {


    private GoogleApiClient mapApiClient;
    private GoogleMap mMap;
    private LocationRequest locRequest;
    private LatLng lastLoc;
    private Polyline userRoute;
    private JSONObject dataRoute;
    private Button btnStart;
    private long startTime = 0;
    private Chronometer timerRoute;


    private enum RunStates {OFF, RUN, PAUSE};


    private RunStates runState = RunStates.OFF;
    private String coordMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, coordMsg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnStart = (Button) findViewById(R.id.btn_Start);
        btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStartPressed(v);
                    }
        });

        timerRoute = (Chronometer) findViewById(R.id.chronometer);
        timerRoute.setFormat("%s");

        if (mapApiClient == null) {
            mapApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

/*        locRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000)
                .setFastestInterval(1000);*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_routes_map) {
            // handle the routes map fragment
        } else if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        mMap = googleMap;
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

/*        PolylineOptions routeOptions = new PolylineOptions()
                .color(Color.RED)
                .width(12)
                .startCap(new RoundCap())
                .endCap(new RoundCap());
        userRoute = googleMap.addPolyline(routeOptions);*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setMyLocationEnabled(true);

    }


    //===============Map Functions===================
    @Override
    public void onLocationChanged(Location location) {
        lastLoc = new LatLng(location.getLatitude(), location.getLongitude());
        if (userRoute != null) {
            List<LatLng> points = userRoute.getPoints();
            points.add(lastLoc);
            userRoute.setPoints(points);
        }
        coordMsg = String.format("XY: %s.\nElev: %s", lastLoc.toString());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(lastLoc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, 16));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        //stopLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        mapApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mapApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest()
                .setInterval(3000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mapApiClient, locationRequest, this);

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mapApiClient, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do something
                } else {
                    finish();
                }
                break;
            }
        }
    }


    public void onStartPressed(View v) {
        if (runState == RunStates.OFF) {

            btnStart.setText("Stop");
            Snackbar.make(v, "Recording...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            runState = RunStates.RUN;
            PolylineOptions routeOptions = new PolylineOptions()
                    .color(Color.RED)
                    .width(12)
                    .startCap(new RoundCap())
                    .endCap(new RoundCap());
            timerRoute.setBase(SystemClock.elapsedRealtime());
            timerRoute.start();
            userRoute = mMap.addPolyline(routeOptions);
            startLocationUpdates();
        }
        else if(runState == RunStates.RUN) {
            btnStart.setText("Start");
            Snackbar.make(v, "Recording Stopped.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            runState = RunStates.OFF;
            userRoute.remove();
            userRoute = null;
            timerRoute.stop();
            stopLocationUpdates();
        }
    }
    public void onStopPressed() {

    }
}