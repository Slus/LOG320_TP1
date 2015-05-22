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
    String branchValueToParent;

    public boolean isWasVisited() {
        return wasVisited;
    }

    public void setWasVisited(boolean wasVisited) {
        this.wasVisited = wasVisited;
    }

    boolean wasVisited = false;


    public Leaf() {
        this.symbol = '\0';
        this.frequency = 0;
        this.parentNode = null;
        this.leftLeaf = null;
        this.rightLeaf = null;
        this.isNode = false;
        this.isRoot = false;
        this.branchValueToParent = "";
    }

    public Leaf(Leaf leaf) {
        this.symbol = leaf.getSymbol();
        this.frequency = leaf.getFrequency();
        this.parentNode = leaf.getParentNode();
        this.leftLeaf = leaf.getLeftLeaf();
        this.rightLeaf = leaf.getRightLeaf();
        this.isNode = leaf.isNode();
        this.isRoot = leaf.isRoot();
        this.branchValueToParent = leaf.getBranchValueToParent();
    }

    public Leaf(char symbol, int frequency, Leaf charNode){
        this.symbol = symbol;
        this.frequency = frequency;
        this.parentNode = charNode;
        this.leftLeaf = null;
        this.rightLeaf = null;
        this.isNode = false;
        this.isRoot = false;
        this.branchValueToParent = "";
    }

    public Leaf(char symbol, int frequency){
        this.symbol = symbol;
        this.frequency = frequency;
        this.parentNode = null;
        this.leftLeaf = null;
        this.rightLeaf = null;
        this.isNode = false;
        this.isRoot = false;
        this.branchValueToParent = "";
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

    public static Leaf makeTree(List<Leaf> leafArray){


        Leaf previousLeaf = null;

        while (leafArray.size() != 1){
            Leaf firstLeafNode = new Leaf(leafArray.get(0), leafArray.get(1));
            System.out.println(" first  element is: " + leafArray.get(0).getSymbol() + " with frequency = " + leafArray.get(0).getFrequency() );
            System.out.println(" first  element is: " + leafArray.get(1).getSymbol() + " with frequency = " + leafArray.get(1).getFrequency() );

            previousLeaf = firstLeafNode;

            List<Leaf> temp = new ArrayList<>();
            //Leaf[] temp = new Leaf[leafArray.length-1];
            int index = 0;
            int insertPos = 0;

            leafArray.remove(0);
            leafArray.remove(0);

            // I want to creat a new array of size leafArray.length-2 so that I can remove the used leaves.
            //Also if the frequency of the node that was just created is less than the next item in the original
            //array input the new node first
            for(int i = 0; i < leafArray.size(); i++){
                if(leafArray.get(i) != null) {
                    if (leafArray.get(i).getFrequency() <= previousLeaf.getFrequency()) {
                        insertPos++;
                    }
                }
            }
            leafArray.add(insertPos, previousLeaf);
        }

        //previousLeaf.setIsRoot(true);
        return previousLeaf;
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

    public String getBranchValueToParent() {
        return branchValueToParent;
    }

    public void setBranchValueToParent(String branchValueToParent) {
        this.branchValueToParent = branchValueToParent;
    }

}
