package dev.ws.jamsholat.Main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.neovisionaries.i18n.CountryCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dev.ws.jamsholat.Model.Data;
import dev.ws.jamsholat.R;
import dev.ws.jamsholat.api.ApiClient;
import dev.ws.jamsholat.api.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAct extends AppCompatActivity implements View.OnClickListener {
    public static TextView tv_example;
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
    private BottomNavigationView bottom_nav;
    private RequestQueue requestQueue;

    String data = "";
    String valLat;
    String valLon;
    int valBulan;
    int valTahun;
    String valDate_or_timestamp;

    TextView tv_lokasi, tv_tanggal_dan_hari, tv_jam_menit, tv_detik, tv_waktu_sholat, tv_pembatas;
    LinearLayoutCompat ll_jam_holder;
    ImageView iv_kurangi_hari, iv_tambah_hari;
    Button btn_getdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        requestQueue = Volley.newRequestQueue(this);
        namaKota = bundle.getString("namaKota");
        namaNegara = bundle.getString("namaNegara");
        valLat = bundle.getString("valLat");
        Log.d("MainAct", "valLat: " + valLat);
        valLon = bundle.getString("valLon");
        Log.d("MainAct", "valLon: " + valLon);

        show = "timer";
        getTanggalSekarang();
        day = 0;

        tv_lokasi = findViewById(R.id.tv_lokasi);
        tv_tanggal_dan_hari = findViewById(R.id.tv_tanggal_dan_hari);
        tv_detik = findViewById(R.id.tv_detik);
        tv_jam_menit = findViewById(R.id.tv_jam_menit);
        tv_waktu_sholat = findViewById(R.id.tv_waktu_sholat);
        ll_jam_holder = findViewById(R.id.ll_jam_holder);
        tv_pembatas = findViewById(R.id.tv_pembatas);
        iv_kurangi_hari = findViewById(R.id.iv_kurangi_hari);
        iv_tambah_hari = findViewById(R.id.iv_tambah_hari);
        bottom_nav = findViewById(R.id.bottom_nav);
        tv_example = findViewById(R.id.tv_example);
        btn_getdata = findViewById(R.id.btn_getdata);

        tv_example.setMovementMethod(new ScrollingMovementMethod());

        btn_getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });



        ll_jam_holder.setOnClickListener(this);
        tv_jam_menit.setOnClickListener(this);
        tv_detik.setOnClickListener(this);
        tv_pembatas.setOnClickListener(this);
        iv_tambah_hari.setOnClickListener(this);
        iv_kurangi_hari.setOnClickListener(this);

        setTv_lokasi();
        tv_tanggal_dan_hari.setText(tanggal_hari_ini);
        createCountDown(11, 36);

        setUpBottomNav();

    }

    private void jsonParse() {
        String url = "https://api.myjson.com/bins/17882r";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat();

                        try {
                            int code = response.getInt("code");
                            if (code != 200){
                                Toast.makeText(getApplicationContext(),"Data gagal di ambil",Toast.LENGTH_SHORT).show();
                            }else {
                                tv_example.setText("RESPON: "+response.toString());
                                JSONObject data = response.getJSONObject("data");
                                tv_example.setText("DATA: "+data.toString());
                                JSONObject timings = data.getJSONObject("timings");
                                tv_example.setText("TIMINGS : "+timings.toString());
                                String intSubuh = timings.getString("Fajr");
                                tv_example.setText("Subuh : "+intSubuh+"\n");
                                String intDzuhur = timings.getString("Dhuhr");
                                String intAshar = timings.getString("Asr");
                                String intMaghrib = timings.getString("Maghrib");
                                String intIsyak = timings.getString("Isha");
                                String intImsak = timings.getString("Imsak");
                                tv_example.setText("Subuh : "+intSubuh+"\n"+
                                        "Dzuhur : "+intDzuhur+"\n"+
                                        "Ashar : "+intAshar+"\n"+
                                        "Maghrib : "+intMaghrib+"\n"+
                                        "Isyak : "+intIsyak+"\n"+
                                        "Imsyak : "+intImsak+"\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    private void setUpBottomNav(){
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = MainFrag.newInstance();
                        break;
                    case R.id.navigation_cari:
                        selectedFragment = CariFrag.newInstance();
                        break;
                    case R.id.navigation_history:
                        selectedFragment = HistoryFrag.newInstance();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, selectedFragment).commit();
                return true;
            }

        });
    }

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
        valDate_or_timestamp = Long.toString( System.currentTimeMillis());
        valBulan = new_calendar.get(Calendar.MONTH);
        valTahun = new_calendar.get(Calendar.YEAR);
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


    private void ambilJadwalSholat(){
        Log.d("MainAct","ambilJadwalSholat");
        if (valLat != null && valLon != null ){
            Log.d("MainAct","ambilJadwalSholat running");
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<Data> call = apiService.getJadwal(valDate_or_timestamp,valLat,valLon,8);
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    Log.d("Data ", " respon" + response.body().getDate());
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    Log.d("MainAct","ambilJadwalSholat gagal");
                }
            });
        }
        Log.d("MainAct","ambilJadwalSholat di batalkan");

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

    @Override
    protected void onStart() {
        super.onStart();
        //bottom_nav.setSelectedItemId(R.id.navigation_home);
    }
}
