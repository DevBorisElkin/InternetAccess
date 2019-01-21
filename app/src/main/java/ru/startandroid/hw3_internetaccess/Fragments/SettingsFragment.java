package ru.startandroid.hw3_internetaccess.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import io.ghyeok.stickyswitch.widget.StickySwitch;
import ru.startandroid.hw3_internetaccess.MainActivity;
import ru.startandroid.hw3_internetaccess.R;
import ru.startandroid.hw3_internetaccess.Service.StartedService;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    StickySwitch stickySwitch;
    ImageButton imageButton;
    Dialog dialog, dialog2;
    ImageView closeImg;
    Button iGotThis, submit;
    EditText editText;

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        Button buttonAddCity = view.findViewById(R.id.button_addCity);
        dialog = new Dialog(getContext());
        dialog2 = new Dialog(getContext());
        imageButton = view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(onClick());
        stickySwitch = view.findViewById(R.id.sticky_switch);
        stickySwitch.setOnSelectedChangeListener(switchListener());
        buttonAddCity.setOnClickListener(changeChosenCity());
    }

    @NonNull
    private StickySwitch.OnSelectedChangeListener switchListener() {
        return new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {

                if (direction == StickySwitch.Direction.RIGHT) {
                    activate();
                } else {
                    deactivate();
                }

            }
        };
    }

    @NonNull
    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        };
    }

    private void showPopup() {
        dialog.setContentView(R.layout.custom_popup_menu);
        closeImg = dialog.findViewById(R.id.close);
        iGotThis = dialog.findViewById(R.id.got_it);
        closeImg.setOnClickListener(close());
        iGotThis.setOnClickListener(close());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @NonNull
    private View.OnClickListener close() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };
    }

    @NonNull
    private View.OnClickListener close2() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        };
    }

    @NonNull
    private View.OnClickListener doIt() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editText.getText().toString();
                if (city.equals("")) {
                    makeText("Enter city ");
                } else {
                    storeCity(city);
                    makeText("In order to apply changes, restart process");
                    dialog2.dismiss();
                }
            }
        };
    }

    private void activate() {
        makeText("activated");
        Intent intent = new Intent(getActivity(), StartedService.class);
        String city = loadChosenCity();
        intent.putExtra(MainActivity.DATA_NEED_FOR_TEST, city);
        getActivity().startService(intent);
    }

    private void deactivate() {
        makeText("deactivated");
        Intent intent = new Intent(getActivity(), StartedService.class);
        getActivity().stopService(intent);
    }

    private void makeText(String a) {
        Toast.makeText(getContext(), a, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    private View.OnClickListener changeChosenCity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.setContentView(R.layout.custom_popup_two);
                closeImg = dialog2.findViewById(R.id.close);
                submit = dialog2.findViewById(R.id.submit);
                editText = dialog2.findViewById(R.id.editText);
                closeImg.setOnClickListener(close2());
                submit.setOnClickListener(doIt());
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        };
    }

    private void storeCity(String city) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.KEY_SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPreferences.edit();
        myEditor.putString("favourite_city", city);
        myEditor.apply();
    }

    public String loadChosenCity() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.KEY_SHARED_PREF, MODE_PRIVATE);
        String chosenCity = sharedPreferences.getString("favourite_city", "Alice Springs");
        return chosenCity;
    }


}
