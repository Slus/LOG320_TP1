import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HuffmanCompression{
    public static void main(String[] args){
        char charTable[][] = new char[256][2];
        List<Integer> usedChar = new ArrayList<>();
        try{
            FileInputStream file = new FileInputStream("/Users/slus/Desktop/hello.txt");
            int characters;

            //I guess this runs O(n) "Amount of characters in text"
            while(( characters = file.read()) != -1) {
                 char c = (char)characters;
                 System.out.print(c);
                 int charAscii = c;

                 if(charAscii <= 256) {
                     if (charTable[charAscii][0] == '\0') {
                         charTable[charAscii][0] = c;
                         charTable[charAscii][1] = '1';
                         usedChar.add((int)c);
                     } else {
                         int frequency = charTable[charAscii][1];
                         int newFrequency = frequency + 1;
                         charTable[charAscii][1] = (char)newFrequency;
                     }
                 }
             }

            //Make new nonNullArray O(n)
            char[][] newBetterArray = new char[usedChar.size()][2];
            for(int i= 0; i <= usedChar.size()-1; i++){
                newBetterArray[i][0] = charTable[usedChar.get(i)][0];
                newBetterArray[i][1] = charTable[usedChar.get(i)][1];
            }

            System.out.println("AFTER SORT");

            //O(n log(n))
            sortTable(newBetterArray);

            //O(n)
            for (int i = 0; i <= newBetterArray.length-1; i++){
                System.out.println(newBetterArray[i][0] + " " + newBetterArray[i][1]);
            }

            System.out.println("Amount of character in new Array " + newBetterArray.length);

        }catch(IOException e){
            e.printStackTrace();
            System.out.println("The file you have requested does not exist");

        }
    }

    private static int length;

    //heap sort
    //FROM http://www.sanfoundry.com/java-program-implement-heap-sort/
    public static void sortTable(char[][] charTable){
        makeHeap(charTable);
        for(int i = length; i > 0; i--){
            swapValues(charTable,0, i);
            length -= 1;
            swapLargeElement(charTable,0);
        }
    }


    public static void makeHeap(char[][] charTable){
        length = charTable.length-1;
        for (int i = length/2; i >= 0; i--){
            swapLargeElement(charTable, i);
        }
    }

    private static void swapLargeElement(char[][] charTable, int i) {
        int leftValue = 2*i;
        int rightValue = 2*i + 1;
        int maxValue = i;

        if(leftValue <= length && charTable[leftValue][1] > charTable[i][1]){
            maxValue = leftValue;
        }
        if(rightValue <= length && charTable[rightValue][1] > charTable[maxValue][1]){
            maxValue = rightValue;
        }

        if(maxValue != i){
            swapValues(charTable, i, maxValue);
            swapLargeElement(charTable, maxValue);
        }
    }

    private static void swapValues(char[][] charTable, int i, int maxValue) {
        int tempValue = charTable[i][1];
        char tempChar = charTable[i][0];
        charTable[i][1] = charTable[maxValue][1];
        charTable[i][0] = charTable[maxValue][0];
        charTable[maxValue][1] = (char)tempValue;
        charTable[maxValue][0] = tempChar;
    }
}