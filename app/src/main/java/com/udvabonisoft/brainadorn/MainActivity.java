package com.udvabonisoft.brainadorn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sharedPreferences=getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor=sharedPreferences.edit();


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