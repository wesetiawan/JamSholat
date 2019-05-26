package dev.ws.jamsholat.Main;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.ws.jamsholat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CariFrag extends Fragment {


    public CariFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cari, container, false);
    }

}
