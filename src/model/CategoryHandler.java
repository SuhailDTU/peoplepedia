package model;

import java.io.*;
import java.util.ArrayList;

public class CategoryHandler {
    Conversion converter = new Conversion();
    public CategoryHandler(){
    }
    //reads in catagories from comma seperated file
    public ArrayList<Categories> readincatagories(String filename){
        ArrayList <Categories> categories = new ArrayList<Categories>();
        String currentline;
        String[] tokens;
        int numOfURLS;
        Person currentPerson;
        Categories currentcatagory;

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))){

            while((currentline = bufferedReader.readLine())!= null){
                tokens = currentline.split(";");
                numOfURLS = Integer.parseInt(tokens[2]);

                currentcatagory = new Categories(tokens[0]);
                currentPerson = new Person(tokens[1]);

                if(numOfURLS!= 0){
                    if(numOfURLS == 1){
                        currentPerson.getUrls().add(tokens[3]);
                    }else{
                        for (int i = 3; i < 3+numOfURLS; i++){
                            currentPerson.getUrls().add(tokens[i]);
                        }
                    }
                }

                if(categories.contains(currentcatagory)){//check if category in list
                    int index = categories.indexOf(currentcatagory);//get index of category already inlist
                    ArrayList<Person> personArrayList = categories.get(index).getPeople(); //get perople list

                    personArrayList.add(currentPerson);//add to people lsit

                    categories.get(index).setPeople(personArrayList); //replace people list in category

                }else{
                    ArrayList<Person> personArrayList = currentcatagory.getPeople(); //get perople list
                    personArrayList.add(currentPerson);//add the person to the peoplelist of catategory
                    currentcatagory.setPeople(personArrayList); //replace the list of category

                    categories.add(currentcatagory);

                }




            }




        }
        catch (IOException ioException){}

        return categories;

    }

    public void addEntry(String category, String name, ArrayList<String> urls){
        converter.ConvertFromHexencodedFileToCommafile("src/encodedfile.txt");//convert to listing form

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/testpeoplepedia.txt", true))) {
            bufferedWriter.write("\n" + category + ";" + name + ";" + urls.size() );

            for(int i = 0 ; i < urls.size(); i++){
                bufferedWriter.write(";" + urls.get(i));
            }


        }catch (IOException ioException){
            ioException.printStackTrace();
        }
        converter.ConvertFromCommafileToHexEncodedUTF8("src/testpeoplepedia.txt");
        File listingfile = new File("src/testpeoplepedia.txt");// delete file after use
        listingfile.delete();


    }



}
