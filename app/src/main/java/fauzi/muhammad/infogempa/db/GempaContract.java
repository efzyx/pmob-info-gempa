package fauzi.muhammad.infogempa.db;

import android.provider.BaseColumns;

/**
 * Created by fauzi on 10/10/2017.
 */

public class GempaContract {
    //Constructor dibuat private agar tidak bisa diinstantiate
    private GempaContract() {}

    public class GempaEntry implements BaseColumns{

        public static final String TABLE_NAME = "gempa";
        public static final String COLUMN_NAME_MAGNITUDE = "magnitude";
        public static final String COLUMN_NAME_PLACE = "place";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DEPTH = "depth";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_TSUNAMI = "tsunami";
    }
}
