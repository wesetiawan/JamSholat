package dev.ws.jamsholat.api;

import dev.ws.jamsholat.Model.JadwalSholat;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Wawan on 5/27/2019
 */
public interface ApiService {
    @GET("{periode}/daily.json?key=ac045fde4c38654fee34b9bb78a5afd6")
    Call<JadwalSholat> getJadwalSholat(@Path("periode") String periode);

}
