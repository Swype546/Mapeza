package be.ecam.mapeza.mapeza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class FavoritesActivity extends AppCompatActivity implements ItemAdapter.ItemAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Place>> {
    private static final String TAG = FavoritesActivity.class.getSimpleName();
    private DBHelper FavoritesDBHElper;
    private RecyclerView resultView;
    private ItemAdapter itemAdapter;
    private ArrayList<Place> places = new ArrayList<Place>();
    private static final int QUERY_LOADER = 22;
    Bundle queryURL = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_element_list);
        FavoritesDBHElper = new DBHelper(this);

        resultView = (RecyclerView) findViewById(R.id.resultView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        resultView.setLayoutManager(layoutManager);
        resultView.setHasFixedSize(true);

        itemAdapter = new ItemAdapter(this);
        resultView.setAdapter(itemAdapter);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(QUERY_LOADER,queryURL,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites_menu, menu);
        return true;
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
        if (id == R.id.Msettings) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int index) {
        Intent intent = new Intent(this, PlaceDetailsActivity.class);
        intent.putExtra("PlaceDetails", new Gson().toJson(this.places.get(index)));
        startActivity(intent);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Place>> loader){}

    @Override
    public Loader<ArrayList<Place>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Place>>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public ArrayList<Place> loadInBackground(){
                return FavoritesDBHElper.getPlaces();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Place>> loader, ArrayList<Place> data){
        this.places = data;
        itemAdapter.setData(data);
    }

}
