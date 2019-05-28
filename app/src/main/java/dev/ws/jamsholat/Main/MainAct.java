package dev.ws.jamsholat.Main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.neovisionaries.i18n.CountryCode;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import dev.ws.jamsholat.R;

public class MainAct extends AppCompatActivity implements View.OnClickListener {
    private Fragment selectedFragment;
    private String namaKota = "";
    private String namaNegara = "";
    private String show = "";
    private String tanggal_hari_ini = "";
    private Locale locale;
    private DateFormat dateFormat;
    private long sisaWaktu;
    private Calendar calendar, currentTime, calendarDay;
    int day;

    TextView tv_lokasi, tv_tanggal_dan_hari, tv_jam_menit, tv_detik, tv_waktu_sholat, tv_pembatas;
    LinearLayoutCompat ll_jam_holder;
    ImageView iv_kurangi_hari, iv_tambah_hari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        namaKota = bundle.getString("namaKota");
        namaNegara = bundle.getString("namaNegara");
        show = "timer";
        getTanggalSekarang();
        day = 0;

        BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav);
        tv_lokasi = findViewById(R.id.tv_lokasi);
        tv_tanggal_dan_hari = findViewById(R.id.tv_tanggal_dan_hari);
        tv_detik = findViewById(R.id.tv_detik);
        tv_jam_menit = findViewById(R.id.tv_jam_menit);
        tv_waktu_sholat = findViewById(R.id.tv_waktu_sholat);
        ll_jam_holder = findViewById(R.id.ll_jam_holder);
        tv_pembatas = findViewById(R.id.tv_pembatas);
        iv_kurangi_hari = findViewById(R.id.iv_kurangi_hari);
        iv_tambah_hari = findViewById(R.id.iv_tambah_hari);

        ll_jam_holder.setOnClickListener(this);
        tv_jam_menit.setOnClickListener(this);
        tv_detik.setOnClickListener(this);
        tv_pembatas.setOnClickListener(this);
        iv_tambah_hari.setOnClickListener(this);
        iv_kurangi_hari.setOnClickListener(this);


        bottom_nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setTv_lokasi();
        tv_tanggal_dan_hari.setText(tanggal_hari_ini);
        createCountDown(4, 14);

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
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, selectedFragment).commit();
            return true;
        }
    };

    private void tambahkurangHari() {
        calendarDay = Calendar.getInstance(locale);
        if (day == 0) {
            getTanggalSekarang();
            tanggal_hari_ini = dateFormat.format(calendarDay.getTime());
        }else {
            calendarDay.add(Calendar.DAY_OF_YEAR, day);
            tanggal_hari_ini = dateFormat.format(calendarDay.getTime());
        }

        tv_tanggal_dan_hari.setText(tanggal_hari_ini);
    }


    private void getTanggalSekarang() {
        String countryCode = CountryCode.findByName(namaNegara).get(0).name();
        Locale new_Locale = new Locale(countryCode);
        DateFormat new_dateFormat = DateFormat.getDateInstance(DateFormat.FULL, new_Locale);
        Calendar new_calendar = Calendar.getInstance(new_Locale);
        dateFormat = new_dateFormat;
        locale = new_Locale;
        currentTime = new_calendar;
        calendar = new_calendar;
        tanggal_hari_ini = new_dateFormat.format(new_calendar.getTime());
    }

    private void createCountDown(int jam, int menit) {
        calendar.set(Calendar.HOUR_OF_DAY, jam);
        calendar.set(Calendar.MINUTE, menit);
        long waktu = calendar.getTimeInMillis() - System.currentTimeMillis();
        Log.d("MainAc", "CountdownTimmer Sebelum jalan: " + waktu);
        new CountDownTimer(waktu, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("MainAc", "CountdownTimmer Running: " + millisUntilFinished);
                sisaWaktu = millisUntilFinished;
                if (show.equals("timer")) {
                    getCurrentTimeCountDown();
                } else {
                    getCurrentTime();
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void getCurrentTime() {
        currentTime = Calendar.getInstance(locale);
        currentTime.getTime();
        long jam = currentTime.get(Calendar.HOUR_OF_DAY);
        long menit = currentTime.get(Calendar.MINUTE);
        long detik = currentTime.get(Calendar.SECOND);
        tv_jam_menit.setText(String.format(locale, "%01d:%02d", jam, menit));
        tv_detik.setText(String.format(locale, "%01d", detik));
    }

    private void getCurrentTimeCountDown() {
        int jam = (int) (sisaWaktu / (1000 * 60 * 60)) % 24;
        int menit = (int) (sisaWaktu / (1000 * 60)) % 60;
        int detik = (int) (sisaWaktu / 1000) % 60;
        tv_jam_menit.setText("-" + String.format(locale, "%01d:%02d", jam, menit));
        tv_detik.setText(String.format(locale, "%01d", detik));

    }

    private void setTv_lokasi() {
        if (namaKota == null) {
            namaKota = "";
        } else if (namaKota.contains("Kota")) {
            Log.d("MainAct", "Found Kota from NamaNegara");
            namaKota = namaKota.replace("Kota", "");
        }
        if (namaNegara == null) {
            namaNegara = "";
        }
        tv_lokasi.setText(namaKota + ", " + namaNegara);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_jam_holder:
            case R.id.tv_jam_menit:
            case R.id.tv_detik:
                if (show.equals("timer")) {
                    show = "clock";
                    getCurrentTime();
                } else {
                    getCurrentTimeCountDown();
                    show = "timer";
                }
                break;
            case R.id.iv_tambah_hari:
                day = day+1;
                Log.d("MainAct","Tambah hari, day:"+day);
                tambahkurangHari();
                break;
            case R.id.iv_kurangi_hari:
                day = day-1;
                Log.d("MainAct","Kurangi hari, day:"+day);
                tambahkurangHari();
                break;

        }
    }


}
