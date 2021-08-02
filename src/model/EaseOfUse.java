package model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.*;

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
    public static void writeToFile(String message, String filename){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
            bufferedWriter.write(message);
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    public static String readFromfile(String filename){
        String result = "";
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            result = bufferedReader.readLine().trim();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        return result;
    }
}
