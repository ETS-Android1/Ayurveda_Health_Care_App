package com.example.ayurvedahealthcareapp;

public class News {
    String heading;

    String breifNews;
    boolean visibility;
    int titleImage;


    public News(String heading, String breifNews, int titleImage) {
        this.heading = heading;
        this.breifNews = breifNews;
        this.titleImage = titleImage;
        this.visibility=false;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
