package com.helmitech.brainadorn;

import static com.helmitech.brainadorn.WgFirstActivity.arrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;



import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class PopupActiviy extends Activity {

    HashMap<String, String> hashMap = new HashMap<>();

    TextView lvlIndicator ,playBtn,noPlayBtn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_activiy);

        lvlIndicator=findViewById(R.id.lvlIndicator);
        playBtn=findViewById(R.id.playBtn);
        noPlayBtn=findViewById(R.id.noPlayBtn);

        sharedPreferences=getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor=sharedPreferences.edit();

        int lvl=sharedPreferences.getInt("lvlNum",0);
        lvl+=1;
        lvlIndicator.setText("LEVEL "+lvl);


        HashMap<String, String> hashMap = arrayList.get(sharedPreferences.getInt("lvlNum",0));
        String quiz = hashMap.get("quiz");
        String ans = hashMap.get("ans");
        String playable=hashMap.get("playable");



        noPlayBtn.setVisibility(View.GONE);

        if(!Objects.equals(playable, "yes"))
        {
            playBtn.setVisibility(View.GONE);
            noPlayBtn.setVisibility(View.VISIBLE);
        }

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(PopupActiviy.this,WgPlayActivity.class);
                Random ran= new Random();
                int a;
                a=ran.nextInt(8);
                if(a==1)
                {
                    MainActivity.whoosh_1.start();
                }
                else if(a==2)
                {
                    MainActivity.whoosh_2.start();
                }
                else if(a==3)
                {
                    MainActivity.whoosh_3.start();
                }
                else if(a==4)
                {
                    MainActivity.whoosh_4.start();
                }
                else if(a==5)
                {
                    MainActivity.whoosh_5.start();
                }

                else if(a==6)
                {
                    MainActivity.whoosh_6.start();
                }
                else
                {
                    MainActivity.whoosh_7.start();
                }
                finish();
                startActivity(i);
            }
        });


        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.4));

        WindowManager.LayoutParams params=  getWindow().getAttributes();
        params.gravity= Gravity.CENTER;

        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);


    }
}