package be.ecam.mapeza.mapeza;

/**
 * Created by 12375 on 25-04-17.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


import java.util.ArrayList;

import static be.ecam.mapeza.mapeza.loadPlaces.getPlaces;
/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivityCurrentPlace extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<ArrayList<Place>>{

    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    // A default location Brussels and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(50.85045, 4.34878);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private double MyCurrentLat;
    private double MyCurrentLong;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private ArrayList<Place> places;
    private Context MapsActivityCurrentPlace = this;

    private static final int QUERY_LOADER = 22;
    Bundle queryURL = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public Loader<ArrayList<Place>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Place>>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public ArrayList<Place> loadInBackground(){
                Gson gson = new Gson();
                SharedPreferences mPrefs;
                mPrefs = PreferenceManager.getDefaultSharedPreferences(MapsActivityCurrentPlace.this);
                String json = mPrefs.getString("favoriteSelectedTypePlaceList", "");
                ArrayList<String> favoriteSelectedTypePlaceList = gson.fromJson(json, ArrayList.class);
                String[] myPlaceName = null;
                if(favoriteSelectedTypePlaceList == null){
                    // default if no sharedpreferences selected
                    myPlaceName = new String[]{ "bar", "restaurant" };
                } else {
                    myPlaceName = favoriteSelectedTypePlaceList.toArray(new String[ favoriteSelectedTypePlaceList.size()]);
                }
                Place.clearArray();
                return getPlaces(500.0, myPlaceName, MyCurrentLat, MyCurrentLong);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.near_element_map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.MnearElementList) {
            Intent intent = new Intent(this, nearElementList.class);
            startActivity(intent);
        }
        if (id == R.id.Mfavorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        }
        if (id == R.id.Msettings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Place>> loader){}

    @Override
    public void onLoadFinished(Loader<ArrayList<Place>> loader, ArrayList<Place> data){
        this.places = data;
        for(int i = 0; i < places.size(); i++){
            double placeLat = places.get(i).getLat();
            double placeLng = places.get(i).getLng();
            LatLng placeLatLng = new LatLng(placeLat,placeLng);
            mMap.addMarker(new MarkerOptions().position(placeLatLng)
                    .title(String.valueOf(i)));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker arg0) {
                    Marker marker = arg0;
                    if(arg0.getTitle() != "Ma Position Actuelle") {
                        int id = Integer.valueOf(arg0.getTitle());
                        Intent intent = new Intent(MapsActivityCurrentPlace, PlaceDetailsActivity.class);
                        intent.putExtra("PlaceDetails", new Gson().toJson(places.get(id)));
                        if (marker != null) {
                            //Set prevMarker back to default color
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }

                        //leave Marker default color if re-click current Marker
                        if (!arg0.equals(marker)) {
                            arg0.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        }

                        startActivity(intent);
                        return true;
                    } else {
                        arg0.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    }
                    return false;
                }
            });

            Log.d(TAG, places.get(i).getPlace_name());
        }
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Builds the map when the Google Play services client is successfully connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Play services connection suspended");
    }


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        //mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
        //});

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        MyCurrentLat = mLastKnownLocation.getLatitude();
        MyCurrentLong = mLastKnownLocation.getLongitude();
        LatLng MyCurrentLocation = new LatLng(MyCurrentLat,MyCurrentLong);
        mMap.addMarker(new MarkerOptions().position(MyCurrentLocation)
                .title("Ma Position Actuelle"));

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(QUERY_LOADER,queryURL,this);
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }
}
