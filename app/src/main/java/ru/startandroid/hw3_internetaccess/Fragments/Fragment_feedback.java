package ru.startandroid.hw3_internetaccess.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import ru.startandroid.hw3_internetaccess.MainActivity;
import ru.startandroid.hw3_internetaccess.R;



public class Fragment_feedback extends Fragment {
    EditText editText;
    ImageView imageView, hiddenView, button;
    FrameLayout frameLayout;
    float dX, dY;
    public static String KEY="key228";

    public Fragment_feedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_fragment_feedback, container, false);

        button=view.findViewById(R.id.imageSend);
        editText=view.findViewById(R.id.editText);
        imageView=view.findViewById(R.id.imageView);
        hiddenView=view.findViewById(R.id.hiddenView);
        hiddenView.setOnClickListener(egg());
        frameLayout=view.findViewById(R.id.frame);
        button.setOnClickListener(click());
        imageView.setOnTouchListener(touch());
        frameLayout=view.findViewById(R.id.frame);


        return view;
    }

    @NonNull
    private View.OnClickListener egg() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeText("This feature is enabled. Check \"MyHungryCat\" game.");
//                Intent intent = new Intent(getContext(), Game_activity.class);
//                intent.putExtra(KEY, 1);
//                startActivity(intent);
            }
        };
    }




       @NonNull
       private View.OnTouchListener touch() {
           return new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   switch (event.getAction()){
                       case  MotionEvent.ACTION_DOWN:
                           dX=imageView.getX()-event.getRawX();
                           dY=imageView.getY()-event.getRawY();
                           break;
                       case MotionEvent.ACTION_MOVE:
                           imageView.animate()
                                   .x(event.getRawX()+dX)
                                   .y(event.getRawY()+dY)
                                   .setDuration(0)
                                   .start();
                           break;
                           default:return false;
                   }
                   return true;
               }
           };
       }


    @NonNull
    private View.OnClickListener click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editText.getText().toString();
                if(s.equals("")){
                    makeText("Message can't be empty");
                }else{
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    String address="bond_ea_135@mail.ru";
                    intent.putExtra(Intent.EXTRA_EMAIL, address);
                    intent.putExtra(Intent.EXTRA_TEXT   , s);
                    startActivity(Intent.createChooser(intent, "Send by email"));
                }
            }
        };
    }

    private void makeText(String a) {
        Toast.makeText(getContext(), a, Toast.LENGTH_SHORT).show();
    }

}
