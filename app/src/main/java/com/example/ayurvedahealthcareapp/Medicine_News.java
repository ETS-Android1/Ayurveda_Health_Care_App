package com.example.ayurvedahealthcareapp;


public class Medicine_News {
   // public String heading;
    String mheading;

    String mbreifNews;
    boolean visibility;
    int mtitleImage;


    public Medicine_News(String heading, String breifNews, int titleImage) {
        this.mheading = heading;
        this.mbreifNews = breifNews;
        this.mtitleImage = titleImage;
        this.visibility=false;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
