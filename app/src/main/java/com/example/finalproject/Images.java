package com.example.finalproject;

import android.graphics.Bitmap;

public class Images {
    String url,hd_Url,date;
    long id;
    Bitmap map;

    public Images(long id,String url,String hd,String date,Bitmap map)
    {
        this.id = id;
        this.date = date;
        this.url = url;
        hd_Url = hd;
        this.map = map;
    }

    public long getId(){
        return id;
    }

    public String getUrl(){
        return url;
    }

    public String getHd_Url(){
        return hd_Url;
    }

    public Bitmap getMap() {
        return map;
    }

    public String getDate() {
        return date;
    }

}
