package model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.*;
import java.net.URISyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EaseOfUse {
    public EaseOfUse() {
    }

    public static String getHexFromPaint(Paint paint){

        Color c = (Color) paint;

        String hex = String.format( "#%02X%02X%02X",
                (int)( c.getRed() * 255 ),
                (int)( c.getGreen() * 255 ),
                (int)( c.getBlue() * 255 ) );

        return hex;
    }
    //writes to file in current directory
    public static void writeToFile(String message, String filename){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
            bufferedWriter.write(message);
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    public static String readFromfileInsideJar(String filename){
        String result = "";
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(filename)))) {
            result = bufferedReader.readLine().trim();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        return result;
    }
    public static String readFromfile(String filename){
        String result = "";
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            result = bufferedReader.readLine().trim();
        }catch(IOException ioException){
            ioException.printStackTrace();
            //if file cant be read set default choice as 1
            EaseOfUse.writeToFile("1",filename);
            return "1";
        }
        return result;
    }
    //unzips files to current directory
    public void unzipFile(String zipFilePath) {
        try {
            //create input stream on zipfile
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(getDirPath() + "/chromedriver.zip"));
            ZipEntry zipEntry;
            //get next entry
            zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) { //
                //create file object of zipentry in DIR
                File newFile = new File(getDirPath() + "/" + zipEntry.getName());
                if (zipEntry.isDirectory()) {//if zip entry is DIR make file DIR
                    newFile.mkdir();
                } else { //else make normal file
                    //create parent file for zipentry since there is no zipentry for root files
                    File parentFile = newFile.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdir();
                    }

                    byte[] data = zipInputStream.readAllBytes();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));

                    bufferedOutputStream.write(data);

                    //get enxt entry
                    zipEntry = zipInputStream.getNextEntry();

                }


            }
            zipInputStream.close();
        }
        catch (IOException ioException ){
            ioException.printStackTrace();
        }

    }
    public String getDirPath(){
        File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        File jarDirPath = jarFile.getParentFile();
        return jarDirPath.getPath();
    }
}
