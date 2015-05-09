import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HuffmanCompression{
    public static void main(String[] args){
        try{
            FileReader file = new FileReader("/Users/slus/Desktop/hello.txt");
            char[][] charArray = new char[256][2];
            int chars = file.read();
            while(chars != -1){

            }

        }catch(IOException e){
            e.printStackTrace();
            System.out.println("The file you have requested does not exist");

        }
    }

    public void compress(File file){

    }
}