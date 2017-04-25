package be.ecam.mapeza.mapeza;

import android.provider.BaseColumns;

/**
 * Created by Othman on 18-04-17.
 */

public class DBContract {
    public static final class PlaceEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String PLACE_ID = "id";
        public static final String PLACE_ADDRESS = "address";
        public static final String PLACE_NAME = "name";
        public static final String PLACE_TYPE = "type";
        public static final String PLACE_LAT = "lat";
        public static final String PLACE_LNG = "lng";
    }
}
