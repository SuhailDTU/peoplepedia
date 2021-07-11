package model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
public class Conversion {

    public Conversion(){


    }

    //converts a commaseperated file to a  and saves it with a specific filename
    public void ConvertFromCommafileToHexEncodedUTF8(String filename, String convertedFileName){
        File file = new File(filename);
        String filestring = "";
        String currentline;
        byte[] byteArray = new byte[(int)file.length()];
        String hex;


        //read from listing file and fill in bytearray
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while ((currentline = bufferedReader.readLine()) != null ){
                filestring = filestring + currentline + "\n";
            }
            byteArray = filestring.getBytes(StandardCharsets.UTF_8);



        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        //write to file in hexencoded format
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(convertedFileName))) {

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


    public void ConvertFromHexencodedFileToCommafile(String filename, String convertedFilename){
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


            //write converted data to file
            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(convertedFilename))){
                bufferedWriter.write(string.trim()); //tri newline from end and write to file

            }catch(IOException ioException){
                ioException.printStackTrace();

            }

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

    //static function for deleting files
    public static void deleteFile(String fileName){
        File file = new File(fileName);
        file.delete();

    }
    public static void deleteFileOnExit(String fileName){
        File file = new File(fileName);
        file.deleteOnExit();

    }

    //encrypts or decrypts a file using AES depending on the mode
    //ciphermodes----
    //Cipher.ENCRYPT_MODE
    //Cipher.DECRYPT_MODE
    public static void encryptOrDecryptFile(String key, String inputFile, String outputfile, int mode){
        try {
            //make key 16 bytes or if already 16 bytes keep it
            byte[] keyBytes = key.getBytes();
            byte[] keyByteArray;

            if(keyBytes.length < 16){
                keyByteArray = new byte[16];
                for (int i = 0; i < keyBytes.length; i++){
                    keyByteArray[i] = keyBytes[i];
                }
            }else {
                keyByteArray = key.getBytes();
            }


            //make key and cypher object
            Key secretkey = new SecretKeySpec(keyByteArray, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, secretkey);

            //read file into bytearray
            File inputFileOBJ = new File(inputFile);
            FileInputStream fileInputStream = new FileInputStream(inputFileOBJ);
            byte[] inputArr = new byte[(int)inputFileOBJ.length()];
            fileInputStream.read(inputArr);
            fileInputStream.close();

            //encrypt byte array
            byte[] outputArr = cipher.doFinal(inputArr);

            //write to file
            File outputFileOBJ = new File(outputfile);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFileOBJ);
            fileOutputStream.write(outputArr);
            fileOutputStream.close();



        }
        catch (BadPaddingException badPaddingException){
            //ignore it
            //we handle not being able to encrypt it in the caller
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
        catch (Exception exception){
            exception.printStackTrace();
        }



    }




}
