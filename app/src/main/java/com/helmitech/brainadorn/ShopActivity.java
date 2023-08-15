package com.helmitech.brainadorn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class ShopActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView buyNow,aquiredStar,aquiredDimond;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error

                        mInterstitialAd = null;
                    }
                });


        buyNow=findViewById(R.id.buyNow);
        aquiredStar=findViewById(R.id.aquiredStar);
        aquiredDimond=findViewById(R.id.aquiredDimond);

        sharedPreferences = getSharedPreferences("" + getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        aquiredStar.setText(sharedPreferences.getString("availableHint","5"));
        aquiredDimond.setText(sharedPreferences.getString("availableDiamond","50"));

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int diamond=Integer.parseInt(sharedPreferences.getString("availableDiamond","50"));

                if(diamond>=1)
                {
                    MainActivity.cash_register.start();

                    int hint=Integer.parseInt(sharedPreferences.getString("availableHint","5"));

                    hint+=2;
                    diamond-=1;
                    editor.putString("availableHint",String.valueOf(hint));
                    editor.putString("availableDiamond",String.valueOf(diamond));
                    editor.apply();
                    aquiredStar.setText(sharedPreferences.getString("availableHint","5"));
                    aquiredDimond.setText(sharedPreferences.getString("availableDiamond","50"));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not enough diamond !",Toast.LENGTH_SHORT).show();
                    MainActivity.glass_breaking.start();
                }

                }

        });


    }

    @Override
    public void onBackPressed()
    {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
        finish();
    }
}