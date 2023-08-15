package com.helmitech.brainadorn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;



public class SecondPageActivity extends AppCompatActivity {
    public static String backgroundImageName;
    Context context = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView setProfile;
    ImageView dpPic,img1,img2,img3,img4,img5,img6,img7,img8,img9,img10,img11,img12,img13,img14,img15;
    TextView dpName;
    String[] images={"img1","img2","img3","img4","img5","img6","img7","img8","img9","img10","img11","img12","img13","img14","img15"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setProfile=findViewById(R.id.setProfile);
        dpName=findViewById(R.id.dpName);
        dpPic=findViewById(R.id.dpPic);
        img1=findViewById(R.id.img1);
        img2=findViewById(R.id.img2);
        img3=findViewById(R.id.img3);
        img4=findViewById(R.id.img4);
        img5=findViewById(R.id.img5);
        img6=findViewById(R.id.img6);
        img7=findViewById(R.id.img7);
        img8=findViewById(R.id.img8);
        img9=findViewById(R.id.img9);
        img10=findViewById(R.id.img10);
        img11=findViewById(R.id.img11);
        img12=findViewById(R.id.img12);
        img13=findViewById(R.id.img13);
        img14=findViewById(R.id.img14);
        img15=findViewById(R.id.img15);


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img1);
                backgroundImageName ="img1" ;
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img2);
                backgroundImageName ="img2" ;
            }
        });


        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img3);
                backgroundImageName ="img3" ;
            }
        });


        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img4);
                backgroundImageName ="img4" ;
            }
        });


        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img5);
                backgroundImageName ="img5";
            }
        });


        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img6);
                backgroundImageName ="img6";
            }
        });


        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img7);
                backgroundImageName ="img7";
            }
        });


        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img8);
                backgroundImageName ="img8";
            }
        });


        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img9);
                backgroundImageName ="img9";
            }
        });


        img10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img10);
                backgroundImageName ="img10";
            }
        });


        img11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img11);
                backgroundImageName ="img11";
            }
        });


        img12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img12);
                backgroundImageName ="img12";
            }
        });

        img13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img13);
                backgroundImageName ="img13";
            }
        });

        img14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img14);
                backgroundImageName ="img14";
            }
        });

        img15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpPic.setBackgroundResource(R.drawable.img15);
                backgroundImageName ="img15";




            }
        });




        sharedPreferences=getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor=sharedPreferences.edit();




        dpName.setText(sharedPreferences.getString("dpName","JOHN WICK").toUpperCase());


        setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.pop_2.start();

                editor.putString("identity","yes");

                editor.putString("dpPicName",backgroundImageName);
                editor.apply();

                Intent intent1=new Intent(SecondPageActivity.this,ThirdPageActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}