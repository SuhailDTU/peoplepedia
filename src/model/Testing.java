package model;

import java.util.ArrayList;
import javax.crypto.*;

public class Testing {


    public static void main(String[] args) throws BadPaddingException{
       CategoryHandler catagoryHandler = new CategoryHandler();
       Conversion conversion = new Conversion();

       conversion.ConvertFromHexencodedFileToCommafile("src/encodedfile.txt", "src\\testpeoplepedia.txt");
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

        //delete entry
        //catagoryHandler.deleteEntry("Race1", "blue");

        //see current state of encoded file by decoding it
        //conversion.ConvertFromHexencodedFileToCommafile("src/encodedfile.txt", "src\\testpeoplepedia.txt");



        //print contents of encoded file
        //conversion.readhexencodedFile("src/encodedfile.txt");



        //testing addding entries to hexencoded file by converting back and forth
        /*
        ArrayList<String> testArrayList = new ArrayList<String>();
        testArrayList.add("url4");
        testArrayList.add("url5");
        testArrayList.add("url6");

        catagoryHandler.addEntry("Race5", "yellow", testArrayList );
        */


        //Conversion.encryptOrDecryptFile("pollo1234", "src/pass.txt", "src/passHid.txt", Cipher.ENCRYPT_MODE );
        //Conversion.encryptOrDecryptFile("1234", "src/passHid.txt", "src/pass.txt", Cipher.DECRYPT_MODE );
        //Conversion.encryptOrDecryptFile("pollo1234", "src/encodedfile.txt", "src/encryptedEncodedFile.txt", Cipher.ENCRYPT_MODE );
        //Conversion.encryptOrDecryptFile("1234", "src/encryptTest.txt", "src/decryptTest.txt", Cipher.DECRYPT_MODE );

    }





}
