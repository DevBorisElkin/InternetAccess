package ru.startandroid.hw3_internetaccess;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import io.ghyeok.stickyswitch.widget.StickySwitch;
import ru.startandroid.hw3_internetaccess.Database.City;
import ru.startandroid.hw3_internetaccess.Database.CityDataSource;
import ru.startandroid.hw3_internetaccess.Fragments.CityList;
import ru.startandroid.hw3_internetaccess.Fragments.Fragment_about;
import ru.startandroid.hw3_internetaccess.Fragments.Fragment_feedback;
import ru.startandroid.hw3_internetaccess.Fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    private static final String POSITIVE_BUTTON_TEXT = "Go";
    public static String KEY = "STRING_53235";
    public static String KEY_SHARED_PREF = "somekey";
    public static String KEY_SHARED_PREF_TWO = "anotherkey";
    public static String INTENT_KEY = "img9";
    public static final String DATA_NEED_FOR_TEST = "img9";
    private ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    ImageView imageView;
    public static final int ENCRYPTION_KEY = 4;
    List<City> elements;
    CityDataSource cityDataSource;
    ImageButton imageButton;
    Dialog dialog;
    ImageView closeImg;
    Button submit;
    EditText editText;

    static int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   Intent intent = new Intent(MainActivity.this, Game_activity.class);
     //   startActivity(intent);
       startFragment();

    }

    private void startFragment() {
        new Thread() {
            public void run() {
                init_DB_connection();
            }
        }.start();
        strangeAction();
        initDrawer();
        initUI();
        imageAction();
        fragmentAction(loadChosenCity());
    }

    private void strangeAction() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }

    private void initUI() {
        dialog = new Dialog(this);

    }


    private void init_DB_connection() {

        cityDataSource = new CityDataSource(this);
        cityDataSource.open();
        elements = cityDataSource.getAllCities();
    }

    private void imageAction() {
        storeImage();
        loadImage();
    }

    private void loadImage() {
        try {
            String path = loadImgPath();
            loadImageFromStorage(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.opening, R.string.closing);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.about) {
                    about();
                    return true;
                } else if (item.getItemId() == R.id.feedback) {
                    feedback();
                    return true;
                } else if (item.getItemId() == R.id.settings) {
                    settings();
                    return true;
                }
                return false;
            }
        });
        drawerLayout.bringToFront();
        drawerLayout.requestLayout();
        View header = navigationView.getHeaderView(0);
        imageView = header.findViewById(R.id.imageView);
    }

    private void feedback() {
        Fragment_feedback fragment_feedback = new Fragment_feedback();
        fragmentAction(fragment_feedback);
    }

    private void about() {
        Fragment_about fragment_about = new Fragment_about();
        fragmentAction(fragment_about);
    }

    private void settings() {
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentAction(settingsFragment);
    }

    private void fragmentAction(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.change_city) {
            drawerLayout.closeDrawers();
            showInputDialog();
            return true;
        }
        if (item.getItemId() == R.id.recent) {
            drawerLayout.closeDrawers();
            CityList cityList = new CityList();
            fragmentAction(cityList);
            return true;
        }
        return false;
    }


    private void showInputDialog() {

        dialog.setContentView(R.layout.custom_popup_choose_city);
        closeImg = dialog.findViewById(R.id.close);
        submit = dialog.findViewById(R.id.submit);
        editText = dialog.findViewById(R.id.editText);
        closeImg.setOnClickListener(close());
        submit.setOnClickListener(doIt());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @NonNull
    private View.OnClickListener doIt() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editText.getText().toString();
                if (city.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter city first..", Toast.LENGTH_SHORT).show();
                } else {
                    fragmentAction2(city);
                    dialog.dismiss();
                }
            }
        };
    }

    public void addElement(String city) {
        CityDataSource.addCity(city);
        dataUpdated();
    }

    private void dataUpdated() {
        elements.clear();
        elements.addAll(cityDataSource.getAllCities());
        //adapter.notifyDataSetChanged();
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

    private void fragmentAction(String city) {
        MainFragment mainFragment = new MainFragment();
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        ft2.addToBackStack(null);
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        if (intent.getStringExtra(INTENT_KEY) != null) {
            String str = intent.getStringExtra(INTENT_KEY);
            bundle.putString(KEY, str);
            storeChosenCity(str);
            mainFragment.setArguments(bundle);
            ft2.replace(R.id.fragment_container, mainFragment);
            ft2.commit();
        } else
            microAction(city, mainFragment, ft2, bundle);
    }

    private void fragmentAction2(String city) {
        MainFragment mainFragment = new MainFragment();
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        ft2.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putString(KEY, city);
        storeChosenCity(city);
        //addElement(city);
        mainFragment.setArguments(bundle);
        ft2.replace(R.id.fragment_container, mainFragment);
        ft2.commit();
    }

    private void microAction(String city, MainFragment mainFragment, FragmentTransaction ft2, Bundle bundle) {
        bundle.putString(KEY, city);
        storeChosenCity(city);
        mainFragment.setArguments(bundle);
        ft2.replace(R.id.fragment_container, mainFragment);
        ft2.commit();
    }

    //______________________________________________________________________________________________________________
    public void storeChosenCity(String city) {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPreferences.edit();
//        CaesarCipher encode = new CaesarCipher();
//        String encodedCity=encode.encrypt(city, ENCRYPTION_KEY);
        myEditor.putString("city", city);
        myEditor.apply();
    }

    public String loadChosenCity() {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREF, MODE_PRIVATE);
        String chosenCity = sharedPreferences.getString("city", "Alice Springs");
//        CaesarCipher encode = new CaesarCipher();
//        String decodedCity=encode.decrypt(chosenCity, ENCRYPTION_KEY);
        return chosenCity;
    }

    //______________________________________________________________________________________________________________
    public void storeImgPath(String path) {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREF_TWO, MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPreferences.edit();
        myEditor.putString("path", path);
        myEditor.apply();
    }

    public String loadImgPath() {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SHARED_PREF_TWO, MODE_PRIVATE);
        String path = sharedPreferences.getString("path", "none");
        return path;
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//        File mypath = new File(directory, "profile.png");
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }

    private void loadImageFromStorage(String path) {
        //deprecated
//        try {
//            File f = new File(path, "profile.png");
//            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
//            imageView.setImageBitmap(b);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    private void storeImage() {
//        try {
//            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.me);
//            String path = saveToInternalStorage(bm);
//            storeImgPath(path);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

