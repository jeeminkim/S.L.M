package com.example.kang.limine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class Admit extends URLConnect {
    CheckBox c1, c2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admit);

        c1 = (CheckBox)findViewById(R.id.checkBox1);
        c2 = (CheckBox)findViewById(R.id.checkBox2);

        c1.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                go(v);
            }
        });
        c2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                go(v);
            }
        });
    }
    public void go(View view){
        if(!c1.isChecked()){
            if(!c2.isChecked()) {
                Toast.makeText(getApplicationContext(), "모두 동의 하시면 자동으로 넘어갑니다.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "모두 동의 하시면 자동으로 넘어갑니다.", Toast.LENGTH_LONG).show();
            }
        }
        else if(!c2.isChecked()){
            if(!c1.isChecked()){
                Toast.makeText(getApplicationContext(), "모두 동의 하시면 자동으로 넘어갑니다.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "모두 동의 하시면 자동으로 넘어갑니다.", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Intent intent1 = new Intent (getApplicationContext(),Join.class);
            startActivity(intent1);
            finish();
        }
    }

}