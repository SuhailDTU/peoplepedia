package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryHandler {
    Conversion converter = new Conversion();



    public CategoryHandler(){
    }


/*
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

                    personArrayList.add(currentPerson);//add to people list

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
*/



    //reads in catagories from comma seperated file
    public ArrayList<Categories> readincatagories(String filename){
        ArrayList <Categories> categories = new ArrayList<Categories>();
        String currentline;
        String[] tokens;
        int numOfURLS;
        Person currentPerson;
        List<Categories> currentcatagories;

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))){

            while((currentline = bufferedReader.readLine())!= null){
                tokens = currentline.split(";");
                numOfURLS = Integer.parseInt(tokens[2]);

                currentcatagories = getCategoriesFromStringList(getCategoryStrings(tokens[0]));
               /* System.out.println("current category list");
                System.out.println(currentcatagories);
                System.out.println(" \n");*/
                currentPerson = new Person(tokens[1]);

                addUrlsToPerson(numOfURLS, currentPerson, tokens);

                addPersonToCategoriesAndCategoriesToList(categories, currentcatagories, currentPerson);
               /* System.out.println("current category list after adding person");
                System.out.println(currentcatagories);
                System.out.println(" \n");*/
            }
            //
            //System.out.println("final category list");
            //System.out.println(categories);
        }
        catch (IOException ioException){}

        return categories;

    }

    private void addUrlsToPerson(int numOfURLS, Person person, String[] tokens){
        if(numOfURLS!= 0){
            if(numOfURLS == 1){
                person.getUrls().add(tokens[3]);
            }else{
                for (int i = 3; i < 3+numOfURLS; i++){
                    person.getUrls().add(tokens[i]);
                }
            }
        }
    }
    private void addPersonToCategoriesAndCategoriesToList(ArrayList<Categories> overAllCategories, List<Categories> newCategories, Person personToAdd ){
        for(int i = 0; i < newCategories.size(); i++){

            if(!overAllCategories.contains(newCategories.get(i))){
                newCategories.get(i).getPeople().add(personToAdd);
                overAllCategories.add(newCategories.get(i));
            }else{
                overAllCategories.get(overAllCategories.indexOf(newCategories.get(i))).getPeople().add(personToAdd);
            }
            //System.out.println("added person to category");
            //System.out.println( newCategories.get(i));
        }


    }

    private List<String> getCategoryStrings(String string){

        return Arrays.stream(string.split("#")).toList();
    }

    private ArrayList<Categories> getCategoriesFromStringList(List<String> list){
        ArrayList<Categories> returnList = new ArrayList<>();
        for(int i = 0; i < list.size() ; i++){
            returnList.add(new Categories(list.get(i)));
        }
        return returnList;
    }


    public void addEntry(String category, String name, ArrayList<String> urls){
        converter.ConvertFromHexencodedFileToCommafile("src/encodedfile.txt", "src\\testpeoplepedia.txt");//convert to listing form

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/testpeoplepedia.txt", true))) {
            bufferedWriter.write("\n" + category + ";" + name + ";" + urls.size() );

            for(int i = 0 ; i < urls.size(); i++){
                bufferedWriter.write(";" + urls.get(i));
            }


        }catch (IOException ioException){
            ioException.printStackTrace();
        }
        converter.ConvertFromCommafileToHexEncodedUTF8("src/testpeoplepedia.txt", "src/encodedfile.txt");
        File listingfile = new File("src/testpeoplepedia.txt");// delete file after use
        listingfile.delete();


    }
    //overloaded functions signature if you want to specify the encodedfile and listingFile
    public void addEntry(String category, String name, ArrayList<String> urls, String listingFilename, String encodedFilename){
        converter.ConvertFromHexencodedFileToCommafile(encodedFilename, listingFilename);//convert to listing form

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(listingFilename, true))) {
            bufferedWriter.write("\n" + category + ";" + name + ";" + urls.size() );

            for(int i = 0 ; i < urls.size(); i++){
                bufferedWriter.write(";" + urls.get(i));
            }


        }catch (IOException ioException){
            ioException.printStackTrace();
        }
        converter.ConvertFromCommafileToHexEncodedUTF8(listingFilename, encodedFilename);
        File listingfile = new File(listingFilename);// delete file after use
        listingfile.delete();
    }

    //this deletes an entry from the lsiting file
    //it does this by reading in the listing file into an arraylist of strings and deleting the string that contains both the catagory and name
    public void deleteEntry(String category, String name, String commaSeperatedUrls){
        converter.ConvertFromHexencodedFileToCommafile("src/encodedfile.txt", "src\\testpeoplepedia.txt");
        ArrayList<String> dataArray = new ArrayList<String>();
        String line;
        String dataString;


        //read in data into string array
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/testpeoplepedia.txt"))) {
            while((line = bufferedReader.readLine()) != null){
                dataArray.add(line);
            }
        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        //remove listing
        for(int i = 0; i < dataArray.size(); i++){
            //get line
            dataString = dataArray.get(i);
            //split line into tokens
            String[] tokens = dataString.split(";");
            //remove entry matching token 0 and 1 (catagory and name) and if the string contains the urls
            if(tokens[0].equals(category) && tokens[1].equals(name) && dataString.contains(commaSeperatedUrls)){ //if line containing category and name found then remove it
                dataArray.remove(i);
                break;//jump out after first match
            }
        }

        //write to file
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/testpeoplepedia.txt"))) {
           for (int i = 0; i < dataArray.size()-1; i++){
               bufferedWriter.write(dataArray.get(i) + "\n");
           }
           bufferedWriter.write(dataArray.get(dataArray.size()-1));

        }catch (IOException ioException){
            ioException.printStackTrace();

        }
        converter.ConvertFromCommafileToHexEncodedUTF8("src/testpeoplepedia.txt", "src/encodedfile.txt");
        Conversion.deleteFile("src/testpeoplepedia.txt");
    }

    private boolean compareCategoryIdentifiers(String cat1, String cat2){

        List<String> categoryList = Arrays.asList(cat1.split("#")) ;
        List<String> categoryList2 =Arrays.asList(cat2.split("#")) ;

        return categoryList.containsAll(categoryList2) && categoryList2.containsAll(categoryList);

    }

    //overloaded function version if you want to specify the files used
    public void deleteEntry(String category, String name, String commaSeperatedUrls, String listingFilename, String encodedFilename){
        converter.ConvertFromHexencodedFileToCommafile(encodedFilename, listingFilename);
        ArrayList<String> dataArray = new ArrayList<String>();
        String line;
        String dataString;


        //read in data into string array
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(listingFilename))) {
            while((line = bufferedReader.readLine()) != null){
                dataArray.add(line);
            }
        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        //remove listing
        for(int i = 0; i < dataArray.size(); i++){
            //get line
            dataString = dataArray.get(i);
            //split line into tokens
            String[] tokens = dataString.split(";");
            //remove entry matching token 0 and 1 (catagory and name) and if the string contains the urls
            if(compareCategoryIdentifiers(tokens[0], category) && tokens[1].equals(name) && dataString.contains(commaSeperatedUrls)){ //if line containing category and name found then remove it
                dataArray.remove(i);
                break;//jump out after first match
            }
        }

        //write to file
        //System.out.println("datasize="+dataArray.size());//debugging
        if(dataArray.size() > 0){ //if list is not empty write to new file
            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(listingFilename))) {
                for (int i = 0; i < dataArray.size()-1; i++){
                    bufferedWriter.write(dataArray.get(i) + "\n");
                }
                bufferedWriter.write(dataArray.get(dataArray.size()-1));

            }catch (IOException ioException){
                ioException.printStackTrace();

            }
        }
        else{
            try {//if you removed the last entry just empty the listing file
                new FileOutputStream(listingFilename).close();
                //opening an outputstream on a file discard its current contents
            }catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        converter.ConvertFromCommafileToHexEncodedUTF8(listingFilename, encodedFilename);
        Conversion.deleteFile(listingFilename);
    }


}
