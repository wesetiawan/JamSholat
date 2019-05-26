package dev.ws.jamsholat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Wawan on 5/27/2019
 */
public class Data {

    @SerializedName("timings")
    @Expose
    private Timings timings;
    @SerializedName("date")
    @Expose
    private Date date;

    public Timings getTimings() {
        return timings;
    }

    public void setTimings(Timings timings) {
        this.timings = timings;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
