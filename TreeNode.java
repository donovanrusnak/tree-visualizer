package sample;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TreeNode {

    //node variables
    private int data;
    private double xPos;
    private double yPos;
    private double childXDiff;
    private TreeNode parent;
    private TreeNode leftChild;
    private TreeNode rightChild;

    private Color nodeColor = Color.LIGHTGREY;
    private double lineWidth = 2;

    //node constants
    private final Color lineColor = Color.BLACK;
    private final int diameter = 30;
    private final Color outlineColor = Color.BLACK;
    private final Color textColor = Color.BLACK;
    private final Font font = Font.font("Tahoma", FontWeight.BOLD, 15);
    private final FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);

    //constructor
    public TreeNode(int data, double xPos, double yPos, double childXDiff, TreeNode parent) {
        this.data = data;
        this.xPos = xPos;
        this.yPos = yPos;
        this.childXDiff = childXDiff;
        this.parent = parent;
        this.leftChild = null;
        this.rightChild = null;
    }

    //draws node onto user interface
    public void draw(GraphicsContext gc){
        gc.setLineWidth(lineWidth);
        if(parent != null) {
            gc.setStroke(lineColor);
            gc.strokeLine(xPos+diameter/2, yPos, parent.getXPos()+diameter/2, parent.getYPos()+diameter);
        }
        gc.setFill(nodeColor);
        gc.fillOval(xPos, yPos, diameter, diameter);
        gc.setStroke(outlineColor);
        gc.strokeOval(xPos, yPos, diameter, diameter);
        gc.setFont(font);
        gc.setFill(textColor);
        double dataWidth = fontMetrics.computeStringWidth(Integer.toString(data));
        double dataHeight = fontMetrics.getAscent();
        gc.fillText(Integer.toString(data), xPos + (diameter-dataWidth)/2, yPos + (5*diameter)/12 + dataHeight/2);
    }

    //removes node from user interface
    public void erase(GraphicsContext gc){
        double upperLeftX, upperLeftY;
        double width, height;
        if(parent != null){
            upperLeftX = Math.min(parent.getXPos() + diameter/2, xPos);
            upperLeftY = parent.getYPos() + diameter + 2;
            width = Math.abs(xPos - parent.getXPos()) + diameter/2;
            height = Math.abs(yPos - parent.getYPos());
        }else{
            upperLeftX = xPos;
            upperLeftY = yPos;
            width = diameter;
            height = diameter;
        }
        gc.clearRect(upperLeftX, upperLeftY, width, height);
    }

    //removes node and all its children from user interface
    public void eraseSubtree(GraphicsContext gc) {
        erase(gc);
        if(leftChild != null) leftChild.eraseSubtree(gc);
        if(rightChild != null) rightChild.eraseSubtree(gc);
    }

    public int getData(){
        return data;
    }

    public void setData(int data){
        this.data = data;
    }

    public double getXPos(){
        return xPos;
    }

    public double getYPos(){
        return yPos;
    }

    public double getChildXDiff(){
        return childXDiff;
    }

    public TreeNode getParent(){
        return parent;
    }

    public TreeNode getLeftChild(){
        return leftChild;
    }

    public void setLeftChild(TreeNode leftChild){
        this.leftChild = leftChild;
    }

    public TreeNode getRightChild(){
        return rightChild;
    }

    public void setRightChild(TreeNode rightChild){
        this.rightChild = rightChild;
    }

    public void setNodeColor(Color nodeColor){
        this.nodeColor = nodeColor;
    }

    public void setLineWidth(double lineWidth){
        this.lineWidth = lineWidth;
    }
}
