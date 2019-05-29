package dev.ws.jamsholat.Model;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dev.ws.jamsholat.Main.MainAct;
import dev.ws.jamsholat.Main.MainFrag;

/**
 * Created by Wawan on 5/29/2019
 */
public class GetData extends AsyncTask<Void,Void,Void> {
    String dataParse = "";
    String singleParse = "";
    String data;
    RequestQueue requestQueue;
    @Override
    protected Void doInBackground(Void... voids) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("http://api.aladhan.com/v1/timings/1559095261?latitude=-7.8129103&longitude=110.38000571&method=8");
            connection =  (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine())!= null){
                stringBuffer.append(line+"\n");
                data = data + line;
            }

            JSONArray JA = new JSONArray(data);
            for (int i = 0; i<JA.length(); i++){
                JSONObject ar = JA.getJSONObject(i);
                singleParse = "Fajr : "+ ar.get("Fajr")+"\n"+
                        "Sunrise : "+ ar.get("Sunrise")+"\n"+
                        "Dhuhr : "+ ar.get("Dhuhr")+"\n"+
                        "Asr : "+ ar.get("Asr")+"\n"+
                        "Sunset : "+ ar.get("Sunset")+"\n"+
                        "Maghrib : "+ ar.get("Maghrib")+"\n"+
                        "Isha : "+ ar.get("Isha")+"\n"+
                        "Imsak : "+ ar.get("Imsak")+"\n";

                dataParse = dataParse + singleParse;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null ){
                connection.disconnect();
            }
            try {
                if (reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainAct.tv_example.setText(this.data);
    }
}
