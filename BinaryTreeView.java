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

    private TreeNode root = null;
    private GridPane grid;
    private Label nodeLabel;
    private Button insertButton;
    private Button removeButton;
    private Button findButton;
    private Button resetTreeGraphicsButton;
    private Button clearButton;
    private HBox inputBox;
    private Canvas treeArea;
    private HBox errorBox;

    private final TextField nodeField;
    private final int treeAreaWidth = 1080;
    private final int treeAreaHeight = 360;
    private final GraphicsContext gc;

    private final Text errorMessage;
    private final Color lessThanNodeColor = Color.color(1, 0, 0, 0.7);
    private final Color greaterThanNodeColor = Color.color(0, 0, 1, 0.7);
    private final Color equalNodeColor = Color.color(0,1, 0,0.7);
    private final double pathLineWidth = 3;

    private final double rootXPos = treeAreaWidth/2 - 5;
    private final double rootYPos = 20;
    private final double rootChildXDiff = rootXPos / 2;

    public BinaryTreeView() {
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        nodeLabel = new Label("Node:");
        nodeField = new TextField();
        insertButton = new Button("Insert");
        removeButton = new Button("Remove");
        findButton = new Button("Find");
        resetTreeGraphicsButton = new Button("Reset");
        clearButton = new Button("Clear");

        inputBox = new HBox();
        inputBox.setAlignment(Pos.BASELINE_CENTER);
        inputBox.setSpacing(5);
        inputBox.getChildren().add(nodeLabel);
        inputBox.getChildren().add(nodeField);
        inputBox.getChildren().add(insertButton);
        inputBox.getChildren().add(removeButton);
        inputBox.getChildren().add(findButton);
        inputBox.getChildren().add(resetTreeGraphicsButton);
        inputBox.getChildren().add(clearButton);
        grid.add(inputBox, 0, 0);

        treeArea = new Canvas(treeAreaWidth, treeAreaHeight);
        gc = treeArea.getGraphicsContext2D();
        grid.add(treeArea, 0, 1);

        errorMessage = new Text();
        errorMessage.setFill(Color.RED);
        errorBox = new HBox(10);
        errorBox.setAlignment(Pos.BOTTOM_CENTER);
        errorBox.getChildren().add(errorMessage);
        grid.add(errorBox, 0, 2);

        insertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                if (nodeField.getText() == null || nodeField.getText().isEmpty())
                    errorMessage.setText("Oops! You need to enter a node to insert!");
                else {
                    try{
                        if(root != null) {
                            root.eraseSubtree(gc);
                            root = insertSubtree(root, rootXPos, rootYPos, rootChildXDiff, null);
                        }
                        double data = Double.parseDouble(nodeField.getText());
                        root = insert(root, rootXPos,  rootYPos,rootChildXDiff, root, data);
                    }catch(NumberFormatException nfe){
                        errorMessage.setText("Oops! A node needs to be a number!");
                    }
                }
                nodeField.clear();
            }
        });

        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                if (nodeField.getText() == null || nodeField.getText().isEmpty())
                    errorMessage.setText("Oops! You need to enter a node to remove!");
                else{
                    try{
                        if(root != null) {
                            root.eraseSubtree(gc);
                            root = insertSubtree(root, rootXPos, rootYPos, rootChildXDiff, null);
                        }
                        double data = Double.parseDouble(nodeField.getText());
                        root = remove(root, data);
                    }catch(NumberFormatException nfe){
                        errorMessage.setText("Oops! A node needs to be a number!");
                    }
                }
                nodeField.clear();
            }
        });

        findButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                if(nodeField.getText() == null || nodeField.getText().isEmpty())
                    errorMessage.setText("Oops! You need to enter a node to find!");
                else{
                    try{
                        if(root != null) {
                            root.eraseSubtree(gc);
                            root = insertSubtree(root, rootXPos, rootYPos, rootChildXDiff, null);
                        }
                        double data = Double.parseDouble(nodeField.getText());
                        root = find(root, data);
                    }catch(NumberFormatException nfe){
                        errorMessage.setText("Oops! A node needs to be a number!");
                    }
                }
                nodeField.clear();
            }
        });

        resetTreeGraphicsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                root.eraseSubtree(gc);
                root = insertSubtree(root, rootXPos, rootYPos, rootChildXDiff, null);
            }
        });

        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorMessage.setText(null);
                gc.clearRect(0, 0, treeArea.getWidth(), treeArea.getHeight());
                root = null;
            }
        });

        //grid.setGridLinesVisible(true);
    }

    private TreeNode insert(TreeNode node, double xPos, double yPos, double childXDiff, TreeNode parent, double data){
        if (node == null) {
            node = new TreeNode(data, xPos, yPos, childXDiff, parent);
            node.draw(gc);
        } else {
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

    private TreeNode remove(TreeNode node, double data){
        if(node == null)
            errorMessage.setText("That node is not in the tree!");
        else{
            if(data < node.getData()) node.setLeftChild(remove(node.getLeftChild(), data));
            else if (data > node.getData()) node.setRightChild(remove(node.getRightChild(), data));
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

    private TreeNode insertSubtree(TreeNode node, double xPos, double yPos, double childXDiff, TreeNode parent) {
        if(node == null) return null;
        TreeNode replaceNode = new TreeNode(node.getData(), xPos, yPos, childXDiff, parent);
        replaceNode.draw(gc);
        replaceNode.setLeftChild(insertSubtree(node.getLeftChild(), xPos - childXDiff, yPos + 60, childXDiff/2, replaceNode));
        replaceNode.setRightChild(insertSubtree(node.getRightChild(), xPos + childXDiff, yPos + 60, childXDiff/2, replaceNode));
        return replaceNode;
    }

    private double getPredecessor(TreeNode node) {
        if(node.getRightChild() == null) return node.getData();
        else return getPredecessor(node.getRightChild());
    }

    private TreeNode find(TreeNode node, double data){
        if(node == null){
            errorMessage.setText("That node is not in the tree!");
        }else{
            if(data < node.getData()) {
                node.setLeftChild(find(node.getLeftChild(), data));
                node.setLineWidth(pathLineWidth);
                node.setNodeColor(greaterThanNodeColor);
                node.draw(gc);
            }else if(data > node.getData()) {
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

    public GridPane getGridPane(){
        return grid;
    }
}
