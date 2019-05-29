package dev.ws.jamsholat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dev.ws.jamsholat.Main.MainAct;
import dev.ws.jamsholat.Model.GPSTracker;

public class HomeAct extends AppCompatActivity implements View.OnClickListener {
    private int LOCATION_PERMISSION_CODE = 1;

    EditText et_lokasi;
    ImageView iv_get_location;
    Button btn_cari;

    double lat;
    double lon;
    String namaKota;
    String namaNegara;
    String valLat;
    String valLon;
    String strBulan;
    String strTahun;


    Drawable ic_location_disable, ic_location_enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        et_lokasi = findViewById(R.id.et_lokasi);
        iv_get_location = findViewById(R.id.iv_get_location);
        btn_cari = findViewById(R.id.btn_cari);
        iv_get_location.setOnClickListener(this);
        btn_cari.setOnClickListener(this);
        ic_location_enable = getDrawable(R.drawable.ic_location);
        ic_location_disable = getDrawable(R.drawable.ic_location_disable);

        checkLocationPermission();

    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(HomeAct.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            iv_get_location.setImageDrawable(ic_location_enable);
        } else {
            iv_get_location.setImageDrawable(ic_location_disable);
        }
    }

    private void getCurrentLocation() {
        GPSTracker gpsTracker = new GPSTracker(HomeAct.this);
        Location l = gpsTracker.getLocation();
        if (l != null) {
            lat = l.getLatitude();
            lon = l.getLongitude();
            namaNegara = getCountyName(lat, lon);
            namaKota = getCityName(lat, lon);
            Log.d("HomeAct", "My Lat : " + lat + " My Lon : " + lon);
            et_lokasi.setText(namaKota + " , " + namaNegara);
            valLat = Double.toString(lat);
            Log.d("HomeAct", "valLat: " + valLat);
            valLon = Double.toString(lon);
            Log.d("HomeAct", "valLat: " + valLat);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_get_location:
                getCurrentLocation();
                break;
            case R.id.btn_cari:
                cariJadwal();
                break;
        }
    }

    private void cariJadwal() {
        btn_cari.setText("Loading...");
        btn_cari.setEnabled(false);
        Intent intent = new Intent(this, MainAct.class);
        if (namaKota != null && namaNegara != null && valLat != null && valLon != null){
            intent.putExtra("namaKota", namaKota);
            intent.putExtra("namaNegara", namaNegara);
            intent.putExtra("valLat", valLat);
            intent.putExtra("valLon", valLon);
            startActivity(intent);
            finish();
        }else {
            if (namaKota == null){
                errorSendData("Maaf gagal Mendapatkan Nama Kota");
            }else if (namaNegara == null){
                errorSendData("Maaf gagal Mendapatkan Nama Negara");
            }else if (valLon == null){
                errorSendData("Maaf gagal mendapatkan valLon");
            }else if (valLat == null){
                errorSendData("Maaf gagal mendapatkan valLat");
            }
        }


    }

    private void errorSendData(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT);
        btn_cari.setText("CARI");
        btn_cari.setEnabled(true);
    }

    private String getCityName(double lat, double lon) {
        String namaKota = "";
        Geocoder geocoder = new Geocoder(HomeAct.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            namaKota = addresses.get(0).getSubAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return namaKota;

    }

    private String getCountyName(double lat, double lon) {
        String namaNegara = "";
        Geocoder geocoder = new Geocoder(HomeAct.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            namaNegara = addresses.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return namaNegara;

    }

    /*private void ambilJamSholat(){
        if (valLat != null && valLon != null ){
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<Data> call = apiService.getJadwal(valLat,valLon,strBulan,strTahun);
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    Log.d("Data ", " respon" + response.body().getDate());
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {

                }
            });
        }

    }*/




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
            }
        }
    }
}
