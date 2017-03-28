package be.ecam.mapeza.mapeza;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    // String placePreference; = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        /*
        doUsefulStuffBasedOnMyPreference(
        sharedPreferences.getString("tempunit", "celsius"));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.mapeza_menu,menu);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        //doUsefulStuffBasedOnMyPreference(sharedPreferences.getString(key, "bars"));
    }

    private void doUsefulStuffBasedOnMyPreference(String placePreference)
    {
        //this.placePreference = placePreference;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        if (itemThatWasClickedId == R.id.settings) {

            Context context = this;
            Class destinationClass = SettingsActivity.class;
            Intent intent = new Intent(context, destinationClass);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
