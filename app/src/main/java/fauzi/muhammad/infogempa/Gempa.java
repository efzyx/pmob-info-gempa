package fauzi.muhammad.infogempa;

import java.util.Date;

/**
 * Created by fauzi on 10/10/2017.
 */

public class Gempa {
    public double magnitude;
    public String place;
    public Date date;
    public double depth;
    public String longitude;
    public String latitude;
    public int tsunami;

    public Gempa(double magnitude, String place, Date date, double depth, String longitude, String latitude, int tsunami) {
        this.magnitude = magnitude;
        this.place = place;
        this.date = date;
        this.depth = depth;
        this.longitude = longitude;
        this.latitude = latitude;
        this.tsunami = tsunami;
    }
}
