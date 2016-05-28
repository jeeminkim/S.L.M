package com.example.kang.limine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Join extends URLConnect {
    BackPressCloseHandler backPressCloseHandler;
    EditText editid,editpass1,editpass2,editname,editemail,editphone,editcode;
    Button button;
    String codeok = "ACD8989";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backPressCloseHandler = new BackPressCloseHandler(this);
        setContentView(R.layout.activity_join);

        button = (Button)findViewById(R.id.buttonok);
        editid = (EditText) findViewById(R.id.joinid);
        editpass1 = (EditText) findViewById(R.id.joinpass1);
        editpass2 = (EditText) findViewById(R.id.joinpass2);
        editname = (EditText) findViewById(R.id.joinname);
        editemail = (EditText) findViewById(R.id.joinemail);
        editphone = (EditText) findViewById(R.id.joinphone);
        editcode = (EditText) findViewById(R.id.codein);


    }

    public void joinok(View view) {

        String id = editid.getText().toString();
        String pass = editpass1.getText().toString();
        String pass2 = editpass2.getText().toString();
        String name = editname.getText().toString();
        String email = editemail.getText().toString();
        String phone = editphone.getText().toString();
        String codein = editcode.getText().toString();

        if (id.length() < 4 || id.length() > 15) {
            Toast.makeText(getApplicationContext(), "아이디를 다시 입력해 주세요", Toast.LENGTH_LONG).show();
        } else if (pass.length() < 6 || pass.length() > 20) {
            Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해 주세요", Toast.LENGTH_LONG).show();
        } else if (phone.length() != 11) {
            Toast.makeText(getApplicationContext(), "핸드폰번호를 다시 입력해 주세요", Toast.LENGTH_LONG).show();
        } else if (email.length() < 6) {
            Toast.makeText(getApplicationContext(), "이메일을 다시 입력해 주세요", Toast.LENGTH_LONG).show();
        }else if (name.length() <2 || name.length() >5){
            Toast.makeText(getApplicationContext(), "이름을 다시 입력해 주세요", Toast.LENGTH_LONG).show();
        } else {
            if (pass.equals(pass2)) {
                if(codein.equals(codeok)){
                AddData(name, id, pass, email, phone);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"인증번호를 잘못 입력하셨습니다",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "비밀번호를 동일하게 입력해 주세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void AddData(String name ,String id ,String pass ,String email,String phone) {

        class addData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Join.this, "잠시만 기다려 주세요", null, true, true);
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
                    String name= (String) params[0];
                    String id = (String) params[1];
                    String pass = (String) params[2];
                    String email = (String) params[3];
                    String phone = (String) params[4];

                    String link = urljoin;
                    String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                    data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");


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
        task.execute(name, id, pass, email, phone);
    }
    private void AddData1(String email) {

        class addData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Join.this, "잠시만 기다려 주세요", null, true, true);
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
                    String email= (String) params[0];

                    String link = urlcode;
                    String data  = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

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
        task.execute(email);
    }
    public void codesend (View v){
        String emailcode = editemail.getText().toString();
        AddData1(emailcode);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}