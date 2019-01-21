package ru.startandroid.hw3_internetaccess.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;


import java.util.ArrayList;
import java.util.List;

import ru.startandroid.hw3_internetaccess.Database.City;
import ru.startandroid.hw3_internetaccess.Database.CityDataSource;
import ru.startandroid.hw3_internetaccess.MainActivity;
import ru.startandroid.hw3_internetaccess.MainFragment;
import ru.startandroid.hw3_internetaccess.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityList extends Fragment {
    List<City> elements;
    ArrayAdapter<City> adapter;
    ListView listView;
    CityDataSource cityDataSource;
    Button buttonClear;
    List<City> elementsReverse;
    private final int ORIENTATION_VERTICAL = 1;

    public CityList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        recView(view);
        databaseCore();
        calculation();
        button_init(view);
    }

    private void recView(View view) {
        RecyclerView rw = view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(ORIENTATION_VERTICAL);
        rw.setLayoutManager(lm);
        rw.setAdapter(new MyAdapter());
    }

    private void databaseCore() {
        cityDataSource = new CityDataSource(getContext());
        cityDataSource.open();
        elements = cityDataSource.getAllCities();
    }

    private void calculation() {
        elementsReverse = new ArrayList<>();
        int j = elements.size();
        for (int i = 0; i < elements.size(); i++, j--) {
            elementsReverse.add(i, elements.get(j - 1));
            elementsReverse.get(i).setPosition(i);
        }
    }

    private void button_init(View view) {
        buttonClear = view.findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearList();
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new MyViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            // return elements.size();
            return elementsReverse.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        LinearLayout linearLayout;

        public MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_container, parent, false));
            itemView.setOnClickListener(this);
            textView = itemView.findViewById(R.id.textView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }

        void bind(int position) {
            //City myCity=elements.get(position);

            City myCity = elementsReverse.get(position);

            if (myCity.getPosition() % 2 == 0) {
                linearLayout.setBackgroundColor(getResources().getColor(R.color.itemContainer));
            } else {
                linearLayout.setBackgroundColor(Color.WHITE);

            }

            String city = myCity.getCity();
            textView.setText(city);
        }

        @Override
        public void onClick(View v) {
            showResultActivity(this.getLayoutPosition(), v);
        }

        private void showResultActivity(int catId, View v) {
            //City city=elements.get(catId);
            City city = elementsReverse.get(catId);
            String value = city.getCity();
            fragmentAction2(value);
        }
    }

    private void fragmentAction2(String city) {
        MainFragment mainFragment = new MainFragment();
        FragmentTransaction ft2 = getActivity().getSupportFragmentManager().beginTransaction();
        ft2.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.KEY, city);
        mainFragment.setArguments(bundle);
        ft2.replace(R.id.fragment_container, mainFragment);
        ft2.commit();
    }

    private void clearList() {
        cityDataSource.deleteAll();
        dataUpdated();
    }

    private void dataUpdated() {
        elements.clear();
        elements.addAll(cityDataSource.getAllCities());
        refresh();

    }

    private void refresh() {
        CityList cityList = new CityList();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, cityList);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
