package com.example.kang.limine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
/**
 * Created by Daniel on 2016-05-08.
 */
public class Noticeboard extends Activity {
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Intent intent = new Intent(Noticeboard.this,Main2Activity.class);
            startActivity(intent);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticeboard);
        Message msg= mHandler.obtainMessage();
        mHandler.sendMessageDelayed(msg,3000);
    }
}
