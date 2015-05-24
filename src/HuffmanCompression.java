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

    public static void main(String[] args) {
        String fileLocation = "/Users/slus/Desktop/";
        String fileName = "hello.txt";

        //Create Frequency table
        int[] freqTable = createFreqTable(fileLocation + fileName);

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
            //fi.read(toDecompress);

            System.out.println("PLEASE WORK " + Integer.toBinaryString(toDecompress[0]));
            String toDecode = "";

            String EOF = "100000000";
            String test = EOF.substring(0, EOF.length()-1);

            RandomAccessFile raf = new RandomAccessFile(file, "r");

            int testByte = raf.readUnsignedByte();
            int testByte2 = raf.readUnsignedByte();



            for(int i = 0; i < toDecompress.length; i++){
                if(Integer.toBinaryString(toDecompress[i]).equals(EOF.substring(0, EOF.length()-1))){
                    System.out.print("WOT?");
                    if(Integer.toBinaryString(toDecompress[i+1]).equals("00000000")){
                        //Done Reading
                    }
                }
                else{
                    System.out.println("THIS IS WHAT I FUCKING READ: "  + Integer.toBinaryString(toDecompress[i]));
                }
            }


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void compress(String fileLocation, String fileName) {
        try {
            FileInputStream inFile = new FileInputStream(fileLocation+fileName);
            FileOutputStream outFile = new FileOutputStream(fileLocation + fileName + ".huf");

            String toConvertToByte = "";
            //Write SecretCode to header
            for (int i = 0; i < secretCodes.length; i++) {
                if(secretCodes[i] != null){
                    toConvertToByte += Integer.toBinaryString(i);
                    toConvertToByte += secretCodes[i];
                }
            }


            int characters;
            //add all character code equivalent to the string
            while(( characters = inFile.read()) != -1) {
                toConvertToByte += secretCodes[characters];
            }

            System.out.println("BEFORE ADDING EOF: " + toConvertToByte.length());

            System.out.println("BEFORE ADDING TRAILING ZEROES: " + toConvertToByte.length());


            //Add EOF
            //toConvertToByte += Integer.toBinaryString(256);

            System.out.println("LETS FUCKING TRY THIS " + Integer.toBinaryString((int)'Ā'));

            int missingZeros = toConvertToByte.length()%8;
            for(int i = 0; i < 8-missingZeros; i++){
                toConvertToByte += "0";
            }

            System.out.println("AFTER ADDING TRAILING ZEROES: " + toConvertToByte.length());
            System.out.println("THIS IS THE FULL STRING: " + toConvertToByte);
            System.out.println("String is of size: " + toConvertToByte.length());
            System.out.println("The Loop should execute: " + toConvertToByte.length()/8+ " times" );

            //Amount of times we should cut the string into 8 bytes
            int iterations = toConvertToByte.length()/8;
            int counter = 0;
            for(int j = 0; j < iterations; j++){
                outFile.write(Byte.parseByte(toConvertToByte.substring(0, 7), 2));
                counter++;
                toConvertToByte = toConvertToByte.substring(7);
            }
            System.out.println("The Loop executed: " + counter + " times" );


            outFile.close();
            inFile.close();

        }catch(IOException e){
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
                /*if(root.getLeftLeaf().getPosition() == 256){
                    secretCodes[256] = "100000000";
                }*/
            }
            if(root.getRightLeaf().getSymbol() != '\0'){
                secretCodes[root.getRightLeaf().getSymbol()] = root.getRightLeaf().getLeafCode();
                /*if(root.getRightLeaf().getPosition() == 256){
                    secretCodes[256] = "100000000";
                }*/
            }
            makeSecretCode(root.getRightLeaf());
            root = root.getLeftLeaf();
        }

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

            usedChar[256] = 1;
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