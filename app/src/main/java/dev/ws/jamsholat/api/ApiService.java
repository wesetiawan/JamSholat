package dev.ws.jamsholat.api;

import dev.ws.jamsholat.Model.Data;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Wawan on 5/27/2019
 */
public interface ApiService {

    @GET("v1/timingsByCity?city=Yogyakarta&country=Indonesia&method=8")
    Call<Data> getJadwal();

}
