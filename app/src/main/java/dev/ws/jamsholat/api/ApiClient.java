package dev.ws.jamsholat.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Wawan on 5/27/2019
 */
public class ApiClient {
    public static final String BASE_URL =  "http://api.aladhan.com/";

    public static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
