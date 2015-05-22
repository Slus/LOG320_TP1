import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slus on 15-05-17.
 */
public class HuffmanCompression {

    private static int half;
    private static List<SecretCode> secretArray = new ArrayList<>(255);


    public static void main(String[] args){
        List<Leaf> leafArray = new ArrayList<>();
        //Leaf[] leafArray;
        String fileLocation = "/Users/slus/Desktop/";
        String fileName = "hello.txt";
        leafArray = createFreqTable(fileLocation + fileName);
        SecretCode[] secretCodeArray = new SecretCode[leafArray.size()];

        //printArray("Before Sort", leafArray);
        leafArray = sortArray(leafArray);
        //printArray("After Sort", leafArray);
        generateSecretCode(Leaf.makeTree(leafArray), secretCodeArray);
        //Leaf.makeTree(leafArray);
        compress(fileLocation, fileName);
        decompress(fileLocation, fileName);
    }

    private static void compress(String fileLocation, String fileName) {
        try {
            FileInputStream inFile = new FileInputStream(fileLocation+fileName);
            FileOutputStream outFile = new FileOutputStream(fileLocation + fileName + ".huf");


            //Write SecretCode to header
            for (int i = 0; i < secretArray.size(); i++) {
                outFile.write((int)secretArray.get(i).getSymbol());
                outFile.write(secretArray.get(i).getSecretCode().getBytes());
                //Write start of text
                outFile.write(012);
            }

            //Write converted Characters
            outFile.write(002);
            int characters;
            SecretCode[] arrayOfCode = new SecretCode[255];
            for (int i = 0; i < secretArray.size(); i++){
                arrayOfCode[(int)secretArray.get(i).getSymbol()] = secretArray.get(i);
            }
            String toOutput = "";
            while(( characters = inFile.read()) != -1) {
                toOutput += arrayOfCode[(int)characters].getSecretCode();
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
                        tempSecret = new SecretCode((char)012, new String(byteTest));
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

            toWrite.write(decode(toDecode, compactSecret, "").getBytes());

            toWrite.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static String decode(String toDecode, SecretCode[] secretCodes, String result) {
        int indexOfDecode;



    }

        /*while(toDecode.length() != 0) {
            String toCompare = "";
            int maxIndex = secretCodes[secretCodes.length - 1].getSecretCode().length();
            int index = 1;
            boolean isFound = false;
            int arrayPos = 0;
            int otherIndex = 0;
            List<SecretCode> test = new ArrayList<>();
            String temp;

            while (toDecode.length() != 0)
                for (int i = 0; i < secretCodes.length; i++) {
                    temp = toDecode.substring(0, index);
                    if (secretCodes[i].getSecretCode().startsWith(temp)) {
                        test.add(secretCodes[i]);
                        int goodPosition = checkIfMore(test)
                        if () {
                            arrayPos = i;
                        } else {
                            arrayPos = -1;
                        }
                    }
                }
                otherIndex++;
                toDecode = toDecode.substring(arrayPos);
                result += secretCodes[arrayPos].getSymbol();
            }


            //Do while O(n)
            /*do {
                index++;
                char temp = toDecode.charAt(index);
                toCompare += temp;

            } while (toCompare.length() < maxIndex || isFound);*/
            //toDecode = toDecode.substring(toCompare.length());
        return result;
    }

    private static boolean checkIfMore(String toDecode, List<SecretCode> test) {
        while(test.size() != 0){

        }
        return false;
    }

    public static void goLeft(Leaf node, SecretCode secretCode){
        //System.out.print(node.getFrequency() + "\n");
        //Gets me to left most leaf
        if(node.getLeftLeaf() != null && !node.getLeftLeaf().wasVisited) {
            node.setWasVisited(true);
            node.getLeftLeaf().setBranchValueToParent("0");
            goLeft(node.getLeftLeaf(), secretCode);

        } else if(node.getRightLeaf() != null){
            node.setWasVisited(true);
            node.getRightLeaf().setBranchValueToParent("1");
            goLeft(node.getRightLeaf(), secretCode);
        }
        else{
            node.setWasVisited(true);
            secretCode.setSecretCode(makeSecretCode(node));
            secretCode.setSymbol(node.getSymbol());

            secretArray.add(secretCode);
            SecretCode secretCode1 = new SecretCode();
            System.out.println(secretCode.getSecretCode());
            tryRight(node.getParentNode(), secretCode1);
        }
    }

    public static void tryRight(Leaf node, SecretCode secretCode){
        if(node.getRightLeaf() != null && !node.getRightLeaf().wasVisited){
            node.getRightLeaf().setWasVisited(true);
            node.getRightLeaf().setBranchValueToParent("1");
            goLeft(node.getRightLeaf(), secretCode);
        }
        else{
            if(node.getParentNode() != null){
                node.setWasVisited(true);
                //node.setBranchValueToParent("1");
                tryRight(node.getParentNode(), secretCode);
            }

        }
    }

    private static String makeSecretCode(Leaf node) {
        String secret = "";
        while(node.getParentNode() != null){
            secret =  node.getBranchValueToParent() + secret;
            node = node.getParentNode();
        }
        return secret;
    }

    private static void generateSecretCode(Leaf node, SecretCode[] secretCodeArray) {
        SecretCode code = new SecretCode();
        goLeft(node, code);
    }

    //Heap Sort. Most consistent O(n log(n))
    //FROM http://www.sanfoundry.com/java-program-implement-heap-sort/
    private static List<Leaf> sortArray(List<Leaf> leafArray) {
        makeHeap(leafArray);
        for(int i = half; i > 0; i--){
            swapValues(leafArray,0, i);
            half -= 1;
            swapLargeElement(leafArray,0);
        }
        return leafArray;
    }

    public static void makeHeap(List<Leaf> leafArray){
        half = leafArray.size()-1;
        for (int i = half/2; i >= 0; i--){
            swapLargeElement(leafArray, i);
        }
    }

    private static void swapLargeElement(List<Leaf> leafArray, int i) {
        int leftValue = 2*i;
        int rightValue = 2*i + 1;
        int maxValue = i;

        if(leftValue <= half && leafArray.get(leftValue).getFrequency() > leafArray.get(i).getFrequency()){
            maxValue = leftValue;
        }
        if(rightValue <= half && leafArray.get(rightValue).getFrequency() > leafArray.get(maxValue).getFrequency()){
            maxValue = rightValue;
        }

        if(maxValue != i){
            swapValues(leafArray, i, maxValue);
            swapLargeElement(leafArray, maxValue);
        }
    }

    private static void swapValues(List<Leaf> leafArray, int i, int maxValue) {
        Leaf tempLeaf = leafArray.get(i);
        leafArray.set(leafArray.indexOf(leafArray.get(i)), leafArray.get(maxValue));
        leafArray.set(maxValue, tempLeaf);
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

    private static List<Leaf> createFreqTable(String directory) {
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
                        charArray[characters].setFrequency(charArray[characters].getFrequency() + 1);
                    }
                }
            }
            file.close();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("The file you have requested does not exist");
        }

        List<Leaf> betterCharArray = new ArrayList<>();
        for(int i= 0; i <= usedChar.size()-1; i++){
            betterCharArray.add(charArray[usedChar.get(i)]);
        }
        return betterCharArray;
    }
}