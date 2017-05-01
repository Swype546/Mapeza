package be.ecam.mapeza.mapeza;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = PlaceDetailsActivity.class.getSimpleName();
    private DBHelper FavoritesDBHElper;
    private GoogleApiClient mGoogleApiClient;
    private String placeId;
    private be.ecam.mapeza.mapeza.Place mPlace;
    private LatLng map;
    private Place gPlace;
    private GoogleMap mMap;
    private TextView placeDetailsAll;
    private TextView placeDetailsName;
    private ImageButton favorite;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.placeDetailsMap);
        mapFragment.getMapAsync(this);

        // Default : Brussels
        this.map = new LatLng(50.85045, 4.34878);
        // Default placeId and mPlace set to ECAM
        this.placeId = "ChIJMSHd8mnEw0cR5So9dEPw0lU";
        String name = "ECAM";
        String addr = "Prom. de l'Alma 50, 1200 Woluwe-Saint-Lambert, Belgium";
        String type = "School";
        Double lat = 50.8501926;
        Double lng = 4.4541356;
        this.mPlace = new be.ecam.mapeza.mapeza.Place(this.placeId, name,
                addr, type, lat, lng);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, null)
                .build();

        ratingBar = (RatingBar) findViewById(R.id.PlaceDetailsRatingBar);
        ratingBar.setIsIndicator(true);
        ratingBar.setRating(0);

        FavoritesDBHElper = new DBHelper(this);

        // Restore data when rotating device
        if(savedInstanceState != null) {
            Log.d(TAG, "onCreate() Restoring previous state");
            if(savedInstanceState.containsKey("PlaceDetailsSaveInstanceState")) {
                this.placeId = savedInstanceState.getString("PlaceDetailsSaveInstanceState");
            }
        }
        else {
            // Retrieving the Place object send by the previous activity (nearElementList, FavoritesActivity or Map)
            String jsonPlace = "";
            Bundle extras = this.getIntent().getExtras();
            if (extras != null) {
                jsonPlace = extras.getString("PlaceDetails");
                this.mPlace = new Gson().fromJson(jsonPlace, be.ecam.mapeza.mapeza.Place.class);
                Log.d(TAG, this.mPlace.getPlace_name());
                // Retrieve placeId from the retrieved Place object
                this.placeId = this.mPlace.getPlace_id();
            }
        }

        Log.d(TAG, placeId);
        // Using Google Places API with placeId
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        placeDetailsAll = (TextView) findViewById(R.id.placeDetailsAll);
        placeDetailsName = (TextView) findViewById(R.id.PlaceDetailsName);
        placeDetailsAll.setText("Loading ...");



        favorite = (ImageButton)findViewById(R.id.placeDetailsFavBtn);

        if(FavoritesDBHElper.checkPlaceById(placeId)){
            favorite.setSelected(true);
        } else {
            favorite.setSelected(false);
        }
        favorite.setOnClickListener(new View.OnClickListener()   {
            @Override
            public void onClick(View v)  {
                if(!favorite.isSelected()){
                    if(FavoritesDBHElper.insert(mPlace)) {
                        showToast("Successfully added to favorites.");
                        favorite.setSelected(true);
                    } else {
                        showToast("Failure, please try again.");
                    }
                } else {
                    if(FavoritesDBHElper.delete(placeId)) {
                        showToast("Successfully removed from favorites.");
                        favorite.setSelected(false);
                    } else {
                        showToast("Failure, please try again.");
                    }
                }
            }
        });
    }

    private void showToast(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("PlaceDetailsSaveInstanceState", placeId);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (gPlace != null) {
            map = gPlace.getLatLng();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(map));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(map, 16));
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                placeDetailsAll.setText(places.getStatus().getStatusMessage());
                return;
            }
            // Selecting the first object buffer.
            if(places.getCount() > 0) {
                gPlace = places.get(0);
                if (gPlace != null) {
                    map = gPlace.getLatLng();
                    Log.d(TAG, map.toString());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(map));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(map, 16));
                    mMap.addMarker(new MarkerOptions().position(gPlace.getLatLng()).title(gPlace.getName().toString()));

                    ratingBar.setRating(gPlace.getRating());
                    placeDetailsName.setText(gPlace.getName());
                    placeDetailsAll.setText(getPlaceDetailsAll());
                }
            } else {
                placeDetailsAll.setText("Place not found");
            }
            places.release();
        }
    };

    public String getPlaceDetailsAll(){
        String str = "";
        str += "Address : " + gPlace.getAddress().toString() + "\n";
        str += "Phone : " + gPlace.getPhoneNumber().toString() + "\n";
        str += "WebSite : " + gPlace.getWebsiteUri().toString() + "\n";
        Log.d(TAG, str);
        return str;
    }

}