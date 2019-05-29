package dev.ws.jamsholat.api;

import dev.ws.jamsholat.Model.Data;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Wawan on 5/27/2019
 */
public interface ApiService {
    /*@GET("?latitude={lat}&longitude={lon}&method=8&month={bulan}&year={tahun}")*/
    Call<Data> getJadwal(@Query("date_or_timestamp") String date_or_timestamp,@Query("latitude") String latitude, @Query("longitude") String longitude,@Query("method") int method);

}
