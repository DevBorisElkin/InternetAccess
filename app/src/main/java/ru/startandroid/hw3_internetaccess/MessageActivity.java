package ru.startandroid.hw3_internetaccess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {
    EditText editText;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        txt=findViewById(R.id.textView);
         editText=findViewById(R.id.editText);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")){
                    Toast.makeText(MessageActivity.this, "Fill in email address first", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("vnd.android-dir/mms-sms");
                    String message=txt.getText().toString();
                    String address=editText.getText().toString();
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Current weather");
                    intent.putExtra(Intent.EXTRA_TEXT   , message);
                    intent.putExtra(Intent.EXTRA_EMAIL, address);
                    startActivity(Intent.createChooser(intent, "send by email"));
                }
            }
        });

       Intent intent=getIntent();
       if(intent!=null){
           String tempC=intent.getStringExtra(MainFragment.tempCodeC);
           String tempF=intent.getStringExtra(MainFragment.tempCodeF);
           String city=intent.getStringExtra(MainFragment.cityCode);
           String weather=intent.getStringExtra(MainFragment.weatherCode);

           String currentInfo="In "+city.toUpperCase()+" current temperature in Celsius is "+tempC+"°C, in Fahrenheits is "+tempF+"°F, weather state is: "+weather;

           txt.setText(currentInfo);
       }



    }
}

