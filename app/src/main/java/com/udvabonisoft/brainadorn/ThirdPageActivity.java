package com.udvabonisoft.brainadorn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ThirdPageActivity extends AppCompatActivity {

    TextView select_Wg,dpName,TandD,select_mem;
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
            }
        });

        select_mem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThirdPageActivity.this,MemoMainActivity.class));

            }
        });

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