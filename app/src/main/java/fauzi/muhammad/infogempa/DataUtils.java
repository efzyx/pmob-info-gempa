package fauzi.muhammad.infogempa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fauzi on 10/10/2017.
 */

public class DataUtils {
    public static List<Gempa> parseJsonString(String s) throws JSONException {

        List<Gempa> gempaList = new ArrayList<>();

        JSONObject jsonObject1 = new JSONObject(s);

        JSONArray features = jsonObject1.getJSONArray("features");
        for (int i = 0; i < features.length(); i++) {

            double magnitude = features.getJSONObject(i).getJSONObject("properties").getDouble("mag");
            String place = features.getJSONObject(i).getJSONObject("properties").getString("place");
            long longdate = features.getJSONObject(i).getJSONObject("properties").getLong("time");
            double depth = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(2);
            String longitude = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getString(0);
            String latitude = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getString(1);
            int tsunami = features.getJSONObject(i).getJSONObject("properties").getInt("tsunami");
            Date date = new Date(longdate);

            Gempa gempa = new Gempa(magnitude, place, date, depth, longitude, latitude, tsunami);
            gempaList.add(gempa);
        }
        return gempaList;
    }
}
