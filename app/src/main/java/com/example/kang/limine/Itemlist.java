package com.example.kang.limine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Itemlist extends Parking {

    TextView textView;
    int sum = 0;
    private String arr[];
    String Citemname,Citemhow;
    String myJSON1;
    JSONArray peoples1 = null;
    ListAdapter listAdapter;

    private ArrayList<Iteminfo> items = new ArrayList<Iteminfo>();
    private static final String TAG_RESULTS = "result";
    private static final String TAG_TYPE = "type";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_LOCA ="num";
    private static final String TAG_COUNT ="count";

    private class Iteminfo {
        int icon;
        String name;
        String price;
        String num;
        String count;
        Iteminfo(int icon, String name, String price,String num,String count) {
            this.icon = icon;
            this.name = name;
            this.price = price;
            this.num = num;
            this.count = count;
        }
    }

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);
        list = (ListView) findViewById(R.id.listView);
        getData(urlitemlist);

    }
    public void golist(View v){
        Intent intent = new Intent (getApplicationContext(),Cart.class);
        startActivity(intent);
        finish();
    }

    public void getData(String url) {
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
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON1 = result;
                showList();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON1);
            peoples1 = jsonObj.getJSONArray(TAG_RESULTS);
            textView = (TextView)findViewById(R.id.sumtotal);

            for (int i = 0; i < peoples1.length(); i++) {

                JSONObject c = peoples1.getJSONObject(i);

                String type = c.getString(TAG_TYPE);
                String price = c.getString(TAG_PRICE);
                String name = c.getString(TAG_NAME);
                String num = c.getString(TAG_LOCA);
                String count =c.getString(TAG_COUNT);

                items.add(new Iteminfo(R.drawable.ic_menu_camera, name, price,num,count));

            }
            listAdapter = new ListAdapter(this, items);
            list.setAdapter(listAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class ListAdapter extends ArrayAdapter<Iteminfo> {

        LayoutInflater inflater;
        Context context;

        private class ViewHolder {
            ImageView icon;
            TextView name;
            TextView price;
            TextView num;
            TextView count;
            Button b1, b2;

        }

        private class ViewHolder2 extends ViewHolder {
            TextView b1;
        }

        ListAdapter(Context context, ArrayList<Iteminfo> list) {
            super(context, 0, list);
            this.context = context;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.image);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.price = (TextView) convertView.findViewById(R.id.price);
                holder.num = (TextView) convertView.findViewById(R.id.locations);
                holder.b1 = (Button) convertView.findViewById(R.id.gocart); //////////////if error this
                holder.b2 = (Button) convertView.findViewById(R.id.godetail);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Iteminfo tems = (Iteminfo) getItem(position);
            holder.name.setText(tems.name);
            holder.price.setText(tems.price);
            holder.num.setText(tems.num);
            holder.icon.setImageResource(tems.icon);
            holder.b1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Cursor c = sql.rawQuery("select * From " + CARTNAME, null);
                        int count = c.getCount();
                        for (int i = 0; i < count; i++) {
                            c.moveToNext();
                            Citemname = c.getString(0);
                        }
                        if (tems.name.equals(Citemname)) {
                            Toast.makeText(getContext(), "이미 추가된 물품입니다", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            sql.execSQL("insert into " + CARTNAME + " (itemname, itemprice, itemlocation, itemhow, itemcheck) values ('" + tems.name + "','" + tems.price + "','" + tems.num + "','" + tems.count + "',0)");
                            Toast.makeText(getContext(), tems.name + " 제품이 장바구니에 추가되었습니다", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.b2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //detail
                }
            });
            return convertView;
        }
    }
    public void gomain(View v){
        Intent intent = new Intent (getApplication(),Main2Activity.class);
        startActivity(intent);
        finish();
    }

}
