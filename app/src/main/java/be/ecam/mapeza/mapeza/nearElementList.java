package be.ecam.mapeza.mapeza;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

import static be.ecam.mapeza.mapeza.Place.getPlaceById;
import static be.ecam.mapeza.mapeza.R.id.resultView;
import static be.ecam.mapeza.mapeza.loadPlaces.getPlaces;

public class nearElementList extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ItemAdapter.ItemAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Place>>,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
    private RecyclerView resultView;
    private ItemAdapter itemAdapter;
    private static final int QUERY_LOADER = 22;
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

    Bundle queryURL = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_element_list);

        //initialisation du RecyclerView
        resultView = (RecyclerView) findViewById(R.id.resultView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        resultView.setLayoutManager(layoutManager);
        resultView.setHasFixedSize(true);

        //initialisation de l'item adapter
        itemAdapter = new ItemAdapter(this);
        resultView.setAdapter(itemAdapter);

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
    public void onClick(int index) {
        Intent intent = new Intent(this, PlaceDetailsActivity.class);
        intent.putExtra("PlaceDetails", new Gson().toJson(getPlaceById(index)));
        startActivity(intent);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Place>> loader){}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    /**
     * Builds the map when the Google Play services client is successfully connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        if(mLastKnownLocation != null) {
            MyCurrentLat = mLastKnownLocation.getLatitude();
            MyCurrentLong = mLastKnownLocation.getLongitude();

            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(QUERY_LOADER, queryURL, this);
        }
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


    //Ajout pour le Loader Manager
    @Override
    public Loader<ArrayList<Place>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Place>>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public ArrayList<Place> loadInBackground()
            {
                Gson gson = new Gson();
                SharedPreferences mPrefs;
                mPrefs = PreferenceManager.getDefaultSharedPreferences(nearElementList.this);
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
                String apiKey = getResources().getString(R.string.google_maps_key);
                return getPlaces(apiKey, 500.0, myPlaceName, MyCurrentLat, MyCurrentLong);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Place>> loader, ArrayList<Place> data){
        itemAdapter.setData(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.near_element_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.MmapsCurrentPlace) {
            Intent intent = new Intent(this, MapsActivityCurrentPlace.class);
            startActivity(intent);
        }
        if (id == R.id.Mfavorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        }
        if (id == R.id.Msettings) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
    }

}
