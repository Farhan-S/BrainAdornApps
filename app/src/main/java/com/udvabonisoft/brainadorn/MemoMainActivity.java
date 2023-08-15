package com.udvabonisoft.brainadorn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Random;

public class MemoMainActivity extends AppCompatActivity {

    ImageView green, red, blue, yellow,dpPic;

    TextView dpName,aquiredStar, aquiredDimond, scoreTv,hintBtn1, hintBtn2,timer3sec, moreHint,moreDiamond,highScoreTv;
    Animation click_anim;
    MediaPlayer g_audio, r_audio, b_audio, y_audio, wrong;
    Button playBtn;
    AdView mAdView;

    int score=0;
    RelativeLayout topRelativeLay, hint1,hint2,gameLay;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RewardedAd rewardedAd;

    boolean isLoading;
    static boolean isLoaded;

    String[] buttonColors = {"green","red","blue","yellow"};

    ArrayList gamePattern = new ArrayList();
    ArrayList userPattern= new ArrayList();

    Handler handler= new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorise_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //for banner add
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadRewardedAd();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //banner add ses


        green= findViewById(R.id.green);
        red=findViewById(R.id.red);
        blue=findViewById(R.id.blue);
        yellow=findViewById(R.id.yellow);
        scoreTv=findViewById(R.id.score);
        hintBtn1=findViewById(R.id.hintBtn1);
        hintBtn2=findViewById(R.id.hintBtn2);
        topRelativeLay=findViewById(R.id.topRelativeLay);
        hint1=findViewById(R.id.hint1);
        hint2=findViewById(R.id.hint2);
        gameLay=findViewById(R.id.gameLay);
        timer3sec=findViewById(R.id.timer3sec);
        moreHint=findViewById(R.id.moreHint);
        moreDiamond=findViewById(R.id.moreDiamond);
        highScoreTv=findViewById(R.id.highScoreTv);




        dpName=findViewById(R.id.dpName);
        dpPic=findViewById(R.id.dpPic);
        aquiredStar=findViewById(R.id.aquiredStar);
        aquiredDimond=findViewById(R.id.aquiredDimond);


        g_audio= MediaPlayer.create(MemoMainActivity.this, R.raw.green);
        r_audio= MediaPlayer.create(MemoMainActivity.this, R.raw.red);
        b_audio= MediaPlayer.create(MemoMainActivity.this, R.raw.blue);
        y_audio= MediaPlayer.create(MemoMainActivity.this, R.raw.yellow);
        wrong=MediaPlayer.create(MemoMainActivity.this, R.raw.wrong);

        click_anim= AnimationUtils.loadAnimation(MemoMainActivity.this, R.anim.click_anim);


        sharedPreferences=getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor=sharedPreferences.edit();



        String dpPicName=sharedPreferences.getString("dpPicName","img1");
        int resId = getResources().getIdentifier(dpPicName, "drawable", getPackageName());
        dpPic.setImageResource(resId);

        dpName.setText(sharedPreferences.getString("dpName","JOHN WICK").toUpperCase());


        aquiredStar.setText(sharedPreferences.getString("availableHint","5"));
        aquiredDimond.setText(sharedPreferences.getString("availableDiamond","50"));
        highScoreTv.setText("HIGH SCORE \n\n "+sharedPreferences.getInt("highScore",score));


        hint1.setVisibility(View.GONE);
        hint2.setVisibility(View.GONE);
        gameLay.setVisibility(View.GONE);
        scoreTv.setVisibility(View.GONE);
        timer3sec.setVisibility(View.GONE);
        highScoreTv.setVisibility(View.GONE);



        playButtonDialog();

        moreHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemoMainActivity.this,ShopActivity.class));
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                green.startAnimation(click_anim);
                g_audio.start();
                userPattern.add("green");
                checkAns(userPattern.size()-1);
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red.startAnimation(click_anim);
                r_audio.start();
                userPattern.add("red");
                checkAns(userPattern.size()-1);
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blue.startAnimation(click_anim);
                b_audio.start();
                userPattern.add("blue");
                checkAns(userPattern.size()-1);
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellow.startAnimation(click_anim);
                y_audio.start();
                userPattern.add("yellow");
                checkAns(userPattern.size()-1);
            }
        });


        hintBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //star reduce
                int h1= Integer.parseInt(sharedPreferences.getString("availableHint","5"));
                if(h1>=1)
                {
                    hintDialog(1500);
                    h1-=1;
                    editor.putString("availableHint",String.valueOf(h1));
                    editor.apply();
                    aquiredStar.setText(sharedPreferences.getString("availableHint","5"));

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not enough star left",Toast.LENGTH_SHORT).show();
                }

            }
        });
        hintBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int h2= Integer.parseInt(sharedPreferences.getString("availableHint","5"));
                if(h2>=2)
                {
                    hintDialog(3000);
                    h2-=2;
                    editor.putString("availableHint",String.valueOf(h2));
                    editor.apply();
                    aquiredStar.setText(sharedPreferences.getString("availableHint","5"));

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not enough star left",Toast.LENGTH_SHORT).show();
                }
            }
        });

        moreDiamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buyDiamond();

            }
        });


    }



    @Override
    public void onResume(){
        super.onResume();
        aquiredStar.setText(sharedPreferences.getString("availableHint","5"));
        aquiredDimond.setText(sharedPreferences.getString("availableDiamond","50"));

    }

    void nextSequence()
    {
        userPattern.clear();

        Random ran= new Random();
        int a;
        a=ran.nextInt(4);
        if(buttonColors[a]=="green")
        {
            gamePattern.add(buttonColors[a]);
            green.startAnimation(click_anim);
            g_audio.start();
        }
        else if(buttonColors[a]=="red")
        {
            gamePattern.add(buttonColors[a]);
            red.startAnimation(click_anim);
            r_audio.start();
        }
        else if(buttonColors[a]=="blue")
        {
            gamePattern.add(buttonColors[a]);
            blue.startAnimation(click_anim);
            b_audio.start();
        }
        else if(buttonColors[a]=="yellow")
        {
            gamePattern.add(buttonColors[a]);
            yellow.startAnimation(click_anim);
            y_audio.start();
        }
    }


    void checkAns(int givenIndex)
    {
        if(gamePattern.isEmpty())
        {
            gameOver();
        }
        else if(gamePattern.size()<userPattern.size())
        {
            gameOver();
        }
        else if(gamePattern.get(givenIndex)==userPattern.get(givenIndex))
        {
            if(gamePattern.size()==userPattern.size())
            {
                score++;
                scoreTv.setText(""+score);

                if(score>sharedPreferences.getInt("highScore",score))
                {
                    editor.putInt("highScore", score);
                    editor.apply();
                    highScoreTv.setText("HIGH SCORE \n\n "+sharedPreferences.getInt("highScore",score));
                }



                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextSequence();
                    }
                },1000);

            }
        }
        else
        {
            gameOver();
        }
    }


    void gameOver()
    {
        wrong.start();
        gamePattern.clear();
        gameOverDialog();
    }

    private void playButtonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        View customLayout = getLayoutInflater().inflate(R.layout.memorise_play_button, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();

        // Set up your custom UI elements and handle their interactions if needed
        TextView backButton = customLayout.findViewById(R.id.backBtn);
        TextView playButton = customLayout.findViewById(R.id.playBtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                dialog.dismiss();

                //timer
                new CountDownTimer(4000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timer3sec.setVisibility(View.VISIBLE);
                        timer3sec.setText("" + millisUntilFinished / 1000);
                    }

                    public void onFinish() {


                        hint1.setVisibility(View.VISIBLE);
                        hint2.setVisibility(View.VISIBLE);
                        gameLay.setVisibility(View.VISIBLE);
                        scoreTv.setVisibility(View.VISIBLE);
                        highScoreTv.setVisibility(View.VISIBLE);
                        timer3sec.setVisibility(View.GONE);

                        nextSequence();

                    }
                }.start();
                //timer ses

            }
        });
        dialog.show();
    }


    private void gameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        View customLayout = getLayoutInflater().inflate(R.layout.memorise_gameover_dialog, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();

        // Set up your custom UI elements and handle their interactions if needed
        TextView exitButton = customLayout.findViewById(R.id.exitBtn);
        TextView playAgainButton = customLayout.findViewById(R.id.playAgainBtn);
        TextView scoreText= customLayout.findViewById(R.id.gameOverAlert);

        scoreText.setText("GAME OVER\n\n SCORE "+score);


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                dialog.dismiss();

                //timer
                new CountDownTimer(4000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timer3sec.setVisibility(View.VISIBLE);
                        hint1.setVisibility(View.GONE);
                        hint2.setVisibility(View.GONE);
                        gameLay.setVisibility(View.GONE);
                        scoreTv.setVisibility(View.GONE);
                        highScoreTv.setVisibility(View.GONE);
                        timer3sec.setText("" + millisUntilFinished / 1000);
                    }

                    public void onFinish() {


                        hint1.setVisibility(View.VISIBLE);
                        hint2.setVisibility(View.VISIBLE);
                        gameLay.setVisibility(View.VISIBLE);
                        scoreTv.setVisibility(View.VISIBLE);
                        highScoreTv.setVisibility(View.VISIBLE);
                        timer3sec.setVisibility(View.GONE);


                        score=0;
                        scoreTv.setText(""+score);
                        nextSequence();

                    }
                }.start();
                //timer ses


            }
        });
        dialog.show();
    }


    private void hintDialog(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        View customLayout = getLayoutInflater().inflate(R.layout.memorise_hint_dialog, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();

        // Set up your custom UI elements and handle their interactions if needed
        TextView dismissButton = customLayout.findViewById(R.id.dismissBtn);
        TextView hintBox = customLayout.findViewById(R.id.hintBox);


        hintBox.setText(""+gamePattern);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, a);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        backPressAlertDialog();
    }
    private void backPressAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        View customLayout = getLayoutInflater().inflate(R.layout.alert_dialog_onbackpress, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();

        // Set up your custom UI elements and handle their interactions if needed
        TextView positiveButton = customLayout.findViewById(R.id.posBtn);
        TextView negativeButton = customLayout.findViewById(R.id.negBtn);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                aquiredStar.setText(sharedPreferences.getString("availableHint","5"));
                aquiredDimond.setText(sharedPreferences.getString("availableDiamond","50"));
            }
        });
        dialog.show();
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
                                        MemoMainActivity.this, "Try again !", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;

                        Toast.makeText(MemoMainActivity.this, "Closed Ads !", Toast.LENGTH_SHORT)
                                .show();
                        isLoaded=false;
                        // Preload the next rewarded ad.
                        MemoMainActivity.this.loadRewardedAd();
                    }
                });
        Activity activityContext = MemoMainActivity.this;
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
                            MemoMainActivity.this.isLoading = false;
                            Toast.makeText(MemoMainActivity.this, "Failed to show an Ad !", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            MemoMainActivity.this.rewardedAd = rewardedAd;

                            MemoMainActivity.this.isLoading = false;
                            isLoaded=true;
                        }
                    });
        }
    }




}