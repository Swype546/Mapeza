package be.ecam.mapeza.mapeza;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 13069 on 18-04-17.
 */

public class Place {

    private String place_id;
    private String place_name;
    private String address;
    private String type;
    private double lat;
    private double lng;

    private static ArrayList<Place> array = new ArrayList<Place>();

    public Place(String place_id, String place_name, String address, String type, double lat, double lng){
        this.place_id = place_id;
        this.place_name = place_name;
        this.address = address;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
    }

    //parsing du JSON
    public static void parse(String json, String type) throws JSONException {
        //On reset l'ArrayList pour ne pas rajouter dans la liste les données qui sont rechargées
        //array.clear();

        JSONObject jsonPlaces = new JSONObject(json);
        JSONArray jsonResults = jsonPlaces.getJSONArray("results");
        for (int i=0; i<jsonResults.length(); i++) {
            JSONObject JSONPlace = jsonResults.getJSONObject(i);
            JSONObject JSONGeometry = JSONPlace.getJSONObject("geometry");
            JSONObject JSONLocation = JSONGeometry.getJSONObject("location");
            double tempLat = JSONLocation.getDouble("lat");
            double tempLng = JSONLocation.getDouble("lng");

            String tempPlace_id = JSONPlace.getString("place_id");
            String tempType = type;
            String tempAddress = JSONPlace.getString("vicinity");
            String tempName = JSONPlace.getString("name");
            array.add(new Place(tempPlace_id, tempName, tempAddress, tempType, tempLat, tempLng));
        }
    }

    public String getPlace_id(){return this.place_id;}
    public String getPlace_name(){return this.place_name;}
    public String getAddress(){return this.address;}
    public String getType(){return this.type;}
    public double getLat(){return this.lat;}
    public double getLng(){return this.lng;}
    public static ArrayList<Place> getPlaces(){return array;}
    public static void clearArray()
    {
        array.clear();
    }
}
