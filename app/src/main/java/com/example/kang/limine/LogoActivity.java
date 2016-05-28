package com.example.kang.limine;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Daniel on 2016-05-07.
 */

public class LogoActivity extends Activity {
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Intent intent = new Intent(LogoActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        Message msg= mHandler.obtainMessage();
        mHandler.sendMessageDelayed(msg,3000);
    }
}
