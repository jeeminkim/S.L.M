package com.example.kang.limine;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Service extends Activity {
    BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backPressCloseHandler = new BackPressCloseHandler(this);
        setContentView(R.layout.activity_service);
    }
    public void home(View v){
        Intent intent = new Intent (getApplication(),Main2Activity.class);
        startActivity(intent);
        finish();
    }
    public void item(View v){
        Intent intent = new Intent (getApplication(),Itemlist.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
