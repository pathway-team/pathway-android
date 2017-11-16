package com.pathway.pathway;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetBehavior;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnPolylineClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        FetchData.FetchDataCallbackInterface,
        SendData.SendDataCallbackInterface{


    private GoogleApiClient mapApiClient;
    private GoogleMap mMap;
    private LocationRequest locRequest;
    private LatLng prevLoc;
    private LatLng lastLoc;
    private Polyline userRoute;
    private Route currentRoute;
    private Double polyDist = 0.0;
    private List<Polyline> areaRoutes = new ArrayList<>();
    private List<Route> nearbyRoutes = new ArrayList<>();
    private String ntwkData;
    private Button btnStart;
    private Chronometer timerRoute;
    private enum RunStates {OFF, RUN, PAUSE}
    private RunStates runState = RunStates.OFF;
    private String coordMsg;
    private DeviceDBHandler dbHandler = new DeviceDBHandler(this);

    @Override
    public void fetchDataCallback(String result) {
        this.ntwkData = result;


        JSONArray routesList = new JSONArray();
        if (nearbyRoutes != null) {
            nearbyRoutes.clear();
        }
        if (areaRoutes != null) {
            for (Polyline pl : areaRoutes) {
                pl.remove();
            }
            areaRoutes.clear();
        }
        try {
            routesList = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < routesList.length(); i++) {
            try {
                JSONObject tempJson = new JSONObject(routesList.getString(i));
                if (tempJson.get("data") != "{}") {
                    Route tempRoute = new Route(tempJson.getString("data").replace("activity", "atype"));
                    PolylineOptions routeOptions = new PolylineOptions()
                            .color(Color.BLUE)
                            .width(16)
                            .startCap(new RoundCap())
                            .endCap(new RoundCap())
                            .clickable(true);

                    Polyline oldRoute = mMap.addPolyline(routeOptions);
                    oldRoute.setTag(tempRoute);
                    oldRoute.setPoints(tempRoute.getDrawPoints());
                    areaRoutes.add(oldRoute);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendDataCallback(Integer result) {
        int test = result;
        switch (test) {
            case (201):
                Toast.makeText(this, "Route successfully saved.", Toast.LENGTH_SHORT).show();
                break;
            case (400):
                Toast.makeText(this, "Route not saved to external.\nRequest not understood.",
                        Toast.LENGTH_SHORT).show();
                        break;
            case(403):
                Toast.makeText(this, "Route not saved.\nMake sure you are signed in.",
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        //Toast.makeText(this, polyline.getTag().toString(), Toast.LENGTH_LONG).show();

        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.route_info_window, null);

        final TextView routeName = (TextView) popupView.findViewById(R.id.nameField);
        final TextView routeDist = (TextView) popupView.findViewById(R.id.distField);
        final TextView routeType = (TextView) popupView.findViewById(R.id.actField);
        final TextView routeDiff = (TextView) popupView.findViewById(R.id.diffField);


        try {
            Route temp = new Route(polyline.getTag().toString());
            routeName.setText(temp.getName());
            routeDist.setText(temp.getDistance().toString());
            routeType.setText(temp.getActivity());
            routeDiff.setText(temp.getDifficulty());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);

        Button btnStExist = (Button) popupView.findViewById(R.id.existStart);
        btnStExist.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                popupWindow.dismiss();

            }
        });
            popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dbHandler = new DeviceDBHandler(getApplicationContext());

        //clears tables
        //dbHandler.onUpgrade(dbHandler.getWritableDatabase(),0,0);

        //only call this here!!!
        dbHandler.createTables();

      
        //Start Test Area for jebragg
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String jsonString = "{" +
                            "\"type\": \"LineString\"," +
                            "\"bbox\": [-79.811818, 36.065488, -79.811308, 36.067061]," +
                            "\"coordinates\": [" +
                            "	[-79.811818, 36.065488, 250.8]," +
                            "	[-79.811646, 36.065553, 251.1]," +
                            "	[-79.811601, 36.065565, 250.8]" +
                            " ]," +
                            "\"timestamps\": [0, 3, 5]," +
                            "\"distance\": 5280.0," +
                            "\"rid\": 2," +
                            "\"pid\": 1," +
                            "\"name\": \"Bus Stop\"," +
                            "\"diffRtng\": \"A-1\"," +
                            "\"atype\": \"walk\"" +
                            "}";

                    Route source = null;

                    try {
                        source = new Route(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //JSONObject testJson = new JSONObject();


                    String testURL = getString(R.string.routesURL);
                    //double elevation = getElev(lastLoc);
                    new FetchData(testURL, MainActivity.this).execute();
                    source.setName("Test Route");
                    source.setActivity("W");
                    source.buildJSON();
                    //dbHandler.addNewRoute(source);
                    List<String> test = dbHandler.getUserRoutes();
                    String dbTest = dbHandler.getLastRoute();
                    int breakpoint = 0;
                    try {
                        source = new Route(dbHandler.getLastRoute());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Intent myIntent = new Intent(MainActivity.this, BasicReportView.class);
                    //Bundle bundle = new Bundle();
                    //bundle.putInt("pid", source.getPID());
                    //bundle.putInt("rid", source.getRID());
                    //myIntent.putExtras(bundle);
                    //startActivity(myIntent);

                    //new SendData(testURL, source, MainActivity.this).execute();
                    //Snackbar.make(view, ntwkData, Snackbar.LENGTH_LONG)
                    //        .setAction("Action", null).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //End test area for jebragg

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
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if(id == R.id.nav_logout && isLogin.islogin == true) {
            isLogin.islogin = false;
            LoginActivity.bundle.remove("username");
            LoginActivity.bundle.remove("password");
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_login && isLogin.islogin == false) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            //activityLink = (TextView) findViewById(R.id.tvRegisterHere);
        } else if (id == R.id.nav_routes && isLogin.islogin == true){
            Intent myIntent = new Intent(this, RoutePage.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_user && isLogin.islogin == true){
            Intent myIntent = new Intent(this, UserPage.class);
            startActivity(myIntent);
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
        LatLng startLoc = new LatLng(39.8283, -98.5795);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //googleMap.addMarker(new MarkerOptions().position(sydney)
        //        .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(startLoc));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, 3));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnPolylineClickListener(this);
        googleMap.setMyLocationEnabled(true);

    }


    //===============Map Functions===================
    @Override
    public void onLocationChanged(Location location) {

        lastLoc = new LatLng(location.getLatitude(), location.getLongitude());
        if (prevLoc == null) {
            prevLoc = lastLoc;
            double dist = 2275.95045447; //1609.34m (or 1 mile) * sqrt(2). Finds distance of sw and ne corners.
            LatLng swCnr = SphericalUtil.computeOffset(lastLoc, dist, 225.0);
            LatLng neCnr = SphericalUtil.computeOffset(lastLoc, dist, 45.0);
            LatLngBounds bBox = new LatLngBounds(swCnr, neCnr);
            new FetchData(getString(R.string.routesURL), bBox, MainActivity.this).execute();
        }
        if (SphericalUtil.computeDistanceBetween(prevLoc, lastLoc) > 500) {
            prevLoc = lastLoc;
            double dist = 2275.95045447; //1609.34m (or 1 mile) * sqrt(2). Finds distance of sw and ne corners.
            LatLng swCnr = SphericalUtil.computeOffset(lastLoc, dist, 225.0);
            LatLng neCnr = SphericalUtil.computeOffset(lastLoc, dist, 45.0);
            LatLngBounds bBox = new LatLngBounds(swCnr, neCnr);
            new FetchData(getString(R.string.routesURL), bBox, MainActivity.this).execute();
        }

        //double elevation = this.getElev(location);
        if (userRoute != null) {
            try {
                double altitude = location.getAltitude();
                List<LatLng> points = userRoute.getPoints();
                points.add(lastLoc);
                currentRoute.addCoords(lastLoc, altitude);
                currentRoute.addTime((int) (SystemClock.elapsedRealtime() - timerRoute.getBase()) / 1000);
                currentRoute.calcBBox();
                polyDist += SphericalUtil.computeDistanceBetween(lastLoc, prevLoc);
                currentRoute.setDistance(polyDist);
                currentRoute.buildJSON();
                userRoute.setPoints(points);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

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
                .setInterval(5000)
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
        if (isLogin.islogin == false) {
            Toast.makeText(this, "No User Logged in.\nPlease login before using Pathway.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (runState == RunStates.OFF) {

            try {
                currentRoute = new Route();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            btnStart.setText("Stop");
            Snackbar.make(v, "Recording...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            runState = RunStates.RUN;
            PolylineOptions routeOptions = new PolylineOptions()
                    .color(Color.RED)
                    .width(12)
                    .startCap(new RoundCap())
                    .endCap(new RoundCap())
                    .zIndex((2.0f));
            timerRoute.setBase(SystemClock.elapsedRealtime());
            timerRoute.start();
            userRoute = mMap.addPolyline(routeOptions);
            //startLocationUpdates();
        }
        else if(runState == RunStates.RUN) {

            btnStart.setText("Start");
            Snackbar.make(v, "Recording Stopped.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            LayoutInflater layoutInflater
                    = (LayoutInflater) getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.route_save_window, null);

            final EditText routeName = (EditText) popupView.findViewById(R.id.editRtName);
            final Spinner dropdown = (Spinner) popupView.findViewById(R.id.actDropDown);
            final CheckBox exporter = (CheckBox) popupView.findViewById(R.id.checkExport);

            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,true);

            Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String rtName = routeName.getText().toString();
                    String actType = dropdown.getSelectedItem().toString().substring(0,1);

                    currentRoute.setName(rtName);
                    currentRoute.setActivity(actType);
                    currentRoute.buildJSON();
                    dbHandler.addNewRoute(currentRoute);
                    
                    try {
                        currentRoute = new Route(dbHandler.getLastRoute());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new SendData(getString(R.string.routesURL), currentRoute, MainActivity.this).execute();

                    popupWindow.dismiss();

                    Intent myIntent = new Intent(MainActivity.this, BasicReportView.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("pid", currentRoute.getPID());
                    bundle.putInt("rid", currentRoute.getRID());
                    myIntent.putExtras(bundle);
                    startActivity(myIntent);
                }
            });
            if (currentRoute.getCoordinates().size() > 1) {
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }

            runState = RunStates.OFF;
            polyDist = 0.0;
            userRoute.remove();
            userRoute = null;
            timerRoute.stop();
            //stopLocationUpdates();
        }
    }
    public void onStopPressed() {

    }


    public double getElev(LatLng point) {
        double result = -100000.0;

        String path = "http://maps.googleapis.com/maps/api/elevation/" + "json?locations="
                + String.valueOf(point.latitude) + "," + String.valueOf(point.longitude) + "&sensor=true";
        try {
            URL url = new URL(String.format(path));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String tmp;
            while ((tmp = reader.readLine()) != null) {
                sb.append(tmp);
            }

            JSONArray feature = new JSONArray(sb.toString());
            result = Double.parseDouble(feature.getJSONObject(0).getString("elevation"));

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}