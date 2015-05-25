import sun.text.IntHashtable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slus on 15-05-17.
 */
public class HuffmanCompression {

    static int half;

    //Allows to store the secretCodes and access them via the (int)char
    static String[] secretCodes = new String[257];
    static int[] freqTable;

    public static void main(String[] args) {
        String fileLocation = "/Users/slus/Desktop/";
        String fileName = "hello.txt";

        //Create Frequency table
        freqTable = createFreqTable(fileLocation + fileName);

        //Create Tree and store the last leaf in root containing all other leaves
        Leaf root = new Leaf();
        root = root.createTree(freqTable);
        makeSecretCode(root);

        compress(fileLocation, fileName);
        decompress(fileLocation, fileName);
    }

    private static void decompress(String fileLocation, String fileName) {
        try {
            File file = new File(fileLocation + fileName + ".huf");
            FileOutputStream toWrite = new FileOutputStream(fileLocation + "Uncompressed_" + fileName);

            byte toDecompress[] = new byte[(int)file.length()];
            FileInputStream fi = new FileInputStream(file);
            fi.read(toDecompress);

            //Convert all bytes back to string
            String toDecode = "";

            int characterCount = Integer.parseInt(toDecompress[0]+"");
            int charsToProcess = Integer.parseInt(toDecompress[1]+"");
            int[] decodedFreqTable = new int[256];

            int bytesTraversed = 2;

            for(int i = bytesTraversed; i < toDecompress.length;i++){
                toDecode += makeByte(Integer.toBinaryString(toDecompress[i]));
            }

            while(characterCount != 0){
                int semicolonLocation = toDecode.indexOf("00111011");
                String fullString = toDecode.substring(0, semicolonLocation);
                bytesTraversed += (fullString.length()+16)/8;

                int positionToInsert = Integer.parseInt(fullString.substring(0,8),2);
                int frequency = Integer.parseInt(fullString.substring(8),2);
                decodedFreqTable[positionToInsert] = frequency;
                characterCount--;
            }

            //reset toDecode in order to get only everything after the header
            toDecode = "";

            //Make header with full bits
            for(int i = bytesTraversed; i < toDecompress.length;i++){
                toDecode += Integer.toBinaryString(toDecompress[i]&0xff);
            }

            System.out.println("Decoded string is " + toDecode);

            //Read header until character count is reached
            //While reading, add the values to a frequency table

            Leaf root = new Leaf();
            root = root.createTree(decodedFreqTable);

            String end = assignCode(root, toDecode, "", charsToProcess);
            System.out.println("The decompressed String is: " + end);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String makeByte(String value){

        int missingZeros = value.length()%8;
        for(int i = 0; i < 8-missingZeros; i++){
            value = "0" + value;
        }
        return value;
    }


    public static String padEndOfFile(String value){
        int missingZeros = value.length()%8;
        for(int i = 0; i < 8-missingZeros; i++){
            value +="0";
        }
        return value;
    }


    private static void compress(String fileLocation, String fileName) {
        try {
            FileInputStream inFile = new FileInputStream(fileLocation+fileName);
            FileOutputStream outFile = new FileOutputStream(fileLocation + fileName + ".huf");

            //String to write
            String toOuput = "";
            int characterCount = 0;

            //Write Char + Frequency to header
            //Format will be somthing like char+Freq+;
            //Ex: 1100001 1000000 00111011
            for (int i = 0; i < freqTable.length; i++){
                //Write char in byte
                if(freqTable[i] != 0){
                    //this is the char value in binary
                    toOuput += makeByte(Integer.toBinaryString(i));

                    //This is the frequency in binary
                    String testing = Integer.toBinaryString(freqTable[i]);
                    toOuput += makeByte(testing);

                    //this is the semicolon binary value
                    toOuput += "00111011";
                    characterCount++;
                }
            }

            //add char count at the beginning
            toOuput = makeByte(Integer.toBinaryString(characterCount)) + toOuput;

            //Write 00000010 for start of text
            //toOuput += "00000010";
            //write all chars in file as code and append everything together
            int characters;
            int charProcessed = 0;
            while(( characters = inFile.read()) != -1) {
                toOuput += secretCodes[characters];
                charProcessed++;
            }
            //Write EOF char basically 256 in binary
            //toOuput += makeByte("100000000");
            toOuput = makeByte(Integer.toBinaryString(charProcessed)) + toOuput;

            toOuput = padEndOfFile(toOuput);

            writeToFile(toOuput, outFile);

            outFile.close();
            inFile.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void writeToFile(String toOutput, FileOutputStream outFile) {
        try {
            int iterations = toOutput.length() / 8;
            int counter = 0;
            for (int j = 0; j < iterations; j++) {
                outFile.write(Integer.parseInt(toOutput.substring(0, 8),2));
                System.out.println("This is the value that will be written: " + Integer.parseInt(toOutput.substring(0, 8)));
                counter++;
                toOutput = toOutput.substring(8);
            }
            System.out.println("The Loop executed: " + counter + " times");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void makeSecretCode(Leaf root) {
        //If leaf has left leaf, it automatically has a right leaf
        while(root.getLeftLeaf() != null){
            root.getLeftLeaf().setLeafCode(root.getLeafCode() + "0");
            root.getRightLeaf().setLeafCode(root.getLeafCode() + "1");
            if(root.getLeftLeaf().getSymbol() != '\0') {
                secretCodes[root.getLeftLeaf().getSymbol()] = root.getLeftLeaf().getLeafCode();
            }
            if(root.getRightLeaf().getSymbol() != '\0'){
                secretCodes[root.getRightLeaf().getSymbol()] = root.getRightLeaf().getLeafCode();
            }
            makeSecretCode(root.getRightLeaf());
            root = root.getLeftLeaf();
        }

    }

    private static String assignCode(Leaf root, String codes, String out, int charsToProcess) {
        if(charsToProcess >= 0){
            if(root.isNode) {
                if (codes.charAt(0) == '0') {
                    assignCode(root.getLeftLeaf(), codes.substring(1), out, charsToProcess--);
                } else {
                    assignCode(root.getRightLeaf(), codes.substring(1), out, charsToProcess--);
                }
            }
            else{
                if(codes.charAt(0) == '0'){
                    out += root.getSymbol();
                }
                else {
                    out += root.getSymbol();
                }
            }
        }
        else{
            return out;
        }
        return out;
    }

    public static int[] createFreqTable(String directory) {
        int[] usedChar = new int[257];
        try {
            FileInputStream file = new FileInputStream(directory);
            int character;
            //at position of the character decimal value, increment the frequency
            while ((character = file.read()) != -1) {
                usedChar[character] += 1;
            }

            //usedChar[256] = 1;
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("The file you have requested does not exist");
        }
        return usedChar;
    }

    //Heap Sort. Most consistent O(n log(n))
    //FROM http://www.sanfoundry.com/java-program-implement-heap-sort/
    public static List<Leaf> sortArray(List<Leaf> leafArray) {
        makeHeap(leafArray);
        for (int i = half; i > 0; i--) {
            swapValues(leafArray, 0, i);
            half -= 1;
            swapLargeElement(leafArray, 0);
        }
        return leafArray;
    }

    public static void makeHeap(List<Leaf> leafArray) {
        half = leafArray.size() - 1;
        for (int i = half / 2; i >= 0; i--) {
            swapLargeElement(leafArray, i);
        }
    }

    private static void swapLargeElement(List<Leaf> leafArray, int i) {
        int leftValue = 2 * i;
        int rightValue = 2 * i + 1;
        int maxValue = i;

        if (leftValue <= half && leafArray.get(leftValue).getFrequency() > leafArray.get(i).getFrequency()) {
            maxValue = leftValue;
        }
        if (rightValue <= half && leafArray.get(rightValue).getFrequency() > leafArray.get(maxValue).getFrequency()) {
            maxValue = rightValue;
        }

        if (maxValue != i) {
            swapValues(leafArray, i, maxValue);
            swapLargeElement(leafArray, maxValue);
        }
    }

    private static void swapValues(List<Leaf> leafArray, int i, int maxValue) {
        Leaf tempLeaf = leafArray.get(i);
        leafArray.set(leafArray.indexOf(leafArray.get(i)), leafArray.get(maxValue));
        leafArray.set(maxValue, tempLeaf);
    }
}