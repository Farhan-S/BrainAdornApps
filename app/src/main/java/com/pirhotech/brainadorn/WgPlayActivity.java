package com.pirhotech.brainadorn;

import static com.pirhotech.brainadorn.WgFirstActivity.arrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class WgPlayActivity extends AppCompatActivity {

    TextView timer3sec,timer60sec,quizTv,submitBtn,dpName,aquiredStar,aquiredDimond,moreHint;
    ImageView dpPic;
    EditText wg_ed;

    HashMap<String, String> hashMap = new HashMap<>();

    public static String numStar;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    RelativeLayout hint1,hint2,ans;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wg_play);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        dpName=findViewById(R.id.dpName);
        dpPic=findViewById(R.id.dpPic);
        timer3sec=findViewById(R.id.timer3sec);
        timer60sec=findViewById(R.id.timer60sec);
        quizTv=findViewById(R.id.quizTv);
        wg_ed=findViewById(R.id.wg_ed);
        submitBtn=findViewById(R.id.submitBtn);
        aquiredStar=findViewById(R.id.aquiredStar);
        aquiredDimond=findViewById(R.id.aquiredDimond);
        hint1=findViewById(R.id.hint1);
        hint2=findViewById(R.id.hint2);
        ans=findViewById(R.id.ans);
        moreHint=findViewById(R.id.moreHint);

        timer60sec.setVisibility(View.GONE);
        quizTv.setVisibility(View.GONE);
        wg_ed.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);
        hint1.setVisibility(View.GONE);
        hint2.setVisibility(View.GONE);
        ans.setVisibility(View.GONE);

        sharedPreferences=getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor=sharedPreferences.edit();

        String dpPicName=sharedPreferences.getString("dpPicName","img1");
        int resId = getResources().getIdentifier(dpPicName, "drawable", getPackageName());
        dpPic.setImageResource(resId);

        dpName.setText(sharedPreferences.getString("dpName","JOHN WICK").toUpperCase());


        aquiredStar.setText(sharedPreferences.getString("availableHint","5"));
        aquiredDimond.setText(sharedPreferences.getString("availableDiamond","50"));



        int lvl=sharedPreferences.getInt("lvlNum",0);
        loadArrayListFromStorage();


        HashMap<String, String> hashMap = arrayList.get(lvl);
        String quiz = hashMap.get("quiz");
        String hint1n=hashMap.get("hint1");
        String hint2n=hashMap.get("hint2");
        quizTv.setText(quiz);

        hint2.setVisibility(View.GONE);

        hint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hint1.startAnimation(MainActivity.click_anim);

                int h1= Integer.parseInt(sharedPreferences.getString("availableHint","5"));
                if(h1>=1)
                {
                    MainActivity.long_suspense_2.start();
                    hint1.setVisibility(View.GONE);
                    h1-=1;
                    editor.putString("availableHint",String.valueOf(h1));
                    editor.apply();
                    aquiredStar.setText(sharedPreferences.getString("availableHint","5"));

                    new CountDownTimer(10000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            quizTv.setText(hint1n);
                        }

                        public void onFinish() {
                            hint2.setVisibility(View.VISIBLE);
                            quizTv.setText(quiz);
                        }
                    }.start();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not enough star left",Toast.LENGTH_SHORT).show();
                }


            }
        });

        hint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint2.startAnimation(MainActivity.click_anim);

                int h2= Integer.parseInt(sharedPreferences.getString("availableHint","5"));
                if(h2>=2)
                {
                    MainActivity.long_suspense_3.start();
                    hint2.setVisibility(View.GONE);
                    ans.setVisibility(View.VISIBLE);
                    h2-=2;
                    editor.putString("availableHint",String.valueOf(h2));
                    editor.apply();
                    aquiredStar.setText(sharedPreferences.getString("availableHint","5"));

                    new CountDownTimer(15000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            quizTv.setText(hint2n);
                        }

                        public void onFinish() {
                            quizTv.setText(quiz);
                        }
                    }.start();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not enough star left",Toast.LENGTH_SHORT).show();
                }

            }
        });

        ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int diamond=Integer.parseInt(sharedPreferences.getString("availableDiamond","50"));
                if(diamond>=2)
                {
                    ans.setVisibility(View.GONE);
                    HashMap<String, String> hashMap = arrayList.get(lvl);
                    String ans = hashMap.get("ans");
                    quizTv.setText(ans);

                    MainActivity.flashback.start();
                    diamond-=2;
                    editor.putString("availableDiamond",String.valueOf(diamond));
                    editor.apply();
                    aquiredDimond.setText(sharedPreferences.getString("availableDiamond","50"));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not enough diamond !",Toast.LENGTH_SHORT).show();
                    MainActivity.glass_breaking.start();
                }
            }
        });

        moreHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.reloading.start();
                startActivity(new Intent(WgPlayActivity.this,ShopActivity.class));
            }
        });


        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer3sec.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer3sec.setVisibility(View.GONE);
                timer60sec.setVisibility(View.VISIBLE);
                quizTv.setVisibility(View.VISIBLE);
                wg_ed.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
                hint1.setVisibility(View.VISIBLE);
//                hint2.setVisibility(View.VISIBLE);

                CountDownTimer countDownTimer= new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timer60sec.setText("" + millisUntilFinished / 1000);


                        int time =Integer.parseInt(timer60sec.getText().toString());

                       if(time>40)
                       {
                           numStar="3";
                       } else if (time>20) {
                           numStar="2";

                       } else if (time>=0) {
                           numStar="1";
                       }
                       if(time<20)
                       {
                           MainActivity.heartbeat.start();
                       }

                    }

                    public void onFinish() {
                        HashMap<String, String> hashMap = arrayList.get(lvl);
                        String ans = hashMap.get("ans");
                        if(getResult(ans))
                        {
                            finalResultGame(true,numStar);
                        }
                        else
                        {
                            finalResultGame(false,numStar);
                        }
                    }
                }.start();

                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HashMap<String, String> hashMap = arrayList.get(lvl);
                        String ans = hashMap.get("ans");
                        if(getResult(ans))
                        {
                            countDownTimer.cancel();
                            finalResultGame(true,numStar);
                        }
                        else
                        {
                            countDownTimer.cancel();
                            finalResultGame(false,numStar);
                        }
                    }
                });
            }
        }.start();

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public boolean getResult(String ans)
    {
        if(wg_ed.getText().toString().toLowerCase().equals(ans))
        {
            return true;
        }
        else
        {
            return false;
        }
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

    private void finalResultGame(boolean res, String numStar) {

        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialogResult);
        View customLayout = getLayoutInflater().inflate(R.layout.activity_result_show, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();

        // Set up your custom UI elements and handle their interactions if needed

        TextView lvlIndicator = customLayout.findViewById(R.id.lvlIndicator);
        TextView skiptxt = customLayout.findViewById(R.id.skiptxt);
        TextView skipLvl = customLayout.findViewById(R.id.skipLvl);
        TextView nextLvl = customLayout.findViewById(R.id.nextLvl);
        TextView onSuccessReplay = customLayout.findViewById(R.id.onSuccessReplay);
        ImageView star1=customLayout.findViewById(R.id.star1);
        ImageView star2=customLayout.findViewById(R.id.star2);
        ImageView star3=customLayout.findViewById(R.id.star3);
        ImageView skipDiamondstar3=customLayout.findViewById(R.id.skipDiamond);

        if(res)
        {
            lvlIndicator.setText("COMPLETED");
            skiptxt.setVisibility(View.GONE);
            skipLvl.setVisibility(View.GONE);
            skipDiamondstar3.setVisibility(View.GONE);

            HashMap<String, String> hashMap1 = arrayList.get(sharedPreferences.getInt("lvlNum",0));
            HashMap<String, String> hashMap2 = arrayList.get(sharedPreferences.getInt("lvlNum",0)+1);

            hashMap=new HashMap<>();
            hashMap.put("ans",hashMap1.get("ans"));
            hashMap.put("numStar",numStar);
            hashMap.put("quiz",hashMap1.get("quiz"));
            hashMap.put("played","yes");
            hashMap.put("playable","yes");
            hashMap.put("hint1",hashMap1.get("hint1"));
            hashMap.put("hint2",hashMap1.get("hint2"));
            arrayList.set(sharedPreferences.getInt("lvlNum",0),hashMap);

            hashMap=new HashMap<>();
            hashMap.put("ans",hashMap2.get("ans"));
            hashMap.put("numStar","0");
            hashMap.put("quiz",hashMap2.get("quiz"));
            hashMap.put("played","no");
            hashMap.put("playable","yes");
            hashMap.put("hint1",hashMap2.get("hint1"));
            hashMap.put("hint2",hashMap2.get("hint2"));
            arrayList.set(sharedPreferences.getInt("lvlNum",0)+1,hashMap);

            deleteArrayListFile();
            saveArrayListToStorage();

            if(numStar.equals("3"))
            {
                star1.setImageResource(R.drawable.levelstarsuccess);
                star2.setImageResource(R.drawable.levelstarsuccess);
                star3.setImageResource(R.drawable.levelstarsuccess);
                MainActivity.crowd_booing.start();
            } else if (numStar.equals("2")) {
                star1.setImageResource(R.drawable.levelstarsuccess);
                star2.setImageResource(R.drawable.levelstarsuccess);
                MainActivity.kids_cheering.start();
            } else if (numStar.equals("1")) {
                star1.setImageResource(R.drawable.levelstarsuccess);
                MainActivity.people_clapping.start();
            }
        }
        else
        {
            onBackPressed();
            nextLvl.setVisibility(View.GONE);
            skiptxt.setVisibility(View.VISIBLE);
            skipLvl.setVisibility(View.VISIBLE);
            skipDiamondstar3.setVisibility(View.VISIBLE);
            lvlIndicator.setText("FAILED");
            Random ran= new Random();
            int a;
            a=ran.nextInt(8);
            if(a==1)
            {
                MainActivity.fail_1.start();
            }
            else if(a==2)
            {
                MainActivity.fail_2.start();
            }
            else if(a==3)
            {
                MainActivity.wrong_answer.start();
            }
            else if(a==4)
            {
                MainActivity.fart_1.start();
            }
            else if(a==5)
            {
                MainActivity.fart_1.start();
            }

            else
            {
                MainActivity.wrong_answer.start();
            }
        }

        onSuccessReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(WgPlayActivity.this,WgPlayActivity.class);
                finish();
                startActivity(i);
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
        nextLvl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle negative button click
                // For example, dismiss the dialog
                editor.putInt("lvlNum",sharedPreferences.getInt("lvlNum",0)+1);
                editor.apply();
                Intent i =new Intent(WgPlayActivity.this,WgPlayActivity.class);
                finish();
                startActivity(i);
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

        skipLvl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle negative button click
                // For example, dismiss the dialog
                int diamond= Integer.parseInt(sharedPreferences.getString("availableDiamond","50"));
                if(diamond>=4)
                {
                    diamond-=4;
                    editor.putString("availableDiamond",String.valueOf(diamond));
                    editor.putInt("lvlNum",sharedPreferences.getInt("lvlNum",0)+1);
                    editor.apply();
                    Intent i =new Intent(WgPlayActivity.this,WgPlayActivity.class);
                    finish();
                    startActivity(i);
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
                else
                {
                    Toast.makeText(getApplicationContext(),"Not enough diamond to skip",Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void deleteArrayListFile() {
        File file = new File(getFilesDir(), "myArrayListFile");
        file.delete();


    }


    private void saveArrayListToStorage() {
        try {
            FileOutputStream fileOutputStream = openFileOutput("myArrayListFile", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(WgFirstActivity.arrayList);
            objectOutputStream.close();
            fileOutputStream.close();
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadArrayListFromStorage() {
        try {
            FileInputStream fileInputStream = openFileInput("myArrayListFile");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            WgFirstActivity.arrayList = (ArrayList<HashMap<String, String>>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}