package dev.ws.jamsholat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Wawan on 5/27/2019
 */
public class Date {
    @SerializedName("readable")
    @Expose
    private String readable;
    @SerializedName("hijri")
    @Expose
    private Hijri hijri;

    public String getReadable() {
        return readable;
    }

    public void setReadable(String readable) {
        this.readable = readable;
    }


    public Hijri getHijri() {
        return hijri;
    }

    public void setHijri(Hijri hijri) {
        this.hijri = hijri;
    }

}
