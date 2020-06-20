package com.example.finalproject;

public class NewsMessage {
    private String headline;
    private String link;
    private String description;
    private String date;
    long id;


    public NewsMessage(long id , String headline, String link, String description, String date){
        this.headline = headline;
        this.link = link;
        this.description=description;
        this.id = id;
        this.date = date;
    }
    public String getHeadline() {
        return headline;
    }
    public String getDescription() {
        return description;
    }
    public String getLink() {
        return link;
    }
    public String getDate(){
        return date;
    }
    public long getId(){
        return id;
    }
    }


