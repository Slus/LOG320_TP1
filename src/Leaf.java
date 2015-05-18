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

    public Leaf() {
        this.symbol = '\0';
        this.frequency = 0;
        this.parentNode = null;
        this.leftLeaf = null;
        this.rightLeaf = null;
        this.isNode = false;
        this.isRoot = false;
    }

    public Leaf(char symbol, int frequency, Leaf charNode){
        this.symbol = symbol;
        this.frequency = frequency;
        this.parentNode = charNode;
        this.leftLeaf = null;
        this.rightLeaf = null;
        this.isNode = false;
        this.isRoot = false;

    }

    public Leaf(char symbol, int frequency){
        this.symbol = symbol;
        this.frequency = frequency;
        this.parentNode = null;
        this.leftLeaf = null;
        this.rightLeaf = null;
        this.isNode = false;
        this.isRoot = false;

    }

    public Leaf(Leaf leaf1, Leaf leaf2, boolean isRoot){
        this.isNode = true;
        this.frequency = leaf1.getFrequency() + leaf2.getFrequency();
        this.symbol = '\0';

        if(leaf1.getSymbol() == '\0'){
            leaf1.setParentNode(this);
            leaf2.setParentNode(this);
            rightLeaf = leaf1;
            leftLeaf = leaf2;
        }
        else{
            if((int)leaf1.getSymbol() >= (int)leaf2.getSymbol()){
                leftLeaf = leaf2;
                rightLeaf = leaf1;
            }
            else {
                leftLeaf = leaf2;
                rightLeaf = leaf1;
            }
        }


        this.isRoot = isRoot;
    }

    public static Leaf[] makeTree(Leaf[] leafArray){
        Leaf[] tree = new Leaf[leafArray.length];
        Leaf previousLeaf = null;
        int startIndex = 2;
        Leaf firstLeafNode = new Leaf(leafArray[0], leafArray[1], false);
        previousLeaf = firstLeafNode;

        for(int i = 2; i < leafArray.length; i++){
            Leaf leafNode = new Leaf(previousLeaf, leafArray[i], false);
            previousLeaf = leafNode;
        }
        tree[0] = previousLeaf;
        return tree;

        /*take previousLeaf
                take third item from leafArray
                make node
                        previousLeaf = node
                                until there is no more item in leafAr*/
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
}
