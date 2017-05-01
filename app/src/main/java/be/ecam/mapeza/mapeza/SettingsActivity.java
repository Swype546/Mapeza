package be.ecam.mapeza.mapeza;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Hadrien on 01-05-17.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.MnearElementList) {
            Intent intent = new Intent(this, nearElementList.class);
            startActivity(intent);
        }
        if (id == R.id.MmapsCurrentPlace) {
            Intent intent = new Intent(this, MapsActivityCurrentPlace.class);
            startActivity(intent);
        }
        if (id == R.id.Mfavorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}