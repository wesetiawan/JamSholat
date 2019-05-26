package dev.ws.jamsholat.Model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by Wawan on 5/27/2019
 */
public class GPSTracker implements LocationListener {
    private int LOCATION_PERMISSION_CODE = 1;


    Activity activity;
    public GPSTracker(Activity a){
        this.activity = a;

    }


    public Location getLocation(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestLocationPermission();
            return null;
        }else {
            LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnable){
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,10,this);
                Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return l;
            }else {
                Toast.makeText(activity,"Mohon Aktifkan GPS Anda",Toast.LENGTH_LONG).show();
                return null;
            }
        }

    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_FINE_LOCATION)){
            showAlertDialog();
        }else {
            ActivityCompat.requestPermissions(activity,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_CODE);
        }
    }

    private void showAlertDialog(){
        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(activity);
        alerDialogBuilder
                .setTitle("Akses lokasi di butuhkan")
                .setMessage("Akses lokasi di butuhkan untuk mendapatkan lokasi kamu")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_CODE);
                    }
                })
                .create().show();
    }




    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
