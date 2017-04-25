package be.ecam.mapeza.mapeza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by Othman on 18-04-17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mapeza.db";
    private static final int DATABASE_VERSION = 1;
    private final SQLiteDatabase mDB = this.getWritableDatabase();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE IF NOT EXISTS " + DBContract.PlaceEntry.TABLE_NAME + " (" +
                        DBContract.PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DBContract.PlaceEntry.PLACE_ID + " STRING NOT NULL, " +
                        DBContract.PlaceEntry.PLACE_ADDRESS + " STRING NOT NULL, " +
                        DBContract.PlaceEntry.PLACE_NAME + " STRING NOT NULL, " +
                        DBContract.PlaceEntry.PLACE_TYPE + " STRING NOT NULL" +
                        DBContract.PlaceEntry.PLACE_LAT + " FLOAT NOT NULL" +
                        DBContract.PlaceEntry.PLACE_LNG + " FLOAT NOT NULL" +
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
    /*
    public select() {
        Cursor cursor = mDB.query(
                DBContract.PlaceEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        if(cursor.getCount() > 0) {
            // list of places
            if (!cursor.moveToPosition(0)) return;
            while (cursor.moveToNext()) {
                int pos = cursor.getPosition();
                String id = cursor.getString(
                        cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_ID));
                String address = cursor.getString(
                        cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_ADDRESS));
                String name = cursor.getString(
                        cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_NAME));
                String type = cursor.getString(
                        cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_TYPE));
                float lat = cursor.getFloat(
                        cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_LAT));
                float lng = cursor.getFloat(
                        cursor.getColumnIndex(DBContract.PlaceEntry.PLACE_LNG));

                // new places object
                // add to places list
            }
        }
        // return places list
        return null;
    }

    public long insert(String id, String address, String name, String type, float lat, float lng){
        ContentValues cv = new ContentValues();
        cv.put(DBContract.PlaceEntry.PLACE_ID, id);
        cv.put(DBContract.PlaceEntry.PLACE_ADDRESS, address);
        cv.put(DBContract.PlaceEntry.PLACE_NAME, name);
        cv.put(DBContract.PlaceEntry.PLACE_TYPE, type);
        cv.put(DBContract.PlaceEntry.PLACE_LAT, lat);
        cv.put(DBContract.PlaceEntry.PLACE_LNG, lng);
        long result = mDB.insert(DBContract.PlaceEntry.TABLE_NAME, null, cv);
        return result;
    }

    public long delete(String id){
        mDB.query(
                DBContract.PlaceEntry.TABLE_NAME,
                null,
                DBContract.PlaceEntry.PLACE_ID + " = ?",
                new String[] { id },
                null,
                null,
                null);
        long result = mDB.delete(DBContract.PlaceEntry.TABLE_NAME,
                DBContract.PlaceEntry._ID + " = " + id, null);
        return result;
    }
    */
}
