package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Testing {


    public static void main(String[] args) {
       CategoryHandler catagoryHandler = new CategoryHandler();

       ArrayList<Categories> categories = catagoryHandler.readincatagories("C:\\Users\\suhai\\Desktop\\Diverse\\testpeoplepedia.txt");

       /*
        for (Categories catagory: categories) {
            ArrayList<Person> people = catagory.getPeople();
            System.out.println("---"+catagory.getCatagoryname());
            for (Person person: people) {
                System.out.println("-"+person.getName());
                ArrayList<String> urls = person.getUrls();
                for (String url: urls) {
                    System.out.println(url);
                }
            }
        }*/

        Conversion conversion = new Conversion();

        conversion.ConvertFromCommafileToHexEncodedUTF8("C:\\Users\\suhai\\Desktop\\Diverse\\testpeoplepedia.txt");

        conversion.readhexencodedFile("src/encodedfile.txt");







    }





}
