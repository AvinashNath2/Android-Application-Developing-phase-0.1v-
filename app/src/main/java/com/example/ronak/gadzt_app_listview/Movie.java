package com.example.ronak.gadzt_app_listview;

/**
 * Created by ronak on 8/16/2017.
 */

public class Movie {

    private String title, genre,price;

    public Movie() {
    }

    public Movie(String title, String genre,String price) {
        this.title = title;
        this.genre = genre;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}
