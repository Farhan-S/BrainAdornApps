package com.udvabonisoft.brainadorn;

import static com.udvabonisoft.brainadorn.WgFirstActivity.arrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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