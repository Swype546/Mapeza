package be.ecam.mapeza.mapeza;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import static be.ecam.mapeza.mapeza.R.id.resultView;

public class nearElementList extends AppCompatActivity implements ItemAdapter.ItemAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String>, SharedPreferences.OnSharedPreferenceChangeListener {
    //textView text;
    private RecyclerView resultView;
    private ItemAdapter itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_element_list);


        //initialisation du RecyclerView
        resultView = (RecyclerView) findViewById(R.id.resultView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        resultView.setLayoutManager(layoutManager);
        resultView.setHasFixedSize(true);

        //initialisation de l'item adapter
        itemAdapter = new ItemAdapter(this);
        resultView.setAdapter(itemAdapter);

        LoaderManager loaderManager = getSupportLoaderManager();
    }

    @Override
    public void onClick(int index) {
        Context context = this;
        //Class destinationClass = nearElementActivity;
        //Intent intent = new Intent(context, destinationClass);
        //intent.putExtra(Intent.EXTRA_INDEX, index);
        //startActivity(intent);
    }

    @Override
    public void onLoaderReset(Loader<String> loader){}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    //Ajout pour le Loader Manager
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public String loadInBackground(){return null;};
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data){
        itemAdapter.setData(new String[]{"a","b","c","d","e"});
    }

}
