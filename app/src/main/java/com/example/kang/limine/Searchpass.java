package com.example.kang.limine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class Searchpass extends URLConnect {

    EditText editText1, editText2, editText3;
    private static final String TAG_RESULTS="result";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PASS = "pass";
    private static final String TAG_ID= "id";

    String myJSON1;
    JSONArray peoples1 = null;
    ArrayList<HashMap<String, String>> personList1;
    int check = 1 ;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchpass);
        personList1 = new ArrayList<HashMap<String,String>>();
        textView = (TextView)findViewById(R.id.findpass);
        editText1 = (EditText)findViewById(R.id.searchpassid);
        editText2 = (EditText)findViewById(R.id.searchpassname);
        editText3 = (EditText)findViewById(R.id.searchpassphone);
    }
    public void findpass(View v) {
        switch(check){
            case 1:
                getData(urlsearchpass);
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "정확한 정보를 입력해 주세요", Toast.LENGTH_LONG).show();
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
                myJSON1=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON1);
            peoples1 = jsonObj.getJSONArray(TAG_RESULTS);
            String Fid = editText1.getText().toString();
            String Fname = editText2.getText().toString();
            String Fphone= editText3.getText().toString();
            String Fpass = "resetpass";
            for(int i=0;i<peoples1.length();i++) {

                JSONObject c = peoples1.getJSONObject(i);

                String phone = c.getString(TAG_PHONE);
                String name = c.getString(TAG_NAME);
                String id = c.getString(TAG_ID);
                String pass = c.getString(TAG_PASS);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_PHONE, phone);
                persons.put(TAG_NAME, name);
                persons.put(TAG_ID,id);
                persons.put(TAG_PASS,pass);

                personList1.add(persons);

                if(Fname.equals(name) && Fphone.equals(phone) && Fid.equals(id)){
                    AddData(Fname,Fid,Fpass);
                    println(" 비밀번호가' "+Fpass+" '로 초기화 되었습니다.\n로그인 후 비밀번호를 변경해 주세요.");
                }else{
                    check =2 ;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    private void println(String data){
        textView.append(data + "\n");
    }
    public void gologin(View v){
        Intent intent = new Intent (getApplication(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void gofindid(View v) {
        Intent intent = new Intent(getApplication(), Searchid.class);
        startActivity(intent);
        finish();
    }
    private void AddData(String name,String id,String pass) {

        class addData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Searchpass.this, "잠시만 기다려 주세요", null, true, true);
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
                    String name =(String) params[0];
                    String id = (String) params[1];
                    String pass = (String) params[2];


                    String link = urlsearchpass;
                    String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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
        task.execute(name, id, pass);
    }

}
