package be.ecam.mapeza.mapeza;

import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by 13069 on 18-04-17.
 */

public class loadPlaces {
    public static ArrayList<Place> getPlaces(double radius, String[] myPlaceName, Double myLatPosition, Double myLngPosition){
        //Nos coordonnées: à récupérer (position actuelle)
        //myLatPosition = 50.8503;
        //myLonPosition = 4.3517;
        Double myPlaceDistanceMeters = radius;


        // myPlaceName représente un array de string
        // qui est a récupérer du sharedpreferences: on doit boucler dessus

        ArrayList<String> JSON = new ArrayList<String>();
        try {
            for(int i=0; i<myPlaceName.length; i++) {
                //Log.v("test",myPlaceName[i]);
                String nearByPlaceSearchURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                        + "location=" + myLatPosition + "," + myLngPosition
                        + "&radius=" + myPlaceDistanceMeters
                        + "&types=" + myPlaceName[i]
                        + "&key=" + "AIzaSyBLkj8WSx2AjfJNN0-BEYplZQWwNs8mYBU";

                Log.v("test",nearByPlaceSearchURL);

                String tempJSON = NetworkUtils.getResponseFromHttpUrl(nearByPlaceSearchURL);
                Place.parse(tempJSON, myPlaceName[i]);
            }
            return Place.getPlaces();
        }catch (Exception e){
            return null;
        }
    };
}
