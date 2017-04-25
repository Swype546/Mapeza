package be.ecam.mapeza.mapeza;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
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

public class PlaceDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = PlaceDetailsActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private String placeId = "ChIJMSHd8mnEw0cR5So9dEPw0lU";
    private LatLng map;
    private Place mPlace;
    private GoogleMap mMap;
    private TextView placeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, null)
                .build();

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        placeDetails = (TextView) findViewById(R.id.place_details_text);
        placeDetails.setText("Loading ...");
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker and move camera
        // Default : Brussels
        map = new LatLng(50.85045, 4.34878);
        if (mPlace != null) {
            map = mPlace.getLatLng();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(map));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(map, 16));
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                placeDetails.setText(places.getStatus().getStatusMessage());
                return;
            }
            // Selecting the first object buffer.
            mPlace = places.get(0);
            if (mPlace != null) {
                map = mPlace.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(map));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(map, 16));
                mMap.addMarker(new MarkerOptions().position(mPlace.getLatLng()).title(mPlace.getName().toString()));

                String placeDetailsString = placeDetailsToString(mPlace);
                Log.d(TAG, placeDetailsString);
                placeDetails.setText(placeDetailsString);
            }
            places.release();
        }
    };

    public String placeDetailsToString(Place place){
        String str = "Name : " + place.getName().toString() + "\n";
        str += "Address : " + place.getAddress().toString() + "\n";
        str += "Phone number : " + place.getPhoneNumber().toString() + "\n";
        str += "WebSite : " + place.getWebsiteUri().toString() + "\n";
        return str;
    }
}