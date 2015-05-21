import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by slus on 15-05-17.
 */
public class HuffmanCompression {

    private static int half;

    public static void main(String[] args){
        Leaf[] leafArray;
        String fileLocation = "/Users/slus/Desktop/";
        String fileName = "hello.txt";
        leafArray = createFreqTable(fileLocation+fileName);
        SecretCode[] secretCodeArray = new SecretCode[leafArray.length];

        //printArray("Before Sort", leafArray);
        leafArray = sortArray(leafArray);
        //printArray("After Sort", leafArray);
        //secretCodeArray = makeSecretCode(Leaf.makeTree(leafArray), secretCodeArray, 0);
        Leaf.makeTree(leafArray);
        //compress(fileLocation, fileName, secretCodeArray);
        //decompress(fileLocation, fileName);
    }

    private static void compress(String fileLocation, String fileName, SecretCode[] secretCodes) {
        try {
            FileInputStream inFile = new FileInputStream(fileLocation+fileName);
            FileOutputStream outFile = new FileOutputStream(fileLocation + fileName + ".huf");


            //Write SecretCode to header
            for (int i = 0; i < secretCodes.length; i++) {
                outFile.write((int)secretCodes[i].getSymbol());
                outFile.write(secretCodes[i].getSecretCode().getBytes());
                //Write start of text
                //outFile.write(012);
            }

            //Write converted Characters
            //outFile.write(002);
            int characters;
            SecretCode[] largeArray = new SecretCode[256];
            for (int i = 0; i < secretCodes.length; i++){
                largeArray[(int)secretCodes[i].getSymbol()] = secretCodes[i];
            }

            String toOutput = "";
            while(( characters = inFile.read()) != -1) {
                toOutput += largeArray[characters].getSecretCode();
            }
            //Write the text to the file
            outFile.write(toOutput.getBytes());
            outFile.close();
            inFile.close();

            }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void decompress(String fileLocation, String fileName){
        try {
            File file = new File(fileLocation+fileName+".huf");

            FileOutputStream toWrite = new FileOutputStream(fileLocation+"Uncompressed_"+fileName);

            String textLine;
            String toDecode = "";
            SecretCode[] secretCode = new SecretCode[256];
            int arrayIndex = 0;
            boolean isNewLine = false;

            //Read header
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            while ((textLine = raf.readLine()) !=  null){
                byte[] byteTest = new byte[textLine.length()];
                if(textLine.length() == 0) {
                    textLine = raf.readLine();
                    byteTest = textLine.getBytes();
                    isNewLine = true;
                }
                else{
                    byteTest = textLine.getBytes();
                }


                    if (byteTest[0] != 002) {
                        SecretCode tempSecret = null;
                        if(isNewLine){
                            tempSecret = new SecretCode((char)012, new String(byteTest).substring(1));
                            isNewLine = false;
                        }else {
                            tempSecret = new SecretCode((char)  byteTest[0], new String(byteTest).substring(1));
                        }
                        secretCode[arrayIndex] = tempSecret;
                        arrayIndex++;
                    } else {
                        toDecode = new String(byteTest).substring(1);
                        System.out.println("In binary" + toDecode);
                    }
                //}
            }

            SecretCode[] compactSecret = new SecretCode[arrayIndex];
            for (int i = 0; i < arrayIndex; i++){
                compactSecret[i] = secretCode[i];
            }

            toWrite.write(decode(toDecode,compactSecret,"").getBytes());

            toWrite.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static String decode(String toDecode, SecretCode[] secretCodes, String result) {
        while(toDecode.length() != 0) {
            String toCompare = "";
            int maxIndex = secretCodes[secretCodes.length-1].getSecretCode().length();
            int index = -1;
            //Do while O(n)
            do {
                index++;
                char temp = toDecode.charAt(index);
                toCompare += temp;
            } while (toDecode.charAt(index) != '0' && toCompare.length() < maxIndex);
            result += secretCodes[toCompare.length()-1].getSymbol();
            toDecode = toDecode.substring(toCompare.length());
        }
        return result;
    }

    private static SecretCode[] makeSecretCode(Leaf node, SecretCode[] secretCodeArray, int lastPosition) {
        String goLeft = "0";
        String goRight = "1";
        SecretCode code = new SecretCode();
        if(node.isRoot()){
            code.setSymbol(node.getLeftLeaf().getSymbol());
            code.setSecretCode(code.getSecretCode() + goLeft);
            if(node.getRightLeaf().isNode){
                node.getRightLeaf().setBranchValueToParent(goRight);
            }
            secretCodeArray[lastPosition] = code;
            lastPosition++;
            makeSecretCode(node.getRightLeaf(), secretCodeArray, lastPosition);
        }
        else{
            if(node.isNode){
                code.setSymbol(node.getLeftLeaf().getSymbol());
                code.setSecretCode(node.branchValueToParent + goLeft);
                node.getRightLeaf().setBranchValueToParent(node.getBranchValueToParent() + goRight);
                secretCodeArray[lastPosition] = code;
                lastPosition++;
                makeSecretCode(node.getRightLeaf(), secretCodeArray, lastPosition);
            }
            //NOT A NODE END OF TREE
            else{
                code.setSymbol(node.getSymbol());
                code.setSecretCode(node.getBranchValueToParent() + goRight);
                secretCodeArray[lastPosition] = code;
            }
        }
        return secretCodeArray;
    }

    //Heap Sort. Most consistent O(n log(n))
    //FROM http://www.sanfoundry.com/java-program-implement-heap-sort/
    private static Leaf[] sortArray(Leaf[] leafArray) {
        makeHeap(leafArray);
        for(int i = half; i > 0; i--){
            swapValues(leafArray,0, i);
            half -= 1;
            swapLargeElement(leafArray,0);
        }
        return leafArray;
    }

    public static void makeHeap(Leaf[] leafArray){
        half = leafArray.length-1;
        for (int i = half/2; i >= 0; i--){
            swapLargeElement(leafArray, i);
        }
    }

    private static void swapLargeElement(Leaf[] leafArray, int i) {
        int leftValue = 2*i;
        int rightValue = 2*i + 1;
        int maxValue = i;

        if(leftValue <= half && leafArray[leftValue].getFrequency() > leafArray[i].getFrequency()){
            maxValue = leftValue;
        }
        if(rightValue <= half && leafArray[rightValue].getFrequency() > leafArray[maxValue].getFrequency()){
            maxValue = rightValue;
        }

        if(maxValue != i){
            swapValues(leafArray, i, maxValue);
            swapLargeElement(leafArray, maxValue);
        }
    }

    private static void swapValues(Leaf[] leafArray, int i, int maxValue) {
        Leaf tempLeaf = leafArray[i];
        leafArray[i] = leafArray[maxValue];
        leafArray[maxValue] = tempLeaf;
    }

    private static void printArray(String message, Leaf[] leafArray) {
        System.out.println(message);
        for(int i = 0; i < leafArray.length; i++){
            if(leafArray[i] != null) {
                System.out.println("Character: " + leafArray[i].getSymbol() + " #Frequency: " + leafArray[i].getFrequency());
            }
        }
        System.out.println("Array is of size: " + leafArray.length);
    }

    private static Leaf[] createFreqTable(String directory) {
        Leaf[] charArray = new Leaf[256];
        List<Integer> usedChar = new ArrayList<>();
        try{
            FileInputStream file = new FileInputStream(directory);
            int characters;
            while(( characters = file.read()) != -1) {
                if(characters <=256) {
                    char c = (char) characters;
                    if(charArray[characters]==null){
                        Leaf newChar = new Leaf(c,1);
                        charArray[characters] = newChar;
                        usedChar.add(characters);
                    }
                    else{
                        charArray[characters].setFrequency(charArray[characters].getFrequency()+1);
                    }
                }
            }
            file.close();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("The file you have requested does not exist");
        }

        Leaf[] betterCharArray = new Leaf[usedChar.size()];
        for(int i= 0; i <= usedChar.size()-1; i++){
            betterCharArray[i] = charArray[usedChar.get(i)];
            betterCharArray[i] = charArray[usedChar.get(i)];
        }
        return betterCharArray;
    }
}