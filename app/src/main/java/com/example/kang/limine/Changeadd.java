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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Changeadd extends Parking {


    EditText editText1, editText2;
    String Cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeadd);

        editText1 = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);

    }
    public void addok(View view){


        AlertDialog dialog = createDialogBox();
        dialog.show();

    }

    private void AddData(String id,String address) {

        class addData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Changeadd.this, "잠시만 기다려 주세요", null, true, true);
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
                    String address = (String) params[1];

                    String link = urlchangeadd;
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");


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
        task.execute(id, address);
    }
    private AlertDialog createDialogBox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("배송시 주소지 변경 / 입력 으로 발생한 모든 피해는 책임지지 않습니다. \n 언제든 수정 가능합니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor c = sql.rawQuery("select id From " + TABLENAME, null);
                int count = c.getCount();
                for (int i = 0; i < count; i++) {
                    c.moveToNext();
                    Cid = c.getString(0);
                }

                String row1, row2;
                row1 = editText1.getText().toString();
                row2 = editText2.getText().toString();
                if(row1.equals(null)||row2.equals(null))
                {
                    Toast.makeText(getApplicationContext(),"주소지를 남김없이 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else {
                    sql.execSQL("update " + TABLENAME + " set address ='" + row1 +" "+ row2 + "' where id ='" + Cid + "'");
                    AddData(Cid, row1 + row2);
                    Toast.makeText(getApplicationContext(), "주소지가 변경되었습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Myinfo.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "주소지 입력이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Myinfo.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
