package dev.ws.jamsholat.Main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.ws.jamsholat.R;

public class MainAct extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        MainFrag mainFrag = new MainFrag();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_container,mainFrag);
        fragmentTransaction.commit();

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainFrag mainFrag = new MainFrag();
                    fragmentTransaction.replace(R.id.fl_container,mainFrag);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_cari:
                    CariFrag cariFrag = new CariFrag();
                    fragmentTransaction.replace(R.id.fl_container,cariFrag);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_history:
                    HistoryFrag historyFrag = new HistoryFrag();
                    fragmentTransaction.replace(R.id.fl_container,historyFrag);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

}
