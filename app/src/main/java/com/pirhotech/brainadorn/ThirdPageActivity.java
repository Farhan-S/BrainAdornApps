package com.pirhotech.brainadorn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Random;

public class ThirdPageActivity extends AppCompatActivity {

    TextView select_Wg,dpName,TandD,select_mem,soundOn,soundOff;
    ImageView dpPic;
    String dpPicName;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        select_Wg=findViewById(R.id.select_Wg);
        select_mem=findViewById(R.id.select_mem);
        dpPic=findViewById(R.id.dpPic);
        dpName=findViewById(R.id.dpName);
        TandD=findViewById(R.id.TandD);
        soundOn=findViewById(R.id.soundOn);
        soundOff=findViewById(R.id.soundOff);


        sharedPreferences=getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor=sharedPreferences.edit();




        dpPicName=sharedPreferences.getString("dpPicName","img1");
        int resId = getResources().getIdentifier(dpPicName, "drawable", getPackageName());
        dpPic.setImageResource(resId);

        dpName.setText(sharedPreferences.getString("dpName","JOHN WICK").toUpperCase());






        select_Wg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                startActivity(new Intent(ThirdPageActivity.this,WgFirstActivity.class));
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

            }
        });

        select_mem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThirdPageActivity.this,MemoMainActivity.class));

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

            }
        });

        soundOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mute();
//                soundOn.setVisibility(View.GONE);
                soundOff.setVisibility(View.VISIBLE);
            }
        });

        soundOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.bell_transition.start();
                unmute();

                soundOff.setVisibility(View.GONE);
                soundOn.setVisibility(View.VISIBLE);
            }
        });

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)>10)
        {
            soundOn.setVisibility(View.VISIBLE);
            soundOff.setVisibility(View.GONE);
        }
        else {
            soundOff.setVisibility(View.VISIBLE);
            soundOn.setVisibility(View.GONE);
        }





    }

    private void mute() {
        //mute audio
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 1);
    }

    public void unmute() {
        //unmute audio
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 1);

    }

    @Override
    public void onBackPressed() {
        showCustomAlertDialog();
    }

    private void showCustomAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialog);
        View customLayout = getLayoutInflater().inflate(R.layout.alert_dialog_onbackpress, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();

        // Set up your custom UI elements and handle their interactions if needed

        TextView customText = customLayout.findViewById(R.id.custom_alert);
        TextView positiveButton = customLayout.findViewById(R.id.posBtn);
        TextView negativeButton = customLayout.findViewById(R.id.negBtn);

        customText.setText("YOU WANT TO EXIT THE GAME ?");



        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle positive button click
                // For example, dismiss the dialog
                finishAffinity();
            }
        });


        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle negative button click
                // For example, dismiss the dialog

                dialog.dismiss();
            }
        });


        dialog.show();
    }
}