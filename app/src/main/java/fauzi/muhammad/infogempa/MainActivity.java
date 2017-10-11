package fauzi.muhammad.infogempa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import fauzi.muhammad.infogempa.db.GempaContract;
import fauzi.muhammad.infogempa.db.DbHelper;

import static fauzi.muhammad.infogempa.db.GempaContract.GempaEntry.TABLE_NAME;

public class  MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RecyclerView mListGempaRecyclerView;
    GempaAdapter mGempaAdapter;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListGempaRecyclerView = (RecyclerView) findViewById(R.id.rv_list_gempa);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);

        mListGempaRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListGempaRecyclerView.setLayoutManager(layoutManager);

        mGempaAdapter = new GempaAdapter();
        mListGempaRecyclerView.setAdapter(mGempaAdapter);

        if(isConnectedToInternet()) {
            loadDataFromServer();
        }else{
            Toast.makeText(this, "No Internet. Data loaded from Database", Toast.LENGTH_SHORT).show();
            loadDatafromDb();
        }
    }

    public void loadDataFromServer() {
        Log.d(TAG, "Ambil data ke server");

        try {
            new UsgsTask().execute(new URL("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.geojson"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                if(isConnectedToInternet()) {
                    loadDataFromServer();
                }else{
                    loadDatafromDb();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getDataFromFixer(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }

    public void updateTextView(List<Gempa> data){
        mGempaAdapter.setData(data);
    }

    private class UsgsTask extends AsyncTask<URL, Void, String> {

        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = "";
            try {
                result = getDataFromFixer(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mListGempaRecyclerView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            List<Gempa> listGempa = null;
            try {
                listGempa = DataUtils.parseJsonString(s);
            } catch (JSONException e) {
                Log.d(TAG, e.getMessage());
                e.printStackTrace();
            }
//            Log.d(TAG, "Total Gempa yang didapat " + String.valueOf(listGempa.size()));

            if(listGempa != null){
                saveGempaToDb(listGempa);
            }

            mListGempaRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);

            updateTextView(listGempa);

//            Log.d(TAG, "Hasil " + s);

        }
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null) &&  (activeNetwork.isConnectedOrConnecting());
    }

    public void loadDatafromDb(){
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                GempaContract.GempaEntry.COLUMN_NAME_MAGNITUDE,
                GempaContract.GempaEntry.COLUMN_NAME_PLACE,
                GempaContract.GempaEntry.COLUMN_NAME_DATE,
                GempaContract.GempaEntry.COLUMN_NAME_DEPTH,
                GempaContract.GempaEntry.COLUMN_NAME_LONGITUDE,
                GempaContract.GempaEntry.COLUMN_NAME_LATITUDE,
                GempaContract.GempaEntry.COLUMN_NAME_TSUNAMI
        };
//        String selection = null;
//        String[] selectionArgs = null;
//        String sortOrder = null;

        Cursor c = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        List<Gempa> listGempa = new ArrayList<>();
        while(c.moveToNext()){
            double magnitude = c.getDouble(c.getColumnIndexOrThrow(GempaContract.GempaEntry.COLUMN_NAME_MAGNITUDE));
            String place = c.getString(c.getColumnIndexOrThrow(GempaContract.GempaEntry.COLUMN_NAME_PLACE));
            Date date = new Date(c.getLong(c.getColumnIndexOrThrow(GempaContract.GempaEntry.COLUMN_NAME_DATE)));
            double depth= c.getDouble(c.getColumnIndexOrThrow(GempaContract.GempaEntry.COLUMN_NAME_DEPTH));
            String longitude = c.getString(c.getColumnIndexOrThrow(GempaContract.GempaEntry.COLUMN_NAME_LONGITUDE));
            String latitude = c.getString(c.getColumnIndexOrThrow(GempaContract.GempaEntry.COLUMN_NAME_LATITUDE));
            int tsunami = c.getInt(c.getColumnIndexOrThrow(GempaContract.GempaEntry.COLUMN_NAME_TSUNAMI));
            Gempa gempa = new Gempa(magnitude, place, date, depth, longitude, latitude, tsunami);
            listGempa.add(gempa);
        }
        mGempaAdapter.setData(listGempa);
    }

    public void saveGempaToDb(List<Gempa> listGempa){
        DbHelper dbHelper = new DbHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //TRUNCATE semua record supaya data baru disimpan
        db.delete(TABLE_NAME, null, null);
        for (Gempa gempa : listGempa) {
            ContentValues cv = new ContentValues();
            cv.put(GempaContract.GempaEntry.COLUMN_NAME_MAGNITUDE, gempa.magnitude);
            cv.put(GempaContract.GempaEntry.COLUMN_NAME_PLACE, gempa.place);
            cv.put(GempaContract.GempaEntry.COLUMN_NAME_DATE,  gempa.date.getTime());
            cv.put(GempaContract.GempaEntry.COLUMN_NAME_DEPTH, gempa.depth);
            cv.put(GempaContract.GempaEntry.COLUMN_NAME_LONGITUDE, gempa.longitude);
            cv.put(GempaContract.GempaEntry.COLUMN_NAME_LATITUDE, gempa.latitude);
            cv.put(GempaContract.GempaEntry.COLUMN_NAME_TSUNAMI, gempa.tsunami);
            db.insert(TABLE_NAME, null, cv);
        }

    }
}