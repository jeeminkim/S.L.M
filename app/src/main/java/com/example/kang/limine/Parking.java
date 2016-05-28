package com.example.kang.limine;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static android.nfc.NfcAdapter.getDefaultAdapter;

public class Parking extends URLConnect {
    String myJSON;
    JSONArray peoples = null;

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    String nfcid, id, myResult;
    TextView tagDesc;

    String Tname,Tid;
    TextView textView;
    Integer intime = 0 ;
    Integer outtime = 0;
    Integer sumb =0 ;
    Integer parkm = 0 ;
    int inh = 0 ;
    int inm = 0 ;
    int ins = 0 ;
    int check = 0;
    int sum =0 ;



    private static final String TAG_RESULTS="result";
    private static final String TAG_ID="id";
    private static final String TAG_INH="inh";
    private static final String TAG_INM="inm";
    private static final String TAG_INS="ins";
    private static final String TAG_OUTH="outh";
    private static final String TAG_OUTM="outm";
    private static final String TAG_OUTS="outs";

    String hview, mview, sview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        tagDesc = (TextView) findViewById(R.id.tagDesc);
        textView = (TextView) findViewById(R.id.textViewa);
        nfcAdapter = getDefaultAdapter(this);
        Cursor c = sql.rawQuery("select * From " + TABLENAME, null);
        int count = c.getCount();
        for (int i = 0; i < count; i++) {
            c.moveToNext();
            Tname = c.getString(0);
            Tid = c.getString(1);
        }
        Cursor c1 = sql.rawQuery("select price From " + BUYNAME, null);
        int count1 = c1.getCount();
        for (int i = 0; i < count1; i++) {
            c1.moveToNext();
            check = c1.getInt(0);
            sum = sum + check;
        }
        Cursor c2 = sql.rawQuery("select * From " + PARKNAME, null);
        int count2 = c2.getCount();
        for (int i = 0; i < count2; i++) {
            c2.moveToNext();
            inh = c2.getInt(1);
            inm = c2.getInt(2);
            ins = c2.getInt(3);
        }
        tagDesc.setText("입장 시간 :" + inh + "시 " + inm + "분 " + ins + "초 \n구매금액 :" + sum + "원");
        intime = inh * 3600 + inm * 60 + ins ;
        if(nfcAdapter == null)
        {
            Toast.makeText(this, "NFC를 지원하지 않는 단말기입니다.", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        id = getDeviceSerialNumber();


    }
    private static String getDeviceSerialNumber() {

        try {
            return (String) Build.class.getField("SERIAL").get(null);
        } catch (Exception ignored) {
            return null;
        }
    }
    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    private void AddData(String id) {

        class addData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(Paint.Join.this, "잠시만 기다려 주세요", null, true, true) ;
                //loading = ProgressDialog.show(Paint.Join.this, "잠시만 기다려 주세요", null, true, true) ;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPreExecute();
                //loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String id= (String) params[0];


                    //String link = urljoin;
                    String link = urlnfcin;
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");


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
                    return new String("doInBackground message : " + e.getMessage());
                }
            }
        }
        addData task = new addData();
        task.execute(id);
    } //nfc in
    private void AddData1(String id) {

        class addData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(Paint.Join.this, "잠시만 기다려 주세요", null, true, true) ;
                //loading = ProgressDialog.show(Paint.Join.this, "잠시만 기다려 주세요", null, true, true) ;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPreExecute();
                //loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String id= (String) params[0];

                    //String link = urljoin;
                    String link = urlnfcout;
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

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
                    return new String("doInBackground message : " + e.getMessage());
                }
            }
        }
        addData task = new addData();
        task.execute(id);
    } //nfc out
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (tag != null) {
            byte[] tagId = tag.getId();
            nfcid = toHexString(tagId);
            try {
                if (nfcid.equals("042CCF5AD44880")) {  // 출구태그후 입구태그시 ( 처음 출입시 )( 정상 )
                    Toast.makeText(getApplicationContext(), "입장", Toast.LENGTH_SHORT).show();
                    AddData(Tid);
                    getData(urlnfcin);



                    HttpThread thread = new HttpThread();
                    thread.start();

                } else if (nfcid.equals("047ECB5AD44881")) {  // 입구태그후 출구태그시 ( 퇴장시 )( 정상 )
                    Toast.makeText(getApplicationContext(), "퇴장", Toast.LENGTH_SHORT).show();
                    AddData1(Tid);
                    getData1(urlnfcout);
                    HttpThread thread = new HttpThread();
                    thread.start();
                    sql.execSQL("");

                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }
       }

    public class HttpThread extends Thread{
        public void run(){
            mHandler.sendEmptyMessage(0);
        }
    }
    public void senddata(String pin){

    }
    Handler mHandler = new Handler() {

        // 핸들러가 호출되면 이쪽 함수가 실행됩니다.

        public void handleMessage(Message msg)
        {

            if(msg.what == 0)
            {
                // 여기다가 코딩
                StringBuffer buffer = new StringBuffer();
                try{
                    URL url = new URL("rkdtjdwn.iptime.org/nfc/nfc.php");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");                                          // 전송 방식은 POST
                    http.setDoInput(true);                                                        // 서버에서 읽기 모드 지정
                    http.setDoOutput(true);                                                       // 서버로 쓰기 모드 지정
                    http.setDefaultUseCaches(false);
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    buffer.append("id").append("=").append(id).append("&");
                    buffer.append("nfcid").append("=").append(nfcid);

                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "utf-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();


                    myResult = builder.toString();                       // 전송결과를 전역 변수에 저장

                    Toast.makeText(Parking.this, "수업 등록 완료", Toast.LENGTH_LONG).show();


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    };

    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
                    .append(CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }
    public void getData1(String url){
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
                showList1();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    protected void showList1(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++) {

                JSONObject c = peoples.getJSONObject(i);

                Integer south = c.getInt(TAG_OUTH);
                Integer soutm = c.getInt(TAG_OUTM);
                Integer souts = c.getInt(TAG_OUTS);
                sql.execSQL("update " + PARKNAME + " set outh ='" + south + "' where id = '" + Tid + "'");
                sql.execSQL("update "+PARKNAME + " set outm ='" + soutm + "' where id = '" + Tid + "'");
                sql.execSQL("update "+PARKNAME+" set outs ='"+souts+"' where id = '"+Tid+"'");

                outtime = south * 3600 + soutm * 60 + souts;
                parkm = (outtime - intime) ; // 분

                /*if ( sum >9999){
                    parkm = parkm - 60 ; //1시간무료
                }else if (sum >29999){
                    parkm = parkm - 120; //2시간무료
                }else if ( sum > 49999){
                    parkm = parkm - 180 ; //3시간 무료
                }
                else{

                }*/
               if(sum<18000){
                   parkm=parkm*1;
               } else if(sum>=18000){
                    parkm= parkm- 3600;
                }else if (sum>=36000){
                    parkm=parkm-7200;
                }else if(sum>=54000){
                    parkm=parkm-10800;
                }else if(sum>=72000){
                    parkm=parkm-14400;
                }else if(sum>=90000){
                    parkm=0;//90000만원 이상 구매시 무료
                }/*else if(parkm==30){
                    parkm=0;//30분 이하 주차의 경우 무료
                }*/
                sumb = parkm * 5; //초당 5원, 분당 300원, 시간당 18000원
                if(sumb>0)
                textView.append("\n퇴장시간 :" + south + "시 " + soutm + "분 " + souts + "초\n주차요금 :" + sumb + " 원");
                else if(sumb<0)
                textView.append("\n퇴장시간 :" +south + "시" + soutm + "분" + souts + "초\n주차요금 :" + " 무료입니다 감사합니다!");
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    public void gomain5(View v){
        Intent intent = new Intent (getApplicationContext(),Main2Activity.class);
        startActivity(intent);
    }
    public void goitem5(View v){
        Intent intent = new Intent (getApplicationContext(),Itemlist.class);
        startActivity(intent);
    }
    public void gocart5(View v){
        Intent intent = new Intent (getApplicationContext(),Cart.class);
        startActivity(intent);
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

            for(int i=0;i<peoples.length();i++) {

                JSONObject c = peoples.getJSONObject(i);


                Integer sinh = c.getInt(TAG_INH);
                Integer sinm = c.getInt(TAG_INM);
                Integer sins = c.getInt(TAG_INS);
                sql.execSQL("insert into "+PARKNAME+" (id, inh, inm, ins) values ('"+Tid+"',"+sinh+","+sinm+","+sins + ")");
                tagDesc.setText("입장 시간 :" + sinh + "시 " + sinm + "분 " + sins + "초 \n구매금액 :" + sum + "원");
                intime = sinh * 3600 + sinm * 60 + sins;
                Toast.makeText(getApplicationContext(),"입장시간 저장",Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
/*public void getData2(String url){
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
                showList2();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    protected void showList2(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++) {

                JSONObject c = peoples.getJSONObject(i);
                Integer sinh = c.getInt(TAG_INH);
                Integer sinm = c.getInt(TAG_INM);
                Integer sins = c.getInt(TAG_INS);
                sql.execSQL("insert into "+PARKNAME+" (id, inh, inm, ins) values ('"+Tid+"',"+sinh+","+sinm+","+sins + ")");
                tagDesc.setText("입장 시간 :" + sinh +"시 "+sinm+"분 "+sins+"초 \n구매금액 :" +sum +"원");
                intime = sinh * 3600 + sinm * 60 + sins;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    */