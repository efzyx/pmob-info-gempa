package fauzi.muhammad.infogempa.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Created by fauzi on 10/10/2017.

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "Gempa.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + GempaContract.GempaEntry.TABLE_NAME + " (" +
                GempaContract.GempaEntry._ID + " INTEGER PRIMARY KEY," +
                GempaContract.GempaEntry.COLUMN_NAME_MAGNITUDE + " REAL," +
                GempaContract.GempaEntry.COLUMN_NAME_PLACE + " TEXT, " +
                GempaContract.GempaEntry.COLUMN_NAME_DATE + " DATE, " +
                GempaContract.GempaEntry.COLUMN_NAME_DEPTH + " REAL)";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int update) {
        switch (old){
            case 3 :
                String SQL_ADD_COLUMN_LONGITUDE = "ALTER TABLE " + GempaContract.GempaEntry.TABLE_NAME +
                        " ADD " + GempaContract.GempaEntry.COLUMN_NAME_LONGITUDE +
                        " TEXT";
                sqLiteDatabase.execSQL(SQL_ADD_COLUMN_LONGITUDE);
                String SQL_ADD_COLUMN_LATITUDE = "ALTER TABLE " + GempaContract.GempaEntry.TABLE_NAME +
                        " ADD " + GempaContract.GempaEntry.COLUMN_NAME_LATITUDE +
                        " TEXT";
                sqLiteDatabase.execSQL(SQL_ADD_COLUMN_LATITUDE);
                String SQL_ADD_COLUMN_TSUNAMI = "ALTER TABLE " + GempaContract.GempaEntry.TABLE_NAME +
                        " ADD " + GempaContract.GempaEntry.COLUMN_NAME_TSUNAMI +
                        " REAL";
                sqLiteDatabase.execSQL(SQL_ADD_COLUMN_TSUNAMI);
                //sqLiteDatabase.close();
                break;
        }
    }
}
