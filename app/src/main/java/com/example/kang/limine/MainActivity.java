package com.example.kang.limine;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends URLConnect {

    EditText editTextUserid;
    EditText editTextPassword;
    BackPressCloseHandler backPressCloseHandler;
    int check = 1;
    String myJSON;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID="id";
    private static final String TAG_PASS="pass";
    private static final String TAG_NAME="name";
    private static final String TAG_ADDRESS="address";
    private static final String TAG_PHONE="phone";
    private static final String TAG_EMAIL="email";
    NfcAdapter mAdapter;
    String loginidid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        personList = new ArrayList<HashMap<String,String>>();
        editTextUserid = (EditText) findViewById(R.id.loginid);
        editTextPassword = (EditText) findViewById(R.id.loginpass);
        loginidid = editTextUserid.getText().toString();
        backPressCloseHandler = new BackPressCloseHandler(this);
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!mAdapter.isEnabled())     {

            //NFC Setting UI
            AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
            ad.setTitle("주차권 이용시");
            ad.setMessage("설정에서 NFC을 ON 해주십시오.");
            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                }
            });
            ad.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton){
                    Toast.makeText(getApplicationContext(),"주차장을 이용하시려면 반드시 ON 해주십시오.",Toast.LENGTH_SHORT).show();
                }
            });
            ad.create();
            ad.show();
        }
    }
    public void login(View v) {

        switch(check){
            case 1:
                getData(urllogin);
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "정확한 정보를 입력해 주세요", Toast.LENGTH_SHORT).show();
                check = 1 ;
                break;
        }

    }
    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            String loginid = editTextUserid.getText().toString();
            String loginpass = editTextPassword.getText().toString();

            for(int i=0;i<peoples.length();i++) {

                JSONObject c = peoples.getJSONObject(i);

                String id = c.getString(TAG_ID);
                String pass = c.getString(TAG_PASS);
                String name = c.getString(TAG_NAME);
                String email = c.getString(TAG_EMAIL);
                String phone = c.getString(TAG_PHONE);
                String address = c.getString(TAG_ADDRESS);


                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_ID, id);
                persons.put(TAG_PASS, pass);
                persons.put(TAG_NAME, name);
                persons.put(TAG_EMAIL,email);
                persons.put(TAG_PHONE,phone);
                persons.put(TAG_ADDRESS,address);

                personList.add(persons);

                if(loginid.equals(id) && loginpass.equals(pass)){
                    sql.execSQL("insert into "+TABLENAME+" (num) values (1)");
                    sql.execSQL("delete from "+TABLENAME+" where num = 1");
                    sql.execSQL("insert into "+TABLENAME+" (name, id, email, phone, address, num) values ('"+name+"','"+id+"','"+email+"','"+phone+"','"+address+"',1)");
                    sql.execSQL("drop table " + BUYNAME);
                    sql.execSQL("drop table " +PARKNAME);
                    sql.execSQL("create table "+PARKNAME+" (id text,inh int, inm int, ins int, outh int, outm int, outs int)");
                    sql.execSQL("create table "+BUYNAME+" (price int)");

                    Intent intent = new Intent (getApplicationContext(),Main2Activity.class);
                    startActivity(intent);
                    persons.clear();
                    Toast.makeText(getApplicationContext(),name+"님 환영합니다", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    check =2 ;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void join(View v) {
        Intent intent2 = new Intent(getApplicationContext(), Admit.class);
        startActivity(intent2);
    }
    public void searchid(View v) {
        Intent intent2 = new Intent(getApplicationContext(), Searchid.class);
        startActivity(intent2);
    }
    public void searchpass(View v) {
        Intent intent2 = new Intent(getApplicationContext(), Searchpass.class);
        startActivity(intent2);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}
