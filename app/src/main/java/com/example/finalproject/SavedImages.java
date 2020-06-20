package com.example.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SavedImages extends AppCompatActivity {

    public static final String URL = "url";
    public static final String HD = "hd";
    Cursor c;
    ImageDatabaseHelper helper;
    ListView list;
    AlertDialog.Builder alert;
    ImageAdapter adapter;
    SQLiteDatabase db;
    ArrayList<Images> aList = new ArrayList<>();
    Images images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);
        adapter = new ImageAdapter(this,aList);
        list = findViewById(R.id.list);
        helper = new ImageDatabaseHelper(this);
        db = helper.getWritableDatabase();

        c = db.query(helper.TABLE,new String[]{helper.ID,helper.URL,helper.DATE,helper.IMAGE,helper.HD_URL},null,null,null,null,null,null);
        LoadData();
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener((AdapterView<?> innerList, View view, int position, long id) -> {
            Bundle data = new Bundle();
            data.putString(URL, aList.get(position).getUrl());
            data.putString(HD, aList.get(position).getHd_Url());
            DetailFragment details = new DetailFragment();
            details.setArguments(data);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,details).commit();
        });

        list.setOnItemLongClickListener((AdapterView<?>parent,View v,int position, long id) -> {
            alert = new AlertDialog.Builder(this);
            String data_id = Long.toString(id);

            alert.setTitle("Do you want to delete this image?");
            alert.setMessage("The selected row is: "+position+"\nThe database id is: "+id);
            alert.setPositiveButton("Yes", (DialogInterface dialog, int which) ->{
                db.delete(helper.TABLE,helper.ID+" =?",new String[]{data_id});
                aList.remove(position);
                adapter.notifyDataSetChanged();
            });
            alert.setNegativeButton("No",(DialogInterface dialog,int which) ->{
            });
            alert.show();
            adapter.notifyDataSetChanged();
            return true;
        });
    }

    private void LoadData(){
        while(c.moveToNext()){
            byte[] byteA = c.getBlob(c.getColumnIndex(helper.IMAGE));
            long id = c.getLong(c.getColumnIndex(helper.ID));
            String date = c.getString(c.getColumnIndex(helper.DATE));
            String url = c.getString(c.getColumnIndex(helper.URL));
            String hd = c.getString(c.getColumnIndex(helper.HD_URL));
            Bitmap map = BitmapFactory.decodeByteArray(byteA,0,byteA.length);
            aList.add(new Images(id,url,hd,date,map));
            list.setAdapter(adapter);
        }
    }

    private class ImageAdapter extends BaseAdapter{

        public ImageAdapter(Context context,ArrayList<Images> list){}

        @Override
        public int getCount() {
            return aList.size();
        }

        @Override
        public Images getItem(int position) {
            return aList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Bitmap map = getItem(position).getMap();
            String date = getItem(position).getDate();
            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.saved_image,parent,false);
            TextView dateT = row.findViewById(R.id.text_image);
            ImageView imageT = row.findViewById(R.id.list_image);
            dateT.setText(date);
            imageT.setImageBitmap(map);
            return row;
//            return null;
        }
    }

}
