package be.ecam.mapeza.mapeza;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 13069 on 18-04-17.
 */

public class loadPlaces {

    public static ArrayList<Place> getPlaces(double radius){
        //Nos coordonnées: à récupérer (position actuelle)
        Double myLatPosition = 50.8503;
        Double myLonPosition = 4.3517;
        Double myPlaceDistanceMeters = radius;

        //Ces éléments sont a récupérer du sharedpreferences: on doit boucler dessus
        String[] myPlaceName = {"bar","cafe"};
        ArrayList<String> JSON = new ArrayList<String>();
        try {
            for(int i=0; i<myPlaceName.length; i++) {
                String nearByPlaceSearchURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                        + "location=" + myLatPosition + "," + myLonPosition
                        + "&radius=" + myPlaceDistanceMeters
                        + "&types=" + myPlaceName[i]
                        + "&key=" + "AIzaSyBLkj8WSx2AjfJNN0-BEYplZQWwNs8mYBU";

                String tempJSON = NetworkUtils.getResponseFromHttpUrl(nearByPlaceSearchURL);
                Place.parse(tempJSON, myPlaceName[i]);
            }

            return Place.getPlaces();
        }catch (Exception e){
            return null;
        }
    };
}
