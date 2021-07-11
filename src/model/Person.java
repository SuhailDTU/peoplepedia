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

    public String getUrlCommaseperated(){
        String urlString = "";

        //if the list is not empty fill string with contents
        if(urls.size() != 0) {
            urlString = urlString + urls.get(0);
            for (int i = 1; i < urls.size(); i++) {
                urlString = urlString + ";" + urls.get(i);
            }
        }
        return urlString;
    }
}
