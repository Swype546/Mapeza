package be.ecam.mapeza.mapeza;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Thibaut on 26/03/2017.
 */

public class PlaceList
{
    private ArrayList<PlaceType> placesList = new ArrayList<PlaceType>();

    String[] places_name = new String[]{
            "Bars", "Stade", "Centres commerciaux","Restaurants", "Parcs", "Magasin d'électronique", "Casino", "Parc d'attraction", "Boites de nuit", "Pinard"
    };

    String[] places_code = new String[]{
            "bar", "stadium", "shopping_mall", "restaurant", "park", "electronics_store", "casino", "amusement_park", "night_club", "liquor_store"
    };

    public PlaceList()
    {
        for(int i = 0; i < places_name.length; i++)
        {
            placesList.add(new PlaceType(places_name[i],false));
        }
    }

    ArrayList<PlaceType> getList()
    {
        return placesList;
    }

    String[] stringArray()
    {
        String[] stringArray = new String[places_name.length];
        for(int i=0;i<places_name.length;i++)
        {
            stringArray[i] = placesList.get(i).getName();
        }
        return stringArray;
    }

    public PlaceType getPlace(int i)
    {
        return placesList.get(i);
    }

    private void setPlace(int i, String name, boolean selected)
    {
        placesList.set(i, new PlaceType(name, selected));
    }

    public String getTranslationInFrench(String code)
    {
        int pos= Arrays.asList(places_code).indexOf(code);
        return places_name[pos];
    }

    public String getMatchingToAPICode(String name)
    {
        int pos=placesList.indexOf(name);
        return places_code[pos];
    }

    public String getMatchingToAPICode(int i)
    {
        return places_code[i];
    }
    public void clear()
    {
        placesList.clear();
    }
}

