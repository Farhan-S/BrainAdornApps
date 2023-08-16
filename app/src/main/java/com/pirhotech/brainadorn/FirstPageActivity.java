package com.pirhotech.brainadorn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FirstPageActivity extends AppCompatActivity {

    TextView setNickNameBtn;
    EditText nick_ed;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setNickNameBtn=findViewById(R.id.setNickNameBtn);
        nick_ed=findViewById(R.id.nick_ed);

        sharedPreferences =getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor=sharedPreferences.edit();



        setNickNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nick_ed.getText().toString().isEmpty())
                {
                    MainActivity.pop_2.start();
                    editor.putString("dpName",nick_ed.getText().toString());
                    editor.apply();
                 Intent i=   new Intent(FirstPageActivity.this,SecondPageActivity.class);
                 startActivity(i);
                 finish();
                }
                else {
                    Toast toast = Toast.makeText(FirstPageActivity.this,"Please give your nick name !",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }


    }
}