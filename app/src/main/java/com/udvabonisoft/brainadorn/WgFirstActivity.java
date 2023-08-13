package com.udvabonisoft.brainadorn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WgFirstActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView lvl1, dpName,moreHint,moreDiamond,aquiredStar,aquiredDimond;
    ImageView dpPic;
    String dpPicName;
    public static MyAdapter myAdapter;

    public GridView gridView;
    public static ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    public HashMap<String,String> hashMap=new HashMap<>();

    private AdView mAdView;
    private RewardedAd rewardedAd;

    boolean isLoading;
    static boolean isLoaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wg_first);
        //start from here ....
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadRewardedAd();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        dpPic = findViewById(R.id.dpPic);
        dpName = findViewById(R.id.dpName);
        moreHint = findViewById(R.id.moreHint);
        moreDiamond = findViewById(R.id.moreDiamond);
        aquiredStar = findViewById(R.id.aquiredStar);
        aquiredDimond = findViewById(R.id.aquiredDimond);


        sharedPreferences = getSharedPreferences("" + getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        dpPicName = sharedPreferences.getString("dpPicName", "img1");
        int resId = getResources().getIdentifier(dpPicName, "drawable", getPackageName());
        dpPic.setImageResource(resId);
        dpName.setText(sharedPreferences.getString("dpName", "JOHN WICK").toUpperCase());

        gridView = findViewById(R.id.levelScrollView);
        loadArrayListFromStorage();
        if (arrayList.isEmpty()) {
            createTable();
            saveArrayListToStorage();
            editor.putString("availableHint","5");
            editor.putString("availableDiamond","50");
            editor.apply();

        }
        myAdapter = new MyAdapter();
        gridView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        moreHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WgFirstActivity.this,ShopActivity.class));
            }
        });


        aquiredStar.setText(sharedPreferences.getString("availableHint","5"));
        aquiredDimond.setText(sharedPreferences.getString("availableDiamond","50"));

        moreDiamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadRewardedVideoAd();
                buyDiamond();

            }
        });
    }


    private void loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    "ca-app-pub-3940256099942544/5224354917",
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.

                            rewardedAd = null;
                            WgFirstActivity.this.isLoading = false;
                            Toast.makeText(WgFirstActivity.this, "Failed to show an Ad !", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            WgFirstActivity.this.rewardedAd = rewardedAd;

                            WgFirstActivity.this.isLoading = false;
                            isLoaded=true;
                        }
                    });
        }
    }


    private void showRewardedVideo() {

        if (rewardedAd == null) {

            return;
        }
//        showVideoButton.setVisibility(View.INVISIBLE);

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.


                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.

                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        Toast.makeText(
                                        WgFirstActivity.this, "Try again !", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;

                        Toast.makeText(WgFirstActivity.this, "Closed Ads !", Toast.LENGTH_SHORT)
                                .show();
                        isLoaded=false;
                        // Preload the next rewarded ad.
                        WgFirstActivity.this.loadRewardedAd();
                    }
                });
        Activity activityContext = WgFirstActivity.this;
        rewardedAd.show(
                activityContext,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.

                        int rewardAmount = Integer.parseInt(sharedPreferences.getString("availableDiamond","50"));
                        rewardAmount+=2;
                        editor.putString("availableDiamond",String.valueOf(rewardAmount));
                        editor.apply();
                        Toast.makeText(getApplicationContext(),"You have earned 2 diamond !",Toast.LENGTH_SHORT).show();
                    }
                });
    }
//


    public void saveArrayListToStorage() {
        try {
            FileOutputStream fileOutputStream = openFileOutput("myArrayListFile", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(arrayList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadArrayListFromStorage() {
        try {
            FileInputStream fileInputStream = openFileInput("myArrayListFile");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            arrayList = (ArrayList<HashMap<String, String>>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public class  MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = inflater.inflate(R.layout.level_item, viewGroup, false);

            ImageView star1=myView.findViewById(R.id.star1);
            ImageView star2=myView.findViewById(R.id.star2);
            ImageView star3=myView.findViewById(R.id.star3);
            TextView lvl = myView.findViewById(R.id.lvl);

            HashMap<String, String> hashMap = arrayList.get(position);
            String numStar = hashMap.get("numStar");

            if (position != 0)
            {
                int resId = getResources().getIdentifier("levellocked", "drawable", getPackageName());
                lvl.setBackgroundResource(resId);
                lvl.setText(null);
            }

                if(Objects.equals(numStar,"3"))
                {
                    star1.setImageResource(R.drawable.levelstarsuccess);
                    star2.setImageResource(R.drawable.levelstarsuccess);
                    star3.setImageResource(R.drawable.levelstarsuccess);
                    lvl.setBackgroundResource(R.drawable.levelstarsuccess);
                    lvl.setText("");
                }
                else if (Objects.equals(numStar,"2"))
                {
                    star1.setImageResource(R.drawable.levelstarsuccess);
                    star2.setImageResource(R.drawable.levelstarsuccess);
                    lvl.setBackgroundResource(R.drawable.bronze_star);
                    lvl.setText("");
                }
                else if (Objects.equals(numStar,"1"))
                {
                    star1.setImageResource(R.drawable.levelstarsuccess);
                    lvl.setBackgroundResource(R.drawable.levelstarfailed);
                    lvl.setText("");
                }
            lvl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editor.putInt("lvlNum", position);
                    editor.apply();

                    Intent i = new Intent(getApplicationContext(), PopupActiviy.class);
                    startActivity(i);
                    myAdapter.notifyDataSetChanged();
                }
            });
            return myView;
        }
    }

    private void buyDiamond() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialog);
        View customLayout = getLayoutInflater().inflate(R.layout.diamond_shop_alert, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();

        // Set up your custom UI elements and handle their interactions if needed
        TextView positiveButton = customLayout.findViewById(R.id.posBtn);
        TextView negativeButton = customLayout.findViewById(R.id.negBtn);



   if(isLoaded)
   {
       positiveButton.setVisibility(View.VISIBLE);

   }
   else {
       positiveButton.setVisibility(View.INVISIBLE);
   }





        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showRewardedVideo();

            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void createTable() {

        hashMap=new HashMap<>();
        hashMap.put("playable", "yes");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has keys but can't open locks?");
        hashMap.put("ans", "piano");
        hashMap.put("hint1", "It produces music when played.");
        hashMap.put("hint2", "It has black and white keys.");
        arrayList.add(hashMap);

        hashMap=new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What comes once in a minute, twice in a moment, but never in a thousand years?");
        hashMap.put("ans", "m");
        hashMap.put("hint1", "It's a letter of the alphabet");
        hashMap.put("hint2", ",It's related to time.");
        arrayList.add(hashMap);


        hashMap=new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", " I am taken from a mine and shut up in a wooden case, from which I am never released, and yet I am used by many. What am I?");
        hashMap.put("ans", "pencil");
        hashMap.put("hint1", "It's a writing instrument.");
        hashMap.put("hint2", "It's made of graphite.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a heart that doesn't beat?");
        hashMap.put("ans", "artichoke");
        hashMap.put("hint1", " It's a vegetable.");
        hashMap.put("hint2", "It has many leaves.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "I have cities, but no houses. I have mountains, but no trees. I have water, but no fish. What am I?");
        hashMap.put("ans", "map");
        hashMap.put("hint1", " It's a representation of geographical features.");
        hashMap.put("hint2", " It helps with navigation.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What gets wet while drying?");
        hashMap.put("ans", "towel");
        hashMap.put("hint1", " It's used to dry things.");
        hashMap.put("hint2", "You often use it after taking a shower.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a head and a tail but no body?");
        hashMap.put("ans", "coin");
        hashMap.put("hint1", "It's used as currency.");
        hashMap.put("hint2", "It has two sides.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "I speak without a mouth and hear without ears. I have no body but come alive with the wind. What am I?");
        hashMap.put("ans", "echo");
        hashMap.put("hint1", "It's a sound phenomenon");
        hashMap.put("hint2", "It bounces off surfaces.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "The more you take, the more you leave behind. What am I?");
        hashMap.put("ans", "Footsteps");
        hashMap.put("hint1", "It's related to walking.");
        hashMap.put("hint2", "It's an impression left on the ground.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has many keys but can't open a single lock?");
        hashMap.put("ans", "keyboard");
        hashMap.put("hint1", "It's used to type on a computer.");
        hashMap.put("hint2", "It has buttons with letters and symbols.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "I am full of holes, yet I can still hold water. What am I?");
        hashMap.put("ans", "sponge");
        hashMap.put("hint1", "It's used for cleaning.");
        hashMap.put("hint2", "It absorbs liquids.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a face and two hands but no arms or legs?");
        hashMap.put("ans", "clock");
        hashMap.put("hint1", "It tells time");
        hashMap.put("hint2", "It has a round dial.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "The more you have of it, the less you see. What is it?");
        hashMap.put("ans", "Darkness");
        hashMap.put("hint1", "It's the opposite of light.");
        hashMap.put("hint2", "it's what you experience at night.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", " I am an odd number. Take away a letter, and I become even. What number am I?");
        hashMap.put("ans", "seven");
        hashMap.put("hint1", "It's a single-digit number.");
        hashMap.put("hint2", "It's considered a lucky number.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a neck but no head?");
        hashMap.put("ans", "bottle");
        hashMap.put("hint1", "It's used for holding liquids.");
        hashMap.put("hint2", "You can drink from it.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "I am taken from a mine and shut up in a wooden case, from which I am never released, and yet I am used by almost every person. What am I?");
        hashMap.put("ans", "graphite");
        hashMap.put("hint1", "It's used for writing or drawing.");
        hashMap.put("hint2", "It's a form of carbon.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "I am alive without breath, and cold as death. I am never thirsty, but always drinking. What am I?");
        hashMap.put("ans", "fish");
        hashMap.put("hint1", "It lives in water.");
        hashMap.put("hint2", "It doesn't need to drink water.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "I am tall when I am young, and short when I am old. What am I?");
        hashMap.put("ans", "candle");
        hashMap.put("hint1", "It's used for lighting.");
        hashMap.put("hint2", "It burns down as it's used.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has an eye but cannot see?");
        hashMap.put("ans", "needle");
        hashMap.put("hint1", " It's used for sewing.");
        hashMap.put("hint2", " It's small and pointed.");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a bottom but no legs to stand?");
        hashMap.put("ans", "A chair");
        hashMap.put("hint1", "It's used for sitting.");
        hashMap.put("hint2", "It has four legs.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a bottom but no legs to stand?");
        hashMap.put("ans", "A telephone");
        hashMap.put("hint1", "It's used for communication.");
        hashMap.put("hint2", "It rings when someone calls.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a bark but can't bite?");
        hashMap.put("ans", "A tree");
        hashMap.put("hint1", "It's found outdoors.");
        hashMap.put("hint2", "It's not a dog.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a tail but can't wag it?");
        hashMap.put("ans", "A coin");
        hashMap.put("hint1", "It's used as currency.");
        hashMap.put("hint2", "It has a design on one side.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has keys but can't unlock your car?");
        hashMap.put("ans", "A computer keyboard");
        hashMap.put("hint1", "It's used for typing.");
        hashMap.put("hint2", "It has keys with letters and numbers.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a shell but can't walk on the beach?");
        hashMap.put("ans", "An egg");
        hashMap.put("hint1", "It's fragile.");
        hashMap.put("hint2", "It's laid by birds.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a bed but doesn't sleep at night?");
        hashMap.put("ans", "A flowerbed");
        hashMap.put("hint1", "It's found in gardens.");
        hashMap.put("hint2", "It's full of colorful plants.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a head but no brain to think?");
        hashMap.put("ans", "A cabbage");
        hashMap.put("hint1", "It's a vegetable.");
        hashMap.put("hint2", "It's used in salads.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has branches but can't swing from trees?");
        hashMap.put("ans", "A bank");
        hashMap.put("hint1", "It deals with money.");
        hashMap.put("hint2", "It has branches in different locations.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a tongue but can't taste ice cream?");
        hashMap.put("ans", "A shoe");
        hashMap.put("hint1", "It's worn on your feet.");
        hashMap.put("hint2", "It doesn't have taste buds.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has eyes but can't see through walls?");
        hashMap.put("ans", "A potato");
        hashMap.put("hint1", "It's a vegetable.");
        hashMap.put("hint2", "It doesn't have x-ray vision.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a bed but never sleeps in it?");
        hashMap.put("ans", "A riverbed");
        hashMap.put("hint1", "It's a part of nature.");
        hashMap.put("hint2", "It's filled with water.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a beak but can't fly in the sky?");
        hashMap.put("ans", "A stapler");
        hashMap.put("hint1", "It's used for binding papers together.");
        hashMap.put("hint2", "It's a stationary object.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has buttons but can't be worn as clothes?");
        hashMap.put("ans", "A remote control");
        hashMap.put("hint1", "It's used to change channels on a TV.");
        hashMap.put("hint2", "It has various control buttons.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a screen but can't display a movie?");
        hashMap.put("ans", "A window");
        hashMap.put("hint1", "It's used to look outside.");
        hashMap.put("hint2", "It's made of glass.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a head but no hair to comb?");
        hashMap.put("ans", "A nail");
        hashMap.put("hint1", "It's used in construction.");
        hashMap.put("hint2", "You can't style it.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a tail but can't chase its own tail?");
        hashMap.put("ans", "A comet");
        hashMap.put("hint1", "It's a celestial object.");
        hashMap.put("hint2", "It travels through space.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a bed but can't sleep?");
        hashMap.put("ans", "A garden bed");
        hashMap.put("hint1", "It's found in the backyard.");
        hashMap.put("hint2", "It's filled with plants.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a mouth but can't speak words?");
        hashMap.put("ans", "A river");
        hashMap.put("hint1", "It flows with water.");
        hashMap.put("hint2", "It's found in nature.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a handle but can't be carried?");
        hashMap.put("ans", "A door");
        hashMap.put("hint1", "It's used for entering and exiting a room.");
        hashMap.put("hint2", "It has a knob.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has legs but can't walk on land?");
        hashMap.put("ans", "A table");
        hashMap.put("hint1", "It's used for placing objects on top.");
        hashMap.put("hint2", "It has four legs.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a tip but can't write a letter?");
        hashMap.put("ans", "A pencil");
        hashMap.put("hint1", "It's used for drawing and writing.");
        hashMap.put("hint2", "It has a graphite tip.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a tail but can't wag it happily?");
        hashMap.put("ans", "A kite");
        hashMap.put("hint1", "It's flown in the sky.");
        hashMap.put("hint2", "It has a tail for stability.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a roof but no walls?");
        hashMap.put("ans", "A car");
        hashMap.put("hint1", "It's used for transportation.");
        hashMap.put("hint2", "It has a roof to protect from rain.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a face but can't laugh at jokes?");
        hashMap.put("ans", "A clock");
        hashMap.put("hint1", "It tells time.");
        hashMap.put("hint2", "It has a circular face.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a trunk but can't stomp like an elephant?");
        hashMap.put("ans", "A tree");
        hashMap.put("hint1", "It's found in nature.");
        hashMap.put("hint2", "It has a sturdy trunk.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has wings but can't fly in the sky?");
        hashMap.put("ans", "A building");
        hashMap.put("hint1", "It's constructed with walls and a roof.");
        hashMap.put("hint2", "It has wing-like extensions.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a belt but can't be worn as pants?");
        hashMap.put("ans", "A conveyor belt");
        hashMap.put("hint1", "It's used for moving items.");
        hashMap.put("hint2", "It has a continuous loop.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a nose but can't smell flowers?");
        hashMap.put("ans", "A car");
        hashMap.put("hint1", "It's used for transportation.");
        hashMap.put("hint2", "It has a front grille.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a blade but can't cut vegetables?");
        hashMap.put("ans", "A fan");
        hashMap.put("hint1", "It's used for cooling.");
        hashMap.put("hint2", "It has rotating blades.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a tail but can't wag in excitement?");
        hashMap.put("ans", "A comet");
        hashMap.put("hint1", "It's a celestial object.");
        hashMap.put("hint2", "It has a long tail when visible.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a pad but can't take notes?");
        hashMap.put("ans", "A mousepad");
        hashMap.put("hint1", "It's used with a computer mouse.");
        hashMap.put("hint2", "It provides a smooth surface.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a brain but can't think like a human?");
        hashMap.put("ans", "A computer");
        hashMap.put("hint1", "It's an electronic device.");
        hashMap.put("hint2", "It processes information.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a head but can't wear a hat?");
        hashMap.put("ans", "A nail");
        hashMap.put("hint1", "It's used in construction.");
        hashMap.put("hint2", "It can't put on accessories.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has strings but can't play a musical instrument?");
        hashMap.put("ans", "A puppet");
        hashMap.put("hint1", "It's controlled by a puppeteer.");
        hashMap.put("hint2", "It's used for entertainment.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a mouth but can't eat food?");
        hashMap.put("ans", "A volcano");
        hashMap.put("hint1", "It's a geological feature.");
        hashMap.put("hint2", "It can erupt with lava.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a heart but can't feel love?");
        hashMap.put("ans", "An apple");
        hashMap.put("hint1", "It's a fruit.");
        hashMap.put("hint2", "It's often associated with teachers.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a frame but can't hold a picture?");
        hashMap.put("ans", "A bicycle");
        hashMap.put("hint1", "It's a two-wheeled vehicle.");
        hashMap.put("hint2", "It's used for transportation.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a leg but can't walk to the kitchen?");
        hashMap.put("ans", "A table");
        hashMap.put("hint1", "It's used for placing objects.");
        hashMap.put("hint2", "It has four legs.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a face but can't smile like a person?");
        hashMap.put("ans", "A clock");
        hashMap.put("hint1", "It tells time.");
        hashMap.put("hint2", "It has a circular face with numbers.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a chest but can't wear a shirt?");
        hashMap.put("ans", "A treasure chest");
        hashMap.put("hint1", "It's used for storing valuable items.");
        hashMap.put("hint2", "It's often associated with pirates.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a lid but can't cover a pot?");
        hashMap.put("ans", "A laptop");
        hashMap.put("hint1", "It's a portable computer.");
        hashMap.put("hint2", "It has a hinged lid.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has teeth but can't eat food?");
        hashMap.put("ans", "A comb");
        hashMap.put("hint1", "It's used for grooming.");
        hashMap.put("hint2", "It has teeth-like structures.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a foot but can't walk on the beach?");
        hashMap.put("ans", "A mountain");
        hashMap.put("hint1", "It's a geographical feature.");
        hashMap.put("hint2", "It's much taller than a person.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a mouth but can't speak?");
        hashMap.put("ans", "A river");
        hashMap.put("hint1", "It flows with water.");
        hashMap.put("hint2", "It's found in nature.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a heart but can't love like a human?");
        hashMap.put("ans", "An apple");
        hashMap.put("hint1", "It's a fruit.");
        hashMap.put("hint2", "It's often given to teachers.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a head but can't think like a person?");
        hashMap.put("ans", "A cabbage");
        hashMap.put("hint1", "It's a vegetable.");
        hashMap.put("hint2", "It's often used in salads.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a shell but can't walk on the beach?");
        hashMap.put("ans", "An egg");
        hashMap.put("hint1", "It's fragile.");
        hashMap.put("hint2", "It's laid by birds.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a tongue but can't taste ice cream?");
        hashMap.put("ans", "A shoe");
        hashMap.put("hint1", "It's worn on your feet.");
        hashMap.put("hint2", "It doesn't have taste buds.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a face but can't talk like a human?");
        hashMap.put("ans", "A clock");
        hashMap.put("hint1", "It tells time.");
        hashMap.put("hint2", "It has a round face.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a neck but can't wear a scarf?");
        hashMap.put("ans", "A bottle");
        hashMap.put("hint1", "It holds liquids.");
        hashMap.put("hint2", "You can take a drink from it.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a leg but can't dance like a person?");
        hashMap.put("ans", "A table");
        hashMap.put("hint1", "It's used for placing objects.");
        hashMap.put("hint2", "It has four legs.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a tongue but can't speak words?");
        hashMap.put("ans", "A shoe");
        hashMap.put("hint1", "It's worn on your feet.");
        hashMap.put("hint2", "It doesn't have vocal cords.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has eyes but can't see through walls?");
        hashMap.put("ans", "A potato");
        hashMap.put("hint1", "It's a vegetable.");
        hashMap.put("hint2", "It doesn't have x-ray vision.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has buttons but can't be worn as clothes?");
        hashMap.put("ans", "A remote control");
        hashMap.put("hint1", "It's used to change TV channels.");
        hashMap.put("hint2", "It has control buttons.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a nose but can't smell flowers?");
        hashMap.put("ans", "A car");
        hashMap.put("hint1", "It's used for transportation.");
        hashMap.put("hint2", "It has a front grille.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a handle but can't be carried?");
        hashMap.put("ans", "A door");
        hashMap.put("hint1", "It's used for entering and exiting a room.");
        hashMap.put("hint2", "It has a knob.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has wings but can't fly in the sky?");
        hashMap.put("ans", "A building");
        hashMap.put("hint1", "It's a constructed structure.");
        hashMap.put("hint2", "It has wing-like extensions.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a tail but can't wag like a dog?");
        hashMap.put("ans", "A kite");
        hashMap.put("hint1", "It's flown in the sky.");
        hashMap.put("hint2", "It has a tail for stability.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a brain but can't think like a person?");
        hashMap.put("ans", "A computer");
        hashMap.put("hint1", "It's an electronic device.");
        hashMap.put("hint2", "It processes information.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a head but can't wear a hat?");
        hashMap.put("ans", "A nail");
        hashMap.put("hint1", "It's used in construction.");
        hashMap.put("hint2", "It can't put on accessories.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has strings but can't play a musical instrument?");
        hashMap.put("ans", "A puppet");
        hashMap.put("hint1", "It's controlled by a puppeteer.");
        hashMap.put("hint2", "It's used for entertainment.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a mouth but can't eat food?");
        hashMap.put("ans", "A volcano");
        hashMap.put("hint1", "It's a geological feature.");
        hashMap.put("hint2", "It can erupt with lava.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a heart but can't feel love?");
        hashMap.put("ans", "An apple");
        hashMap.put("hint1", "It's a fruit.");
        hashMap.put("hint2", "It's often associated with teachers.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a frame but can't hold a picture?");
        hashMap.put("ans", "A bicycle");
        hashMap.put("hint1", "It's a two-wheeled vehicle.");
        hashMap.put("hint2", "It's used for transportation.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a leg but can't walk to the kitchen?");
        hashMap.put("ans", "A table");
        hashMap.put("hint1", "It's used for placing objects.");
        hashMap.put("hint2", "It has four legs.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a face but can't smile like a person?");
        hashMap.put("ans", "A clock");
        hashMap.put("hint1", "It tells time.");
        hashMap.put("hint2", "It has a circular face with numbers.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a chest but can't wear a shirt?");
        hashMap.put("ans", "A treasure chest");
        hashMap.put("hint1", "It's used for storing valuable items.");
        hashMap.put("hint2", "It's often associated with pirates.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a lid but can't cover a pot?");
        hashMap.put("ans", "A laptop");
        hashMap.put("hint1", "It's a portable computer.");
        hashMap.put("hint2", "It has a hinged lid.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has teeth but can't eat food?");
        hashMap.put("ans", "A comb");
        hashMap.put("hint1", "It's used for grooming.");
        hashMap.put("hint2", "It has teeth-like structures.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a foot but can't walk on the beach?");
        hashMap.put("ans", "A mountain");
        hashMap.put("hint1", "It's a geographical feature.");
        hashMap.put("hint2", "It's much taller than a person.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a mouth but can't speak?");
        hashMap.put("ans", "A river");
        hashMap.put("hint1", "It flows with water.");
        hashMap.put("hint2", "It's found in nature.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a head but can't think like a person?");
        hashMap.put("ans", "A cabbage");
        hashMap.put("hint1", "It's a vegetable.");
        hashMap.put("hint2", "It's often used in salads.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a face but can't talk like a human?");
        hashMap.put("ans", "A clock");
        hashMap.put("hint1", "It tells time.");
        hashMap.put("hint2", "It has a round face.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a neck but can't wear a scarf?");
        hashMap.put("ans", "A bottle");
        hashMap.put("hint1", "It holds liquids.");
        hashMap.put("hint2", "You can take a drink from it.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a face but can't talk like a human?");
        hashMap.put("ans", "A clock");
        hashMap.put("hint1", "It tells time.");
        hashMap.put("hint2", "It has a round face.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has wings but can't fly in the sky?");
        hashMap.put("ans", "A building");
        hashMap.put("hint1", "It's a constructed structure.");
        hashMap.put("hint2", "It has wing-like extensions.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has strings but can't play a musical instrument?");
        hashMap.put("ans", "A puppet");
        hashMap.put("hint1", "It's controlled by a puppeteer.");
        hashMap.put("hint2", "It's used for entertainment.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("playable", "no");
        hashMap.put("played", "no");
        hashMap.put("numStar", "0");
        hashMap.put("quiz", "What has a chest but can't wear a shirt?");
        hashMap.put("ans", "A treasure chest");
        hashMap.put("hint1", "It's used for storing valuable items.");
        hashMap.put("hint2", "It's often associated with pirates.");
        arrayList.add(hashMap);

        // Add more items to the arrayList as needed

    }
}
