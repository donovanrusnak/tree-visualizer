package model;

import exceptions.DuplicateNodeException;
import exceptions.MissingNodeException;

import static model.TreeNode.PathStatus.*;

public class TreeNode {

    //node variables
    private int data;
    private TreeNode leftChild;
    private TreeNode rightChild;
    private PathStatus pathStatus;

    //flags for whether node is on path to found node
    public enum PathStatus{
        OFF,
        LESS,
        GREATER,
        EQUAL
    }

    public TreeNode(int data){
        this.data = data;
        this.leftChild = null;
        this.rightChild = null;
        this.pathStatus = OFF;
    }

    //insert a node with given data
    //recursively searches tree until proper position found
    //throws exception if node already in tree
    public void insert(int data) throws DuplicateNodeException {
        if(data < this.data){
            if(leftChild == null) leftChild = new TreeNode(data);
            else leftChild.insert(data);
        }else if(data > this.data){
            if(rightChild == null) rightChild = new TreeNode(data);
            else rightChild.insert(data);
        }else{
            throw new DuplicateNodeException();
        }
    }

    //remove node with given data
    //recursively searches tree until node to remove found
    //throws exception if node not in tree
    public TreeNode remove(int data) throws MissingNodeException{
        if(data < this.data){
            if(leftChild == null) throw new MissingNodeException();
            leftChild = leftChild.remove(data);
        }
        else if(data > this.data){
            if(rightChild == null) throw new MissingNodeException();
            rightChild = rightChild.remove(data);
        }
        else{
            if(leftChild == null && rightChild == null){
                return null;
            }
            else if(leftChild == null){
                return rightChild;
            }
            else if(rightChild == null){
                return leftChild;
            }
            else{
                this.data = leftChild.getPredecessorData();
                leftChild = leftChild.remove(this.data);
            }
        }
        return this;
    }

    //return the data of the predecessor node
    //recursively searches tree for predecessor
    private int getPredecessorData() {
        if(rightChild == null) return data;
        else return rightChild.getPredecessorData();
    }

    //find node with given data
    //recursively searches tree until node found
    //flags nodes along path as less than, greater than, or equal to node to find
    public void find(int data) throws MissingNodeException{
        if(data < this.data){
            if(leftChild == null) throw new MissingNodeException();
            leftChild.find(data);
            pathStatus = LESS;
        }else if(data > this.data){
            if(rightChild == null) throw new MissingNodeException();
            rightChild.find(data);
            pathStatus = GREATER;
        }else{
            pathStatus = EQUAL;
        }
    }

    //flag all nodes in tree as off path
    //recursive pre-order traversal of tree
    public void reset() {
        pathStatus = OFF;
        if(leftChild != null) leftChild.reset();
        if(rightChild != null) rightChild.reset();
    }

    public int getData(){
        return data;
    }

    public TreeNode getLeftChild(){
        return leftChild;
    }

    public TreeNode getRightChild(){
        return rightChild;
    }

    public PathStatus getPathStatus() {
        return pathStatus;
    }
}
