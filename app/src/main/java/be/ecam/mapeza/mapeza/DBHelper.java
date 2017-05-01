package be.ecam.mapeza.mapeza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Othman on 18-04-17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE IF NOT EXISTS " + DBContract.PlaceEntry.TABLE_NAME + " (" +
                        DBContract.PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DBContract.PlaceEntry.PLACE_ID + " STRING NOT NULL, " +
                        DBContract.PlaceEntry.PLACE_ADDRESS + " STRING NOT NULL, " +
                        DBContract.PlaceEntry.PLACE_NAME + " STRING NOT NULL, " +
                        DBContract.PlaceEntry.PLACE_TYPE + " STRING NOT NULL, " +
                        DBContract.PlaceEntry.PLACE_LAT + " DOUBLE NOT NULL, " +
                        DBContract.PlaceEntry.PLACE_LNG + " DOUBLE NOT NULL" +
                        "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                          int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +
                DBContract.PlaceEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Place> getPlaces() {
        SQLiteDatabase mDB = this.getReadableDatabase();
        ArrayList<Place> places = new ArrayList<Place>();
        Cursor cursor = mDB.query(
                DBContract.PlaceEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        Log.d(TAG, "select() : cursor.getCount() = " + cursor.getCount());

        if(cursor.getCount() > 0) {
            try {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(
                            cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_ID));
                    String address = cursor.getString(
                            cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_ADDRESS));
                    String name = cursor.getString(
                            cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_NAME));
                    String type = cursor.getString(
                            cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_TYPE));
                    double lat = cursor.getFloat(
                            cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_LAT));
                    double lng = cursor.getFloat(
                            cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_LNG));

                    //Log.d(TAG, "Place : " + id + " | " + address + " | " + name + " | " + type
                    //        + " | " + lat + " | " + lng);
                    Place place = new Place(id, name, address, type, lat, lng);
                    places.add(place);
                }
            } finally {
                cursor.close();
                mDB.close();
            }
        }
        return places;
    }

    public Boolean checkPlaceById(String id){
        SQLiteDatabase mDB = this.getReadableDatabase();
        Cursor cursor = mDB.query(
                DBContract.PlaceEntry.TABLE_NAME,
                null,
                DBContract.PlaceEntry.PLACE_ID + " = ?",
                new String[] { id },
                null,
                null,
                null);

        Log.d(TAG, "checkPlaceById() : cursor.getCount() = " + cursor.getCount());
        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean insert(Place place){
        SQLiteDatabase mDB = this.getWritableDatabase();

        String id = place.getPlace_id();
        String address = place.getAddress();
        String name = place.getPlace_name();
        String type = place.getType();
        double lat = place.getLat();
        double lng = place.getLng();

        ContentValues cv = new ContentValues();
        cv.put(DBContract.PlaceEntry.PLACE_ID, id);
        cv.put(DBContract.PlaceEntry.PLACE_ADDRESS, address);
        cv.put(DBContract.PlaceEntry.PLACE_NAME, name);
        cv.put(DBContract.PlaceEntry.PLACE_TYPE, type);
        cv.put(DBContract.PlaceEntry.PLACE_LAT, lat);
        cv.put(DBContract.PlaceEntry.PLACE_LNG, lng);
        long result = mDB.insert(DBContract.PlaceEntry.TABLE_NAME, null, cv);

        mDB.close();
        Log.d(TAG, "insert() : result = " + result);
        if (result > -1 ){
            return true;
        } else {
            return false;
        }
    }

    public Boolean delete(String id){
        SQLiteDatabase mDB = this.getWritableDatabase();

        long result = 0;

        try {
            result = mDB.delete(DBContract.PlaceEntry.TABLE_NAME,
                    DBContract.PlaceEntry.PLACE_ID + " = ?", new String[] { id });
        } finally {
            mDB.close();
        }

        Log.d(TAG, "delete() : result = " + result);
        if (result > 0 ){
            return true;
        } else {
            return false;
        }
    }
}
