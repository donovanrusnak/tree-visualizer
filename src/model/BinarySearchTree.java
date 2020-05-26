package model;

import exceptions.DuplicateNodeException;
import exceptions.MissingNodeException;

public class BinarySearchTree {

    private TreeNode root;

    public BinarySearchTree(){
        this.root = null;
    }

    //insert node with given data
    //throw exception if node already in tree
    public void insert(int data) throws DuplicateNodeException{
        if(isEmpty())
            root = new TreeNode(data);
        else
            root.insert(data);
    }

    //remove node with given data
    //throw exception if node not in tree
    public void remove(int data) throws MissingNodeException{
        if(!isEmpty())
            root = root.remove(data);
        else
            throw new MissingNodeException();
    }

    //find node with given data
    //flag each node along path as on path
    //throw exception if node not in tree
    public void find(int data) throws MissingNodeException{
        if(!isEmpty())
            root.find(data);
        else
            throw new MissingNodeException();
    }

    //flag all nodes in tree as off path
    public void reset(){
        if(!isEmpty()) root.reset();
    }

    public void makeEmpty(){
        root = null;
    }

    public boolean isEmpty(){
        return root == null;
    }

    public TreeNode getRoot(){
        return root;
    }
}
