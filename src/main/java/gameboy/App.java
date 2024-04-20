package gameboy;

import gameboy.utilities.math.Vector3;
import gameboy.core.Interpreter;
import gameboy.core.Renderer;

import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class App extends Application {

    Vector3 deltaCamera;
    int width = 512;
    int height = 512;
    double resolution = 1;
    WritableImage frame = new WritableImage(width, height);
    ImageView imageView = new ImageView(frame);

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        HBox mainPane = new HBox();
        ImageView viewPort = imageView;

        mainPane.getChildren().add(generateSceneCreator());
        mainPane.getChildren().add(viewPort);

        Scene scene = new Scene(mainPane);

        primaryStage.setTitle("Java RayTracer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public VBox generateSceneCreator() {
        VBox inputFieldBox = new VBox();

        TextArea inputField = new TextArea("Camera {\r\n" + //
                "\tposition : {45,5,-3}\r\n" + //
                "\tfov : 10°\r\n" + //
                "\tpitch : 3.5°\r\n" + //
                "\tyaw: -69°\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Light {\r\n" + //
                "\tposition : {45, .5, -5}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Sphere {\r\n" + //
                "\tposition : {0,1.5,0}\r\n" + //
                "\tradius: 1\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Cube {\r\n" + //
                "\tposition : {0,1,6}\r\n" + //
                "\tsidelength: 2 \r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Plane {\r\n" + //
                "\tposition : {0,0,0}\r\n" + //
                "\taxis : y\r\n" + //
                "\tcolor : {255,255,255}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Plane {\r\n" + //
                "\tposition : {-10,0,0}\r\n" + //
                "\taxis: x\r\n" + //
                "\tcolor : {255,255,255}\r\n" + //
                "}");
        inputField.setPrefHeight((height / 3) * 2);

        TextArea logField = new TextArea();
        logField.setPrefHeight(height / 3);
        logField.setEditable(false);

        Button button = new Button("Run");
        button.setOnAction((evt) -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;

                System.setOut(ps);

                frame = new Renderer(new Interpreter().interpret(inputField.getText()), width, height).render();
                imageView.setImage(frame);

                System.out.flush();
                System.setOut(old);
                logField.setText(baos.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        inputFieldBox.getChildren().addAll(inputField, logField, button);

        return inputFieldBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}