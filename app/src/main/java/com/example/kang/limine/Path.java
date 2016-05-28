package com.example.kang.limine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class Path extends Parking {

    ListView list;
    ListAdapter adapter;

    private ArrayList<MyData> arrData = new ArrayList<MyData>();

    private class MyData  {
        private int image;
        private String name;
        private String price;
        private String gaesu;


        public MyData(int image, String name, String price,String gaesu) {
            this.image = image;
            this.name = name;
            this.price = price;
            this.gaesu = gaesu;
        }
        public int getImage() {
            return image;
        }
        public String getName() {
            return name;
        }
        public String getPrice() {
            return price;
        }
        public String getGaesu(){
            return gaesu;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        setData();
        adapter = new ListAdapter(this, arrData);
        list = (ListView) findViewById(R.id.listView3);
        list.setAdapter(adapter);
    }
    private void setData() {
        arrData = new ArrayList<MyData>();
        Cursor c = sql.rawQuery("select * from "+CARTNAME+" order by itemlocation asc", null);
        int count = c.getCount();
        for (int i = 0; i < count; i++) {
            c.moveToNext();
            String cname = c.getString(0);
            String cprice = c.getString(1);
            String chow = c.getString(4);


            int p = Integer.parseInt(cprice);
            int h = Integer.parseInt(chow);
            int price = p*h;
            String pri = String.valueOf(price);

            arrData.add(new MyData(R.drawable.ic_menu_camera, cname, pri,chow));

        }
    }
    private class ListAdapter extends ArrayAdapter<MyData> {

        LayoutInflater inflater;
        Context context;

        private class ViewHolder {
            ImageView image;
            TextView name;
            TextView price;
            TextView gaesu;
            Button delete;

        }

        ListAdapter(Context context, ArrayList<MyData> list) {
            super(context, 0, list);
            this.context = context;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.cart_list, null);
                holder.image = (ImageView) convertView.findViewById(R.id.image2);
                holder.name = (TextView) convertView.findViewById(R.id.name2);
                holder.price = (TextView) convertView.findViewById(R.id.price2);
                holder.gaesu = (TextView) convertView.findViewById(R.id.gaesu);
                holder.delete = (Button) convertView.findViewById(R.id.delete); //////////////if error this
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final MyData tems = (MyData) getItem(position);
            holder.name.setText(tems.name);
            holder.price.setText(tems.price);
            holder.gaesu.setText(tems.gaesu);
            holder.image.setImageResource(tems.image);
            holder.delete.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    int a = Integer.parseInt(tems.price);
                    sql.execSQL("insert into "+BUYNAME+ " (price) values ("+a+")");
                    sql.execSQL("delete from "+CARTNAME+" where itemname = '"+tems.name+"'");
                    Intent intent = new Intent (getApplication(),Path.class);
                    startActivity(intent);
                    finish();
                }
            });
            return convertView;
        }
    }
}
