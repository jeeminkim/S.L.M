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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Cart extends Parking {

    TextView textView;
    int sum = 0 ;
    TextView textcount;
    String Citemhow;

    ListView list;
    ListAdapter adapter;
    private ArrayList<MyData> arrData = new ArrayList<MyData>();

    private class MyData  {
        private int image;
        private String name;
        private String price;
        private String location;
        private String count;

        public MyData(int image, String name, String price, String location,String count) {
            this.image = image;
            this.name = name;
            this.price = price;
            this.location = location;
            this.count = count;
        }
        public int getImage() {
            return image;
        }
        public String getName() {
            return name;
        }
        public String getPrice() { return price; }
        public String getLocation() {return location; }
        public String getCount() {return count;}
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setData();
        adapter = new ListAdapter(this, arrData);
        list = (ListView) findViewById(R.id.listView2);
        list.setAdapter(adapter);
    }

    private void setData() {
        arrData = new ArrayList<MyData>();
        textView = (TextView)findViewById(R.id.sumtotal);
        Cursor c = sql.rawQuery("select * From " + CARTNAME, null);
        int count = c.getCount();
        for (int i = 0; i < count; i++) {
            c.moveToNext();
            String cname = c.getString(0);
            String cprice = c.getString(1);
            String clocation = c.getString(2);
            String curl = c.getString(3);
            String ccount = c.getString(4);
            String ccheck = c.getString(5);

            arrData.add(new MyData(R.drawable.ic_menu_camera, cname, cprice, clocation, ccount));
            int scount = Integer.parseInt(ccount);
            int sprice = Integer.parseInt(cprice);
            sum = sum+(scount*sprice);

        }
        String str = Integer.toString(sum);
        textView.append(str);
    }
    public void allclear(View v){
        Toast.makeText(getApplicationContext(), "목록을 비웠습니다", Toast.LENGTH_SHORT).show();
        sql.execSQL("drop table " + CARTNAME);
        sql.execSQL("create table "+CARTNAME+" (itemname text, itemprice text, itemlocation text, itemurl text, itemhow int, itemcheck text)");
        Intent intent01 = new Intent (getApplicationContext(),Cart.class);
        startActivity(intent01);
        finish();
    }
    private class ListAdapter extends ArrayAdapter<MyData> {

        LayoutInflater inflater;
        Context context;

        private class ViewHolder {
            ImageView image;
            TextView name;
            TextView price;
            TextView location;
            TextView count;
            Button b1;

            Button up,down;
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
                convertView = inflater.inflate(R.layout.list_item2, null);
                holder.image = (ImageView) convertView.findViewById(R.id.image2);
                holder.name = (TextView) convertView.findViewById(R.id.name2);
                holder.price = (TextView) convertView.findViewById(R.id.price2);
                holder.location = (TextView)convertView.findViewById(R.id.locations2);
                holder.count = (TextView)convertView.findViewById(R.id.count2);

                holder.up = (Button) convertView.findViewById(R.id.up2);
                holder.down = (Button) convertView.findViewById(R.id.down2);
                holder.b1 = (Button) convertView.findViewById(R.id.godelete); //////////////if error this
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final MyData tems = (MyData) getItem(position);
            textView = (TextView)findViewById(R.id.count2);
            holder.name.setText(tems.name);
            holder.price.setText(tems.price);
            holder.image.setImageResource(tems.image);
            holder.location.setText(tems.location);
            holder.count.setText(tems.count);
            holder.b1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sql.execSQL("delete from " + CARTNAME + " where itemname = '" + tems.name + "'");
                    Toast.makeText(getContext(), tems.name + " 물품이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Cart.class);
                    startActivity(intent);
                    finish();
                }
            });
            holder.up.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Cursor c = sql.rawQuery("select * from " + CARTNAME + " where itemname = '"+tems.name+"'", null);
                    int ct = c.getCount();
                    for (int i = ct-1; i < ct; i++) {
                        c.moveToNext();
                        int how = c.getInt(4);
                        how = how + 1;
                        sql.execSQL("update " + CARTNAME + " set itemhow = '" + how + "' where itemname = '" + tems.name + "'");
                        Intent intent = new Intent(getApplicationContext(),Cart.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            holder.down.setOnClickListener(new View.OnClickListener() {
               public void onClick(View v) {
                    Cursor c = sql.rawQuery("select * from " + CARTNAME + " where itemname = '"+tems.name+"'", null);
                   int ct = c.getCount();// 4
                    for (int i = ct-1; i < ct; i++) {
                        c.moveToNext();
                        int how1 = c.getInt(4);
                        how1 = how1 - 1;
                        if(how1 > 0){
                            sql.execSQL("update " + CARTNAME + " set itemhow = '" + how1 + "' where itemname = '" + tems.name + "'");
                            Intent intent = new Intent(getApplicationContext(),Cart.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"물품을 삭제합니다.",Toast.LENGTH_SHORT).show();
                            sql.execSQL("delete from " + CARTNAME + " where itemname = '" + tems.name + "'");
                            Intent intent = new Intent(getApplicationContext(),Cart.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
            return convertView;
        }
    }
    public void gomap (View v){
        Intent intent = new Intent (getApplication(),Path.class);
        startActivity(intent);
        finish();

    }
    public void itemlist (View v){
        Intent intent = new Intent (getApplication(),Itemlist.class);
        startActivity(intent);
        finish();

    }
    public void gomain2(View v){
        Intent intent = new Intent (getApplication(),Main2Activity.class);
        startActivity(intent);
        finish();
    }
}
