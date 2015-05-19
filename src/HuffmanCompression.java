import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slus on 15-05-17.
 */
public class HuffmanCompression {

    private static int length;

    public static void main(String[] args){
        Leaf[] leafArray;
        String fileLocation = "/Users/slus/Desktop/";
        String fileName = "hello.txt";
        leafArray = createFreqTable(fileLocation+fileName);
        SecretCode[] secretCodeArray = new SecretCode[leafArray.length];

        printArray("Before Sort", leafArray);
        sortArray(leafArray);
        printArray("After Sort", leafArray);
        secretCodeArray = makeSecretCode(Leaf.makeTree(leafArray), secretCodeArray, 0);
        compress(fileLocation, fileName, secretCodeArray);
    }

    private static void compress(String fileLocation, String fileName, SecretCode[] secretCodes) {
        try {
            FileInputStream inFile = new FileInputStream(fileLocation+fileName);
            FileOutputStream outFile = new FileOutputStream(fileLocation + fileName + ".huf");

            //Write SecretCode to header
            for (int i = 0; i < secretCodes.length; i++) {
                int secretCodeLength = secretCodes[i].getSecretCode().length();
                char[] charArray;
                charArray = secretCodes[i].getSecretCode().toCharArray();
                for (int j= 0; j < secretCodeLength; j++){
                    //outFile.write((byte)charArray[j]);
                    outFile.write(Integer.bitCount(1));
                }

            }

            }catch(IOException e){
            e.printStackTrace();
        }
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
    private static void sortArray(Leaf[] leafArray) {
        makeHeap(leafArray);
        for(int i = length; i > 0; i--){
            swapValues(leafArray,0, i);
            length -= 1;
            swapLargeElement(leafArray,0);
        }
    }

    public static void makeHeap(Leaf[] leafArray){
        length = leafArray.length-1;
        for (int i = length/2; i >= 0; i--){
            swapLargeElement(leafArray, i);
        }
    }

    private static void swapLargeElement(Leaf[] leafArray, int i) {
        int leftValue = 2*i;
        int rightValue = 2*i + 1;
        int maxValue = i;

        if(leftValue <= length && leafArray[leftValue].getFrequency() > leafArray[i].getFrequency()){
            maxValue = leftValue;
        }
        if(rightValue <= length && leafArray[rightValue].getFrequency() > leafArray[maxValue].getFrequency()){
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