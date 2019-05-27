package dev.ws.jamsholat.Main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.neovisionaries.i18n.CountryCode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import dev.ws.jamsholat.R;

public class MainAct extends AppCompatActivity {
    private Fragment selectedFragment;
    private String namaKota = "";
    private String namaNegara = "";
    private long sisaWaktu;
    private Locale locale;

    TextView tv_location,tv_tanggal_dan_hari,tv_timmer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        namaKota = bundle.getString("namaKota");
        namaNegara = bundle.getString("namaNegara");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        tv_location = findViewById(R.id.tv_location);
        tv_tanggal_dan_hari = findViewById(R.id.tv_tanggal_dan_hari);
        tv_timmer = findViewById(R.id.tv_timmer);

        tv_location.setText(namaKota+", "+namaNegara);
        tv_tanggal_dan_hari.setText(getDateSekarang());
        createCountDownTimer(4,21);

        MainFrag mainFrag = new MainFrag();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_container,mainFrag);
        fragmentTransaction.commit();

        tv_timmer.setText(updateTimer());

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainFrag mainFrag = new MainFrag();
                    selectedFragment = mainFrag;
                    break;
                case R.id.navigation_cari:
                    CariFrag cariFrag = new CariFrag();
                    selectedFragment = cariFrag;
                    break;
                case R.id.navigation_history:
                    HistoryFrag historyFrag = new HistoryFrag();
                    selectedFragment = historyFrag;
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,selectedFragment).commit();
            return true;
        }
    };


    private String getDateSekarang(){
        String countryCode = CountryCode.findByName(namaNegara).get(0).name();
        locale = new Locale(countryCode);
        Calendar calendar = Calendar.getInstance(locale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL,locale);
        return dateFormat.format(calendar.getTime());
    }

    private void createCountDownTimer(int jam,int menit){
        Calendar targetTime = Calendar.getInstance();
        targetTime.set(Calendar.HOUR_OF_DAY,jam);
        targetTime.set(Calendar.MINUTE,menit);
        new CountDownTimer(targetTime.getTimeInMillis()-System.currentTimeMillis(),1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sisaWaktu = millisUntilFinished;
                Log.d("MainAc","CountdownTimmer Running: "+sisaWaktu);
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

    private String updateTimer() {
        int menit = (int) (sisaWaktu / 1000)/ 60;
        int jam = (int) (sisaWaktu / 1000)/ 360;

        return String.format(Locale.getDefault(),"%02d:%02d",jam,menit);
    }
}
