package com.example.user.gharbar.Models;

/**
 * Created by Pablo Shakun on 2/10/2017.
 */
public class Album {
    private String name;
    private int rentPrice;
    private int thumbnail;
    public Album(String name, int rentPrice, int thumbnail) {
        this.name = name;
        this.rentPrice = rentPrice;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getrentPrice() {
        return rentPrice;
    }
    public int getThumbnail() {
        return thumbnail;
    }

}