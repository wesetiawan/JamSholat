package dev.ws.jamsholat.Main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.neovisionaries.i18n.CountryCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import dev.ws.jamsholat.R;

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
    private String url;

    String valLat;
    String valLon;
    int jam;
    int menit;
    int detik;
    int nextJam;
    int nextMenit;
    long valDate_or_timestamp;

    TextView tv_lokasi, tv_tanggal_dan_hari, tv_jam_menit, tv_detik, tv_waktu_sholat, tv_pembatas;
    LinearLayoutCompat ll_jam_holder;
    ImageView iv_kurangi_hari, iv_tambah_hari, btn_back, btn_more;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        requestQueue = Volley.newRequestQueue(this);
        namaKota = bundle.getString("namaKota");
        namaKota = namaKota.substring(namaKota.lastIndexOf(" ")+1);
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
        btn_back = findViewById(R.id.btn_back);
        btn_more = findViewById(R.id.btn_more);

        tv_example.setMovementMethod(new ScrollingMovementMethod());

        ll_jam_holder.setOnClickListener(this);
        tv_jam_menit.setOnClickListener(this);
        tv_detik.setOnClickListener(this);
        tv_pembatas.setOnClickListener(this);
        iv_tambah_hari.setOnClickListener(this);
        iv_kurangi_hari.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        setTv_lokasi();
        tv_tanggal_dan_hari.setText(tanggal_hari_ini);
        getCurrentTime();
        getTanggalSekarang();
        createCountDown();

        setUpBottomNav();
        jsonParse();
    }

    /*private void getJamSholat(){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<JadwalSholat> call = apiService.getJadwalSholat(namaKota);
        Log.d("MainAct","Url : "+ call.getClass());

        call.enqueue(new Callback<JadwalSholat>() {
            @Override
            public void onResponse(Call<JadwalSholat> call, Response<JadwalSholat> response) {
                int status = response.body().getStatusCode();
                if (status == 0){
                    items = response.body().getItems();
                    if (items != null){
                        String subuh = items.get(0).getFajr();
                        String dzuhur = items.get(0).getDhuhr();
                        String ashar = items.get(0).getAsr();
                        String maghrib = items.get(0).getMaghrib();
                        String isyak = items.get(0).getIsha();
                        tv_example.setText("Subuh : "+subuh+"\n"+
                                "Dzuhur : "+dzuhur+"\n"+
                                "Ashar : "+ashar+"\n"+
                                "Maghrib : "+maghrib+"\n"+
                                "Isyak : "+isyak+"\n");
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Maaf terjadi kesalahan mohon refres ulang", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JadwalSholat> call, Throwable t) {

            }
        });
    }*/

    /*private static List<JadwalSholat>fromJson(JSONArray array){
        ArrayList<JadwalSholat> res = new ArrayList<>();
        for (int i = 0; i<array.length();i++){
            JSONObject jadwalSholat = array.getJSONObject(i);
            jadwalSholat.
        }


        return
    }*/

    private void jsonParse() {

        url = "https://muslimsalat.com/"+namaKota+"/weekly.json";
        Log.d("MainAct", "url:" + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int code = response.getInt("status_valid");
                            if (code != 1) {

                                Toast.makeText(getApplicationContext(), "Data gagal di ambil", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray jsonArray = response.getJSONArray("items");
                                for (int i = 0;i<jsonArray.length();i++){
                                    JSONObject items = jsonArray.getJSONObject(i);
                                    String date = items.getString("date_for");
                                    tv_example.append(date+"\n");

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Kesalahan pada jaringan , Mungkin server error",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    private void setUpBottomNav() {
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
        calendarDay.add(Calendar.DAY_OF_YEAR, day);
        tanggal_hari_ini = dateFormat.format(calendarDay.getTime());
        day = 0;
        tv_tanggal_dan_hari.setText(tanggal_hari_ini);
    }


    private void getTanggalSekarang() {
        String countryCode = CountryCode.findByName(namaNegara).get(0).name();
        Locale new_Locale = new Locale(countryCode);
        Calendar new_calendar = Calendar.getInstance(new_Locale);
        DateFormat new_dateFormat = DateFormat.getDateInstance(DateFormat.FULL, new_Locale);
        dateFormat = new_dateFormat;
        locale = new_Locale;
        calendar = new_calendar;
        calendarDay = new_calendar;
        tanggal_hari_ini = new_dateFormat.format(new_calendar.getTime());
        valDate_or_timestamp = calendar.get(Calendar.MONTH);
    }

    private void createCountDown() {
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 22);
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
        jam = currentTime.get(Calendar.HOUR_OF_DAY);
        menit = currentTime.get(Calendar.MINUTE);
        detik = currentTime.get(Calendar.SECOND);
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
                day += 1;
                tambahkurangHari();
                break;
            case R.id.iv_kurangi_hari:
                day -= 1;
                tambahkurangHari();
                break;
            case R.id.btn_back:
            case R.id.btn_more:
                onBackPressed();
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //bottom_nav.setSelectedItemId(R.id.navigation_home);
    }
}
