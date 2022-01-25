package ottr;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.stage.Stage;

public class Main extends Application {

    private Desktop desktop = Desktop.getDesktop();


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../sample.fxml"));
        primaryStage.setTitle("OTTR Visualization");
        primaryStage.setScene(new Scene(root, 600, 256));
        primaryStage.setResizable(false);
        primaryStage.show();

        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Open a Picture...");

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            openFile(file);
                        }
                    }
                });


    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }




//    public static void main(String[] args) {
//
//        System.setProperty("org.graphstream.ui", "swing");
//        launch(args);
//
//        Graph graph = new SingleGraph("Tutorial 1");
//        graph.addNode("A" );
//        graph.addNode("B" );
//        graph.addNode("C" );
//        graph.addEdge("AB", "A", "B");
//        graph.addEdge("BC", "B", "C");
//        graph.addEdge("CA", "C", "A");
//        graph.display();
//
//    }
}
