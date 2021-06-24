package model;

import java.util.ArrayList;

public class Person {
    private String name;
    private ArrayList<String> urls = new ArrayList<String>();

    public Person(String name){
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }
}
