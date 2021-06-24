package model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Conversion {

    public Conversion(){


    }

    public void ConvertFromCommafileToHexEncodedUTF8(String filename){
        File file = new File(filename);
        String filestring = "";
        String currentline;
        byte[] byteArray = new byte[(int)file.length()];
        String hex;


        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while ((currentline = bufferedReader.readLine()) != null ){
                filestring = filestring + currentline + "\n";
            }
            byteArray = filestring.getBytes(StandardCharsets.UTF_8);



        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src\\encodedfile.txt"))) {

            hex = String.format("%02x", byteArray[0]);

            bufferedWriter.write(hex);
            for (int i = 1; i < byteArray.length; i++){
                hex = String.format("%02x", byteArray[i]);
                bufferedWriter.write(" " + hex);

            }

        }catch (IOException ioException){
            ioException.printStackTrace();
        }


    }
    public void readhexencodedFile(String filename){
        String fileline;
        String[] filelineArray;
        ArrayList<Byte> byteArray = new ArrayList<Byte>();
        byte hexAsByte;


        try(  BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));) {
            while((fileline = bufferedReader.readLine()) !=  null){
                filelineArray = fileline.split(" ");




                for(int i = 0; i < filelineArray.length; i++ ){
                    hexAsByte = Byte.parseByte(filelineArray[i], 16);
                    byteArray.add(hexAsByte);
                }


            }


            byte[] bytearrayForConversion = convertToPrimitiveByteArray(byteArray) ;

            String string = new String(bytearrayForConversion);
            System.out.println(string);

        }catch (IOException ioException){
            ioException.printStackTrace();

        }

    }

    public static byte[] convertToPrimitiveByteArray(ArrayList<Byte> bytearraylist){
        byte[] returnArray = new byte[bytearraylist.size()];

        for (int i = 0; i < bytearraylist.size(); i++ ){
            returnArray[i] = bytearraylist.get(i).byteValue();
        }

        return returnArray;
    }


}
