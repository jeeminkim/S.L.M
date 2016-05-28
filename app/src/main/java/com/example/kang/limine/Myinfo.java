package com.example.kang.limine;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Myinfo extends Parking {


    ArrayList<HashMap<String, String>> personList;
    TextView textView1, textView2, textView3, textView4,textView5;
    EditText editText1, editText2;
    String Cid, Cname, Cemail, Cphone, Caddress;
    String sendid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personList = new ArrayList<HashMap<String, String>>();
        setContentView(R.layout.activity_myinfo);
        editText1 = (EditText) findViewById(R.id.cpass1);
        editText2 = (EditText) findViewById(R.id.cpass2);
        textView1 = (TextView) findViewById(R.id.viewid);
        textView2 = (TextView) findViewById(R.id.viewname);
        textView3 = (TextView) findViewById(R.id.viewemail);
        textView4 = (TextView) findViewById(R.id.viewphone);
        textView5 = (TextView) findViewById(R.id.viewadd);

        Cursor c = sql.rawQuery("select name, id, email, phone, address From " + TABLENAME, null);
        int count = c.getCount();
        for (int i = 0; i < count; i++) {
            c.moveToNext();
            Cname = c.getString(0);
            Cphone = c.getString(3);
            Cid = c.getString(1);
            Cemail = c.getString(2);
            Caddress = c.getString(4);

        }
        textView1.append(Cid);
        textView2.append(Cname);
        textView3.append(Cemail);
        textView4.append(Cphone);
        textView5.append(Caddress);
    }

    public void cok(View v) {
        sendid = textView1.getText().toString();
        String pass1 = editText1.getText().toString();
        String pass2 = editText2.getText().toString();


        if (pass1.length() < 6 || pass2.length() <6){
            Toast.makeText(getApplicationContext(), "비밀번호를 6자리 이상 입력해주세요", Toast.LENGTH_LONG).show();
        }else if (pass1.length() >20 || pass2.length() > 20){
            Toast.makeText(getApplicationContext(), "비밀번호를 20자 이하로 입력해주세요", Toast.LENGTH_LONG).show();
        }
        else if(pass1.equals(pass2)){
            AddDatapass(sendid, pass1);
            AlertDialog dialog = createDialogBox();
            dialog.show();
        }
        else{
            Toast.makeText(getApplicationContext(), "비밀번호를 동일하게 입력해주세요",Toast.LENGTH_SHORT).show();
        }
    }

    public void changeadd(View v) {
        Intent intent = new Intent(getApplicationContext(), Changeadd.class);
        startActivity(intent);
        finish();
    }



    private void AddDatapass(String id, String pass) {

        class addData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Myinfo.this, "잠시만 기다려 주세요", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPreExecute();
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                try {

                    String id = (String) params[0];
                    String pass = (String) params[1];

                    String link = urlinfo;
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");


                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;


                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("message : " + e.getMessage());
                }
            }
        }
        addData task = new addData();
        task.execute(id, pass);
    }

    private AlertDialog createDialogBox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("비밀번호를 변경하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다. \n변경된 비밀번호로 다시 로그인 해주세요.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }
    public void ccan(View v){
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        startActivity(intent);
        finish();
    }
}


