package gui;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import exceptions.DuplicateNodeException;
import exceptions.MissingNodeException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.BinarySearchTree;
import model.TreeNode;

import static java.lang.Math.abs;
import static model.TreeNode.PathStatus.OFF;


public class BinaryTreeView {

    //user interface variables
    private BinarySearchTree tree;
    private GridPane grid;
    private Label nodeLabel;
    private Button insertButton;
    private Button removeButton;
    private Button findButton;
    private Button resetColoursButton;
    private Button clearButton;
    private HBox inputBox;
    private Canvas treeArea;
    private HBox errorBox;

    private final TextField nodeField;
    private final GraphicsContext gc;
    private final Text errorMessage;

    //user interface constants
    private final int treeAreaWidth = 1080;
    private final int treeAreaHeight = 360;
    private final int heightDifference = 60;
    private final int nodeDiameter = 30;
    private final double rootXPos = treeAreaWidth/2 - 5;
    private final double rootYPos = 20;
    private final double rootChildXDiff = rootXPos / 2;
    private final double neutralEdgeWidth = 2;
    private final double pathEdgeWidth = 3;

    private final Font font = Font.font("Tahoma", FontWeight.BOLD, 15);
    private final FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);

    private final Color lineColour = Color.BLACK;
    private final Color textColour = Color.BLACK;
    private final Color lessThanNodeColour = Color.color(1, 0, 0, 0.7);
    private final Color greaterThanNodeColour = Color.color(0, 0, 1, 0.7);
    private final Color equalNodeColour = Color.color(0,1, 0,0.7);
    private final Color neutralNodeColour = Color.LIGHTGREY;

    private final String noInputMessage = "Oops! You need to enter a node!";
    private final String notIntegerMessage = "Oops! A node needs to be an integer!";
    private final String duplicateNodeMessage = "Oops! That node is already in the tree!";
    private final String missingNodeMessage = "Oops! That node is not in the tree!";

    public BinaryTreeView() {

        tree = new BinarySearchTree();

        //initialize and set up grid (user interface)
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //initialize labels, text box, and buttons
        nodeLabel = new Label("Node:");
        nodeField = new TextField();
        insertButton = new Button("Insert");
        removeButton = new Button("Remove");
        findButton = new Button("Find");
        resetColoursButton = new Button("Reset");
        clearButton = new Button("Clear");

        //add labels, text box, and buttons to grid
        inputBox = new HBox();
        inputBox.setAlignment(Pos.BASELINE_CENTER);
        inputBox.setSpacing(5);
        inputBox.getChildren().add(nodeLabel);
        inputBox.getChildren().add(nodeField);
        inputBox.getChildren().add(insertButton);
        inputBox.getChildren().add(removeButton);
        inputBox.getChildren().add(findButton);
        inputBox.getChildren().add(resetColoursButton);
        inputBox.getChildren().add(clearButton);
        grid.add(inputBox, 0, 0);

        //add area for drawing tree to grid
        treeArea = new Canvas(treeAreaWidth, treeAreaHeight);
        gc = treeArea.getGraphicsContext2D();
        grid.add(treeArea, 0, 1);

        //add area for error message to grid
        errorMessage = new Text();
        errorMessage.setFill(Color.RED);
        errorBox = new HBox(10);
        errorBox.setAlignment(Pos.BOTTOM_CENTER);
        errorBox.getChildren().add(errorMessage);
        grid.add(errorBox, 0, 2);

        //insert button listener
        insertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                if(nodeField.getText() == null || nodeField.getText().isEmpty())
                    errorMessage.setText(noInputMessage);
                else{
                    erase();
                    tree.reset();
                    try{
                        int data = Integer.parseInt(nodeField.getText());
                        tree.insert(data);
                    }catch(NumberFormatException nfe){
                        errorMessage.setText(notIntegerMessage);
                    }catch(DuplicateNodeException dne){
                        errorMessage.setText(duplicateNodeMessage);
                    }
                }
                draw(tree);
                nodeField.clear();
            }
        });

        //remove button listener
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                if(nodeField.getText() == null || nodeField.getText().isEmpty())
                    errorMessage.setText(noInputMessage);
                else{
                    erase();
                    tree.reset();
                    try{
                        int data = Integer.parseInt(nodeField.getText());
                        tree.remove(data);
                    }catch(NumberFormatException nfe){
                        errorMessage.setText(notIntegerMessage);
                    }catch(MissingNodeException mne){
                        errorMessage.setText(missingNodeMessage);
                    }
                }
                draw(tree);
                nodeField.clear();
            }
        });

        //find button listener
        findButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                if(nodeField.getText() == null || nodeField.getText().isEmpty())
                    errorMessage.setText(noInputMessage);
                else{
                    erase();
                    tree.reset();
                    try{
                        int data = Integer.parseInt(nodeField.getText());
                        tree.find(data);
                    }catch(NumberFormatException nfe){
                        errorMessage.setText(notIntegerMessage);
                    }catch(MissingNodeException mne){
                        errorMessage.setText(missingNodeMessage);
                    }
                }
                draw(tree);
                nodeField.clear();
            }
        });

        //reset button listener
        resetColoursButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                erase();
                tree.reset();
                draw(tree);
            }
        });

        //clear button listener
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                erase();
                tree.makeEmpty();
            }
        });
    }

    //erase tree from screen
    private void erase(){
        gc.clearRect(0, 0, treeArea.getWidth(), treeArea.getHeight());
    }

    //draw tree to screen
    private void draw(BinarySearchTree tree){
        if(!tree.isEmpty()) draw(tree.getRoot(), rootXPos, rootYPos, rootChildXDiff);
    }

    //recursively draw tree node by node with pre-order traversal
    private void draw(TreeNode node, double xPos, double yPos, double childXDiff){
        setNodeSizeColour(node);
        drawNodeToScreen(node, xPos, yPos);
        if(node.getLeftChild() != null)
            drawEdgeAndRecurse(node.getLeftChild(), xPos, yPos, -childXDiff);
        if(node.getRightChild() != null)
            drawEdgeAndRecurse(node.getRightChild(), xPos, yPos, childXDiff);
    }

    //check if node is on path to found node and set colour and edge width accordingly
    private void setNodeSizeColour(TreeNode node){
        switch(node.getPathStatus()){
            case OFF:
                gc.setLineWidth(neutralEdgeWidth);
                gc.setFill(neutralNodeColour);
                break;
            case LESS:
                gc.setLineWidth(pathEdgeWidth);
                gc.setFill(lessThanNodeColour);
                break;
            case GREATER:
                gc.setLineWidth(pathEdgeWidth);
                gc.setFill(greaterThanNodeColour);
                break;
            case EQUAL:
                gc.setLineWidth(pathEdgeWidth);
                gc.setFill(equalNodeColour);
                break;
        }
    }

    //draw node on screen at given x and y coordinates
    private void drawNodeToScreen(TreeNode node, double xPos, double yPos){
        gc.fillOval(xPos, yPos, nodeDiameter, nodeDiameter);
        gc.setStroke(lineColour);
        gc.strokeOval(xPos, yPos, nodeDiameter, nodeDiameter);
        gc.setFont(font);
        gc.setFill(textColour);
        double dataWidth = fontMetrics.computeStringWidth(Integer.toString(node.getData()));
        double dataHeight = fontMetrics.getAscent();
        gc.fillText(Integer.toString(node.getData()), xPos + (nodeDiameter-dataWidth)/2, yPos + (5*nodeDiameter)/12 + dataHeight/2);
    }

    //draw edge to child node and call draw on child node
    private void drawEdgeAndRecurse(TreeNode child, double xPos, double yPos, double childXDiff){
        double childX = xPos + childXDiff;
        double childY = yPos + heightDifference;
        if(child.getPathStatus() != OFF)
            gc.setLineWidth(pathEdgeWidth);
        else
            gc.setLineWidth(neutralEdgeWidth);
        gc.strokeLine(xPos + nodeDiameter/2, yPos + nodeDiameter, childX + nodeDiameter/2, childY);
        draw(child, childX, childY, abs(childXDiff/2));
    }

    //return the GridPane for the interface
    public GridPane getGridPane(){
        return grid;
    }
}
