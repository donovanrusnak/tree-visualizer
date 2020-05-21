package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("Binary Search Tree Visualizer");

        BinaryTreeView view = new BinaryTreeView();

        Scene scene = new Scene(view.getGridPane(), 1100, 440);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
