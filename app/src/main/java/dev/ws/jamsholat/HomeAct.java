package dev.ws.jamsholat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dev.ws.jamsholat.Model.GPSTracker;

public class HomeAct extends AppCompatActivity implements View.OnClickListener {
    private int LOCATION_PERMISSION_CODE = 1;

    EditText et_lokasi;
    ImageView iv_get_location;
    double lat;
    double lon;
    String namaKota;
    String namaNegara;
    String alamat;


    Drawable ic_location_disable, ic_location_enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        et_lokasi = findViewById(R.id.et_lokasi);
        iv_get_location = findViewById(R.id.iv_get_location);
        ic_location_enable = getDrawable(R.drawable.ic_location);
        ic_location_disable = getDrawable(R.drawable.ic_location_disable);

        checkLocationPermission();

        iv_get_location.setOnClickListener(this);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(HomeAct.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            iv_get_location.setImageDrawable(ic_location_enable);
        } else {
            iv_get_location.setImageDrawable(ic_location_disable);
        }
    }

    private void getCurrentLocation(){
        GPSTracker gpsTracker = new GPSTracker(HomeAct.this);
        Location l = gpsTracker.getLocation();
        if (l != null){
            lat = l.getLatitude();
            lon = l.getLongitude();
            alamat = getAlamat(lat,lon);
            namaNegara = getCountyName(lat,lon);
            namaKota = getCityName(lat,lon);
            namaKota = namaKota.replace("Kota ","");
            Log.d("HomeAct","My Lat : "+lat+" My Lon : "+lon);
            Log.d("HomeAct","MyCity : "+namaKota+" "+"MyCountry : "+namaNegara+" "+"Alamat Kamu "+alamat);
            et_lokasi.setText(namaKota+" , "+namaNegara);

        }
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

    private String getAlamat(double lat, double lon) {
        String alamat = "";
        Geocoder geocoder = new Geocoder(HomeAct.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            alamat = addresses.get(0).getThoroughfare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alamat;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_get_location:
                getCurrentLocation();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                checkLocationPermission();
            }
        }
    }
}
