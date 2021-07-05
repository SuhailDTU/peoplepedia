package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Testing {


    public static void main(String[] args) {
       CategoryHandler catagoryHandler = new CategoryHandler();
       Conversion conversion = new Conversion();

       conversion.ConvertFromHexencodedFileToCommafile("src/encodedfile.txt");
       ArrayList<Categories> categories = catagoryHandler.readincatagories("src/testpeoplepedia.txt");


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
        }



        //conversion.ConvertFromCommafileToHexEncodedUTF8("src/testpeoplepedia.txt");

        //conversion.readhexencodedFile("src/encodedfile.txt");
       // conversion.ConvertFromHexencodedFileToCommafile("src/encodedfile.txt");//convert to listing form


        //testing addding entries to hexencoded file by converting back and forth
        /*
        ArrayList<String> testArrayList = new ArrayList<String>();
        testArrayList.add("url4");
        testArrayList.add("url5");
        testArrayList.add("url6");

        catagoryHandler.addEntry("Race5", "yellow", testArrayList );
        */




    }





}
