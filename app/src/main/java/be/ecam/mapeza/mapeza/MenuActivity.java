package be.ecam.mapeza.mapeza;
// à remplacer par le nom de domaine adéquat : be.ecam.mapeza.mapeza

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
public class MenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // Comportement du bouton "A Propos"
                Intent testActivity = new Intent(MenuActivity.this, MenuActivity.class);

                startActivity(testActivity);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onClick(Button button) {
        // Handle navigation view item clicks here.
        int id = button.getId();

        if (id == R.id.buttonHome) {
            Intent testActivity = new Intent(MenuActivity.this, HomeActivity.class);

            startActivity(testActivity);
            // Handle the camera action
        } else if (id == R.id.buttonDetails) {
            Intent testActivity = new Intent(MenuActivity.this, nearElementList.class);

            startActivity(testActivity);

        } else if (id == R.id.buttonMap) {
            Intent testActivity = new Intent(MenuActivity.this, PlaceDetailsActivity.class);

            startActivity(testActivity);

        } else if (id == R.id.buttonSettings) {
           // Intent testActivity = new Intent(MenuActivity.this, settings.class);

         //   startActivity(testActivity);
        }

    }

}
