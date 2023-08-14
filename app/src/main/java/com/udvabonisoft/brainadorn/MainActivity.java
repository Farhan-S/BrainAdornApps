package com.udvabonisoft.brainadorn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static MediaPlayer crowd_booing, fail_1, fail_2, glass_breaking,heartbeat,kids_cheering,people_clapping,pop_1,
            pop_2,whoosh_1,whoosh_2,whoosh_3,whoosh_4,whoosh_5,whoosh_6,whoosh_7,wrong_answer,cash_register,
            fireball_whoosh,bell_transition,fart_2,fart_1,flashback,punch_1,punch_2,typewriter,long_suspense_2,long_suspense_3,reloading,slap,splat,rubber_duck,mouse_click;


    public static Animation click_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sharedPreferences=getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor=sharedPreferences.edit();

        crowd_booing = MediaPlayer.create(getApplicationContext(), R.raw.crowd_booing);
        fail_1 = MediaPlayer.create(getApplicationContext(), R.raw.fail_1);
        fail_2 = MediaPlayer.create(getApplicationContext(), R.raw.fail_2);
        glass_breaking = MediaPlayer.create(getApplicationContext(), R.raw.glass_breaking);
        heartbeat = MediaPlayer.create(getApplicationContext(), R.raw.heartbeat);
        kids_cheering = MediaPlayer.create(getApplicationContext(), R.raw.kids_cheering);
        people_clapping = MediaPlayer.create(getApplicationContext(), R.raw.people_clapping);
        pop_1 = MediaPlayer.create(getApplicationContext(), R.raw.pop_1);
        pop_2 = MediaPlayer.create(getApplicationContext(), R.raw.pop_2);
        whoosh_1 = MediaPlayer.create(getApplicationContext(), R.raw.whoosh_1);
        whoosh_2 = MediaPlayer.create(getApplicationContext(), R.raw.whoosh_2);
        whoosh_3 = MediaPlayer.create(getApplicationContext(), R.raw.whoosh_3);
        whoosh_4 = MediaPlayer.create(getApplicationContext(), R.raw.whoosh_4);
        whoosh_5 = MediaPlayer.create(getApplicationContext(), R.raw.whoosh_5);
        whoosh_6 = MediaPlayer.create(getApplicationContext(), R.raw.whoosh_6);
        whoosh_7 = MediaPlayer.create(getApplicationContext(), R.raw.whoosh_7);
        cash_register = MediaPlayer.create(getApplicationContext(), R.raw.cash_register);
        wrong_answer = MediaPlayer.create(getApplicationContext(), R.raw.wrong_answer);
        fireball_whoosh = MediaPlayer.create(getApplicationContext(), R.raw.fireball_whoosh);
        flashback = MediaPlayer.create(getApplicationContext(), R.raw.flashback);
        punch_1 = MediaPlayer.create(getApplicationContext(), R.raw.punch_1);
        punch_2 = MediaPlayer.create(getApplicationContext(), R.raw.punch_2);
        typewriter = MediaPlayer.create(getApplicationContext(), R.raw.typewriter);
        long_suspense_2 = MediaPlayer.create(getApplicationContext(), R.raw.long_suspense_2);
        long_suspense_3 = MediaPlayer.create(getApplicationContext(), R.raw.long_suspense_3);
        reloading = MediaPlayer.create(getApplicationContext(), R.raw.reloading);
        slap = MediaPlayer.create(getApplicationContext(), R.raw.slap);
        splat = MediaPlayer.create(getApplicationContext(), R.raw.splat);
        rubber_duck = MediaPlayer.create(getApplicationContext(), R.raw.rubber_duck);
        mouse_click = MediaPlayer.create(getApplicationContext(), R.raw.mouse_click);
        fart_2 = MediaPlayer.create(getApplicationContext(), R.raw.fart_2);
        fart_1 = MediaPlayer.create(getApplicationContext(), R.raw.fart_1);
        bell_transition = MediaPlayer.create(getApplicationContext(), R.raw.bell_transition);

        fireball_whoosh.start();


        click_anim= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click_anim);



        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {



                        if(sharedPreferences.getString("identity","no").equals("yes"))
                        {
                            startActivity(new Intent(MainActivity.this,ThirdPageActivity.class));
                            finish();
                        }
                        else {
                            startActivity(new Intent(MainActivity.this,FirstPageActivity.class));
                            finish();
                        }

                    }
                },
                2000);
    }
}