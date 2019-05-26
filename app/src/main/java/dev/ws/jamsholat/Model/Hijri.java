package dev.ws.jamsholat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Wawan on 5/27/2019
 */
public class Hijri {
    @SerializedName("date")
    @Expose
    private String date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
