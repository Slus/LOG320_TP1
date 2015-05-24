import java.util.ArrayList;
import java.util.List;

/**
 * Created by slus on 15-05-17.
 */
public class Leaf {
    char symbol;
    int frequency;
    Leaf parentNode;
    Leaf leftLeaf;
    Leaf rightLeaf;
    boolean isNode;
    boolean isRoot;
    String leafCode;
    boolean wasVisited = false;
    int position;


    public Leaf() {
        this.symbol = '\0';
        this.frequency = 0;
        this.parentNode = null;
        this.leftLeaf = null;
        this.rightLeaf = null;
        this.isNode = false;
        this.isRoot = false;
        this.leafCode = "";
    }

    public Leaf(Leaf leaf) {
        this.symbol = leaf.getSymbol();
        this.frequency = leaf.getFrequency();
        this.parentNode = leaf.getParentNode();
        this.leftLeaf = leaf.getLeftLeaf();
        this.rightLeaf = leaf.getRightLeaf();
        this.isNode = leaf.isNode();
        this.isRoot = leaf.isRoot();
        this.leafCode = leaf.getLeafCode();
    }

    public Leaf(char symbol, int frequency, Leaf charNode){
        this.symbol = symbol;
        this.frequency = frequency;
        this.parentNode = charNode;
        this.leftLeaf = null;
        this.rightLeaf = null;
        this.isNode = false;
        this.isRoot = false;
        this.leafCode = "";
    }

    public Leaf(char symbol, int frequency, int position){
        this.symbol = symbol;
        this.frequency = frequency;
        this.parentNode = null;
        this.leftLeaf = null;
        this.rightLeaf = null;
        this.isNode = false;
        this.isRoot = false;
        this.leafCode = "";
        this.position = position;
    }

    public Leaf(Leaf leaf1, Leaf leaf2){
        this.isNode = true;
        this.frequency = leaf1.getFrequency() + leaf2.getFrequency();
        if(leaf1.isNode){
            this.rightLeaf = leaf1;
            this.leftLeaf = leaf2;
        }
        else if(leaf2.isNode){
            this.rightLeaf = leaf2;
            this.leftLeaf = leaf1;
        }
        else {
            this.rightLeaf = leaf1;
            this.leftLeaf = leaf2;
        }

        leaf1.setParentNode(this);
        leaf2.setParentNode(this);


    }

    public Leaf createTree(int[] freqTable){
        List<Leaf> theTree = new ArrayList<>();

        //Get all non-null items in the array and store them into the arraylist
        for(int i = 0; i < freqTable.length; i++){
            if(freqTable[i] != 0){
                Leaf tempLeaf = new Leaf((char)i, freqTable[i], i);
                theTree.add(tempLeaf);
            }
        }

        //Sort arraylist by frequency
        HuffmanCompression.sortArray(theTree);

        if(theTree.size() == 1){
            Leaf lonelyLeaf = theTree.remove(0);
            Leaf templeaf = new Leaf('\0',0, 0);
            theTree.add(new Leaf(lonelyLeaf, templeaf));
        }
        else{
            while(theTree.size() != 1) {
                int insertPos = 0;
                Leaf leaf1 = theTree.remove(0);
                Leaf leaf2 = theTree.remove(0);

                Leaf node = new Leaf(leaf1, leaf2);

                //get position to place in theTree array
                while (insertPos < theTree.size() && node.getFrequency() > theTree.get(insertPos).getFrequency()) {
                    insertPos++;
                }
                //Add node to the correct location
                theTree.add(insertPos, node);
            }
        }
        theTree.get(0).setLeafCode("");
        return theTree.get(0);
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Leaf getParentNode() {
        return parentNode;
    }

    public void setParentNode(Leaf parentNode) {
        this.parentNode = parentNode;
    }

    public Leaf getLeftLeaf() {
        return leftLeaf;
    }

    public void setLeftLeaf(Leaf leftLeaf) {
        this.leftLeaf = leftLeaf;
    }

    public Leaf getRightLeaf() {
        return rightLeaf;
    }

    public void setRightLeaf(Leaf rightLeaf) {
        this.rightLeaf = rightLeaf;
    }

    public boolean isNode() {
        return isNode;
    }

    public void setIsNode(boolean isNode) {
        this.isNode = isNode;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public String getLeafCode() {
        return leafCode;
    }

    public void setLeafCode(String leafCode) {
        this.leafCode = leafCode;
    }

    public boolean isWasVisited() {
        return wasVisited;
    }

    public void setWasVisited(boolean wasVisited) {
        this.wasVisited = wasVisited;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
