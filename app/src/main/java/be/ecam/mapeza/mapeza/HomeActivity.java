package be.ecam.mapeza.mapeza;
// à remplacer par le nom de domaine adéquat : be.ecam.mapeza.mapeza

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences mPrefs;
    MyCustomAdapter dataAdapter = null;
    PlaceList favoritePlaceList = new PlaceList();
    ArrayList favoriteSelectedTypePlaceList = new ArrayList();
    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Generate list View from ArrayList
        displayListView();

        checkButtonClick();
    }

    private void displayListView() {

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(HomeActivity.this,
                R.layout.item_listview, favoritePlaceList.getList());
        ListView listView = (ListView) findViewById(R.id.list);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        /*
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Place place = (Place) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + place.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });
        */
    }

    private class MyCustomAdapter extends ArrayAdapter<Place> {

        private ArrayList<Place> placeList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Place> placeList) {
            super(context, textViewResourceId, favoritePlaceList.getList());
            this.placeList = new ArrayList<Place>();
            this.placeList.addAll(placeList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            //Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.item_listview, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.check);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Place place = (Place) cb.getTag();
                        /*
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        */
                        place.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Place place = favoritePlaceList.getPlace(position);
            holder.name.setText(place.getName());
            holder.name.setChecked(place.isSelected());
            holder.name.setTag(place);

            return convertView;

        }

    }

    // bouton OK pour sauver les lieux favoris sélectionnés
    private void checkButtonClick() {

        Button myButton = (Button) findViewById(R.id.select_favorite);
        myButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //StringBuffer responseText = new StringBuffer();
                //String responseText = new String();
                //responseText+="The following were selected...\n";

                ArrayList<Place> placeList = dataAdapter.placeList;
                favoriteSelectedTypePlaceList.clear();
                for(int i=0;i<favoritePlaceList.getList().size();i++){
                    Place place = favoritePlaceList.getPlace(i);
                    if(place.isSelected()){
                        //responseText.append("\n" + place.getName());
                        String selectedPlace=place.getName();
                        favoriteSelectedTypePlaceList.add(favoritePlaceList.getMatchingToAPICode(i));
                        //String favoriteString = (String) favoriteSelectedPlaceList.get(favoriteSelectedPlaceList.size() - 1);
                        //Log.v("test",String.valueOf(i));
                        //Log.v("test",responseText);
                        /*Toast.makeText(getApplicationContext(),
                                favoriteString, Toast.LENGTH_LONG).show();
                        */
                    }
                }

                /*
                Toast.makeText(getApplicationContext(),
                       responseText, Toast.LENGTH_LONG).show();
                */
               /* Toast.makeText(getApplicationContext(),
                        Arrays.toString(favoriteSelectedPlaceList.toArray()), Toast.LENGTH_LONG).show();
                */
                mPrefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(favoriteSelectedTypePlaceList); // myObject - instance of MyObject
                prefsEditor.putString("favoriteSelectedTypePlaceList", json);

                Toast.makeText(getApplicationContext(),
                       json, Toast.LENGTH_LONG).show();
                prefsEditor.commit();

                // And go to next activity (screen).

            }
        });

    }

}