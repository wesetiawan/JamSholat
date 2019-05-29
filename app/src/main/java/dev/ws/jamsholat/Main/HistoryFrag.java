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
public class HistoryFrag extends Fragment {
    private View view;
    static private HistoryFrag historyFrag;

    public static HistoryFrag newInstance(){
        historyFrag = new HistoryFrag();
        return historyFrag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);


        return view;
    }

}
