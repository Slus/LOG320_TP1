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
        if((int)leaf1.getSymbol() >= (int)leaf2.getSymbol()){
            leftLeaf = leaf1;
            rightLeaf = leaf2;
        }
        else {
            leftLeaf = leaf2;
            rightLeaf = leaf1;
        }
        this.isRoot = isRoot;
    }

    public static Leaf[] makeTree(Leaf[] leafArray){
        Leaf[] tree = new Leaf[leafArray.length];
        Leaf previousLeaf = null;
        int lastIndex = 1;
        for(int i = 0; i < leafArray.length; i++){
            if(tree[0] == null){
                Leaf leafNode = new Leaf(leafArray[0], leafArray[1], false);
                tree[i] = leafNode;
                previousLeaf = leafNode;
            }
            else{
                Leaf leafNode = new Leaf(previousLeaf, leafArray[lastIndex], false);
                lastIndex++;
                tree[i] = leafNode;
            }



        }
        return tree;
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
