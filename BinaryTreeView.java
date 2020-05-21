package sample;

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
import javafx.scene.text.Text;

public class BinaryTreeView {

    //user interface variables
    private TreeNode root = null;
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
    private final Color lessThanNodeColor = Color.color(1, 0, 0, 0.7);
    private final Color greaterThanNodeColor = Color.color(0, 0, 1, 0.7);
    private final Color equalNodeColor = Color.color(0,1, 0,0.7);

    private final double pathLineWidth = 3;
    private final double rootXPos = treeAreaWidth/2 - 5;
    private final double rootYPos = 20;
    private final double rootChildXDiff = rootXPos / 2;

    public BinaryTreeView() {

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
                //clear error message, print error message if there is no input
                errorMessage.setText(null);
                if(nodeField.getText() == null || nodeField.getText().isEmpty())
                    errorMessage.setText("Oops! You need to enter a node to insert!");
                else{
                    try{
                        //erase and redraw tree to reset all node colours
                        if(root != null){
                            root.eraseSubtree(gc);
                            root = insertSubtree(root, rootXPos, rootYPos, rootChildXDiff, null);
                        }
                        //insert node into tree
                        int data = Integer.parseInt(nodeField.getText());
                        root = insert(root, rootXPos,  rootYPos, rootChildXDiff, root, data);
                    }catch(NumberFormatException nfe){
                        //print error message if input is not an integer
                        errorMessage.setText("Oops! A node needs to be an integer!");
                    }
                }
                nodeField.clear();
            }
        });

        //remove button listener
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //clear error message, print error message if there is no input
                errorMessage.setText(null);
                if(nodeField.getText() == null || nodeField.getText().isEmpty())
                    errorMessage.setText("Oops! You need to enter a node to remove!");
                else{
                    try{
                        //erase and redraw tree to reset all node colours
                        if(root != null){
                            root.eraseSubtree(gc);
                            root = insertSubtree(root, rootXPos, rootYPos, rootChildXDiff, null);
                        }
                        //remove node from tree
                        int data = Integer.parseInt(nodeField.getText());
                        root = remove(root, data);
                    }catch(NumberFormatException nfe){
                        //print error message if input is not an integer
                        errorMessage.setText("Oops! A node needs to be an integer!");
                    }
                }
                nodeField.clear();
            }
        });

        //find button listener
        findButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //clear error message, print error message if there is no input
                errorMessage.setText(null);
                if(nodeField.getText() == null || nodeField.getText().isEmpty())
                    errorMessage.setText("Oops! You need to enter a node to find!");
                else{
                    try{
                        //erase and redraw tree to reset all node colours and line widths
                        if(root != null){
                            root.eraseSubtree(gc);
                            root = insertSubtree(root, rootXPos, rootYPos, rootChildXDiff, null);
                        }
                        //find node in tree
                        int data = Integer.parseInt(nodeField.getText());
                        root = find(root, data);
                    }catch(NumberFormatException nfe){
                        //print error message if input is not an integer
                        errorMessage.setText("Oops! A node needs to be an integer!");
                    }
                }
                nodeField.clear();
            }
        });

        //reset button listener
        resetColoursButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                //if tree is not empty erase and redraw tree to reset all node colours and line widths
                if(root != null) {
                    root.eraseSubtree(gc);
                    root = insertSubtree(root, rootXPos, rootYPos, rootChildXDiff, null);
                }
            }
        });

        //clear button listener
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                //erase tree from the interface and set root node to null
                gc.clearRect(0, 0, treeArea.getWidth(), treeArea.getHeight());
                root = null;
            }
        });

        //grid.setGridLinesVisible(true);
    }

    //insert and draw a node in the tree
    //recursively searches tree until proper position found
    //returns new inserted node if in correct position or node at current position if not
    private TreeNode insert(TreeNode node, double xPos, double yPos, double childXDiff, TreeNode parent, int data){
        if(node == null) {
            node = new TreeNode(data, xPos, yPos, childXDiff, parent);
            node.draw(gc);
        }else{
            if(data < node.getData()){
                node.setLeftChild(insert(node.getLeftChild(), xPos - childXDiff,  yPos + 60,childXDiff / 2, node, data));
            }else if(data > node.getData()){
                node.setRightChild(insert(node.getRightChild(), xPos + childXDiff,yPos + 60,childXDiff / 2, node, data));
            }else{
                errorMessage.setText("Oops! That node is already in the tree!");
            }
        }

        return node;
    }

    //remove and erase node from tree
    //recursively searches tree until node to remove found or null node reached
    //returns new child node if correct node or current node if not
    private TreeNode remove(TreeNode node, int data){
        if(node == null)
            errorMessage.setText("Oops! That node is not in the tree!");
        else{
            if(data < node.getData()) node.setLeftChild(remove(node.getLeftChild(), data));
            else if(data > node.getData()) node.setRightChild(remove(node.getRightChild(), data));
            else{
                if(node.getLeftChild() == null && node.getRightChild() == null){
                    node.erase(gc);
                    node = null;
                }
                else if(node.getLeftChild() == null && node.getRightChild() != null){
                    node.eraseSubtree(gc);
                    node = insertSubtree(node.getRightChild(), node.getXPos(), node.getYPos(),
                            node.getChildXDiff(), node.getParent());
                }
                else if(node.getLeftChild() != null && node.getRightChild() == null){
                    node.eraseSubtree(gc);
                    node = insertSubtree(node.getLeftChild(), node.getXPos(), node.getYPos(),
                            node.getChildXDiff(), node.getParent());
                }
                else{
                    node.setData(getPredecessor(node.getLeftChild()));
                    node.draw(gc);
                    node.setLeftChild(remove(node.getLeftChild(), node.getData()));
                }
            }
        }

        return node;
    }

    //insert and draw a node and all its children in the tree
    //recursively traverses subtree creating new nodes with given specifications
    //allows coordinates (positions) of nodes to be correctly adjusted
    //returns the inserted node
    private TreeNode insertSubtree(TreeNode node, double xPos, double yPos, double childXDiff, TreeNode parent) {
        if(node == null) return null;
        TreeNode replaceNode = new TreeNode(node.getData(), xPos, yPos, childXDiff, parent);
        replaceNode.draw(gc);
        replaceNode.setLeftChild(insertSubtree(node.getLeftChild(), xPos - childXDiff, yPos + 60, childXDiff/2, replaceNode));
        replaceNode.setRightChild(insertSubtree(node.getRightChild(), xPos + childXDiff, yPos + 60, childXDiff/2, replaceNode));
        return replaceNode;
    }

    //return the data in the predecessor of a given node
    private int getPredecessor(TreeNode node) {
        if(node.getRightChild() == null) return node.getData();
        else return getPredecessor(node.getRightChild());
    }

    //find and draw path to node in the tree
    //recursively follows path until node found or null node reached
    //colours in nodes and bolds lines on path
    //returns the given node
    private TreeNode find(TreeNode node, int data){
        if(node == null){
            errorMessage.setText("Oops! That node is not in the tree!");
        }else{
            if(data < node.getData()){
                node.setLeftChild(find(node.getLeftChild(), data));
                node.setLineWidth(pathLineWidth);
                node.setNodeColor(greaterThanNodeColor);
                node.draw(gc);
            }else if(data > node.getData()){
                node.setRightChild(find(node.getRightChild(), data));
                node.setLineWidth(pathLineWidth);
                node.setNodeColor(lessThanNodeColor);
                node.draw(gc);
            }else{
                node.setNodeColor(equalNodeColor);
                node.setLineWidth(pathLineWidth);
                node.draw(gc);
            }
        }

        return node;
    }

    //return the gridpane for the interface
    public GridPane getGridPane(){
        return grid;
    }
}
