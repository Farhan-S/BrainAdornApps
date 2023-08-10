package com.udvabonisoft.brainadorn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class ShopActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView buyNow,aquiredStar,aquiredDimond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                int hint=Integer.parseInt(sharedPreferences.getString("availableHint","5"));
                int diamond=Integer.parseInt(sharedPreferences.getString("availableDiamond","50"));
                hint+=2;
                diamond-=1;
                editor.putString("availableHint",String.valueOf(hint));
                editor.putString("availableDiamond",String.valueOf(diamond));
                editor.apply();
                aquiredStar.setText(sharedPreferences.getString("availableHint","5"));
                aquiredDimond.setText(sharedPreferences.getString("availableDiamond","50"));
            }
        });


    }
}