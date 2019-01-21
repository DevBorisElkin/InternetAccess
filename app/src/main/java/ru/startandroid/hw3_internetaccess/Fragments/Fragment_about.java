package ru.startandroid.hw3_internetaccess.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.startandroid.hw3_internetaccess.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_about extends Fragment {


    public Fragment_about() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_about, container, false);
    }

}
