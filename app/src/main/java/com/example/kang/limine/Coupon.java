package com.example.kang.limine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Coupon extends AppCompatActivity {


    ListView lvItems;
    // ListView  location;
    //ListView lvMaps;
    private ArrayList<ItemInfo> items = new ArrayList<ItemInfo>();
    //private ArrayList<MapInfo> maps=new ArrayList<MapInfo>();
    private ArrayList<Abbre> locations = new ArrayList<Abbre>();
    ItemListAdapter listAdapter;
    private class Abbre{
        String location;
        Abbre(String location)
        {
            this.location= location;
        }
    }


    //MapListAdapter mlistAdapter;
    private class ItemInfo {
        int icon;
        String name;
        String item;
        ItemInfo(int icon, String name, String item)
        {
            this.icon = icon;
            this.name = name;
            this.item = item;
        }
    }

    /*private class MapInfo{
        int icon;
        MapInfo(int icon){
            this.icon=icon;
        }
    }*/
    /*private class MapListAdapter extends ArrayAdapter<MapInfo>
    LayoutInflater inflater;
    Context context;
    private class ViewHolder{
    ImageView map;
    }
    MapListAdapter(Context context,ArrayList<MapInfo> list){
    super(context,0,list);
    this.context=context;
    inflater=(LayoutInflater)getSystenServuce(LAYOUT_INFLATER_SERVICE);
    }
     @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView2==null) {
              //새로 생성 //converterview가 더이상 정보가 없는 경우
                holder =new ViewHolder();
                convertView2 = inflater.inflate(R.layout.activity_dialog, null);
                holder.map=(ImageView)convertView.findViewById(R.id.ivLocation);
                //홀더에 대한 정보를 넣어주면 됨.
                convertView.setTag(holder); //홀더가 convertview와 같이 돌아다님.
           } //필요할때만 가져오는 코드 // 분석
            else{
                //재사용 케이스
                holder=(ViewHolder)convertView2.getTag();
            }
            /*
            ImageView ivProfile = (ImageView)convertView.findViewById(R.id.ivProfile);
            TextView tvName= (TextView)convertView.findViewById(R.id.tvName);
            TextView tvPhone=(TextView)convertView.findViewById(R.id.tvPhone);
            */ //무의미해진 작업.
 /*   final MapInfo minfo = (MapInfo)getItem(position);
    holder.map.setImageResource(minfo.map);
        */
    private class ItemListAdapter extends ArrayAdapter<ItemInfo>{
        //화면을 실행했을때 몇번까지 몇번까지 데이터를 보여줘야겠다
        //가져와서 리스트에게 넘겨주는 역할
        // 몇번 리스트아이템을 달라는 요청
        LayoutInflater inflater; //xml을 자바에서 쓸수 있게 변화를 해주는 역할
        Context context;
        private class ViewHolder {
            ImageView icon;
            TextView name;
            TextView item;
            ImageButton location;
        }
        private class ViewHolder2 extends ViewHolder{
            TextView location;
        }
        ItemListAdapter(Context context, ArrayList<ItemInfo> list) {
            super(context, 0, list);
            this.context=context;
            inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);//xml을 리스트 뷰로 사용하겠다.
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null) {

                //.location=(TextView)dialogView.findViewById(R.id.ivLocation);
                //새로 생성 //converterview가 더이상 정보가 없는 경우
                holder =new ViewHolder();
                convertView = inflater.inflate(R.layout.activity_item_coupon, null);
                holder.icon=(ImageView)convertView.findViewById(R.id.ivProfile);
                holder.name=(TextView)convertView.findViewById(R.id.tvName);
                holder.item=(TextView)convertView.findViewById(R.id.tvPhone);
                holder.location=(ImageButton)convertView.findViewById(R.id.ibtn_Phone);
                //홀더에 대한 정보를 넣어주면 됨.
                convertView.setTag(holder); //홀더가 convertview와 같이 돌아다님.
            } //필요할때만 가져오는 코드 // 분석
            else{
                //재사용 케이스
                holder=(ViewHolder)convertView.getTag();
            }
            /*
            ImageView ivProfile = (ImageView)convertView.findViewById(R.id.ivProfile);
            TextView tvName= (TextView)convertView.findViewById(R.id.tvName);
            TextView tvPhone=(TextView)convertView.findViewById(R.id.tvPhone);
            */ //무의미해진 작업.
            final ItemInfo info = (ItemInfo)getItem(position);
            holder.name.setText(info.name);
            holder.item.setText(info.item);
            holder.icon.setImageResource(info.icon);
            holder.location.setOnClickListener(new View.OnClickListener() { //location 버튼을 눌렀을때
                @Override
                public void onClick(View v) {
                   /*
                   private class ItemInfo{
                   int icon;
                   ItemInfo(int icon){
                   this.icon=icon;
                   }
                   }
                   private class ItemListAdapter extends ArrayAdapter<ItemInfo>{
                 LayoutInflater inflater; //xml을 자바에서 쓸수 있게 변화를 해주는 역할
        Context context;
        private class ViewHolder {
            ImageView icon;
        }
        ItemListAdapter(Context context, ArrayList<ItemInfo> list) {
            super(context, 0, list);
            this.context=context;
            inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);//xml을 리스트 뷰로 사용하겠다.
        }
           @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null) {
                convertView = inflater.inflate(R.layout.activity_dialog, null);
                holder.icon=(ImageView)convertView.findViewById(R.id.ivLocation);
                convertView.setTag(holder); //홀더가 convertview와 같이 돌아다님.
            } //필요할때만 가져오는 코드 // 분석
            else{
                //재사용 케이스
                holder=(ViewHolder)convertView.getTag();
            }
            final ItemInfo info = (ItemInfo)getItem(position);
            holder.icon.setImageResource(info.icon);
                    */
                    LayoutInflater inflater=getLayoutInflater();
                    View dialogView=inflater.inflate(R.layout.activity_dialog_coupon, null);
                    ViewHolder2 holder2;
                    holder2=new ViewHolder2();

                    holder2.location=(TextView)dialogView.findViewById(R.id.ivLocation);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    new AlertDialog.Builder(getContext())
                            .setTitle("쿠폰: "+info.name)
                            .setMessage(info.item  + " \n" + "\n" + "\n"+ locations.get(position).location)
                            .setIcon(R.mipmap.ic_launcher)
                                    //.setView(new TextView().setText(locations.get(position).location))
                                    //.setView(Abbre.location)
                                    //

                            .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(Coupon.this, "저장되었습니다", Toast.LENGTH_LONG).show();
                                }
                            })

                            .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(Coupon.this, "확인 되었습니다", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNeutralButton("도움", null).show();
                }
            });
            return convertView;
        }
        //Array Adapter에서 가장 메인 역할을 하는 코드
        //position과 convertview
        //convert뷰에 인플레이터를 파싱
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_coupon);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items.add(new ItemInfo(R.drawable.icecream, "투게더 (500g)", "5280 -> 4980"));
        items.add(new ItemInfo(R.drawable.milk, "서울우유 (1.8L)", "5500 -> 4900"));
        items.add(new ItemInfo(R.drawable.meat, "한우-국거리 (100g)", "1290 -> 990"));
        items.add(new ItemInfo(R.drawable.zzambong, "진짬뽕 (4+1)", "2개 구매시 1000원 할인"));
        items.add(new ItemInfo(R.drawable.mandoo, "고향만두 (390*3)", "8900 -> 7900"));
        listAdapter = new ItemListAdapter(this,items);

        //listAdapter= new MapListAdapter(this,maps);
        locations.add(new Abbre("식품->냉동->3열 1줄"));
        locations.add(new Abbre("식품->냉장->1열 2줄"));
        locations.add(new Abbre("식품->냉장->5열 1줄"));
        locations.add(new Abbre("식품->가공->라면->3열 1줄"));
        locations.add(new Abbre("식품->냉동->10열 1줄"));

        lvItems.setAdapter(listAdapter);

    }

}

