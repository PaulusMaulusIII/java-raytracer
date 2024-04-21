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
    int width = 1280;
    int height = 720;
    WritableImage frame = new WritableImage((width / 4) * 3, height);
    ImageView imageView = new ImageView(frame);

    @Override
    public void start(Stage stage) throws InterruptedException {
        HBox mainPane = new HBox();
        ImageView viewPort = imageView;

        mainPane.getChildren().add(generateSceneCreator());
        mainPane.getChildren().add(viewPort);

        Scene scene = new Scene(mainPane);

        stage.setTitle("Java RayTracer");
        stage.setScene(scene);
        stage.heightProperty().addListener((evt) -> resize(stage));
        stage.widthProperty().addListener((evt) -> resize(stage));
        stage.show();
    }

    public void resize(Stage stage) {
        width = (int) stage.getWidth();
        height = (int) stage.getHeight();
    }

    public VBox generateSceneCreator() {
        VBox inputFieldBox = new VBox();

        TextArea inputField = new TextArea("Camera {\r\n" + //
                "\tposition : {0, 5, -10}\r\n" + //
                "\tfov : 1\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Cube {\r\n" + //
                "\tposition : {2.5,2,0}\r\n" + //
                "\tsidelength : 4\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Sphere {\r\n" + //
                "\tposition : {-2.5,2,0}\r\n" + //
                "\tradius : 2\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Plane {\r\n" + //
                "\tposition: {0,0,0}\r\n" + //
                "\taxis : y\r\n" + //
                "\tcolor: {255,255,255}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Plane {\r\n" + //
                "\tposition: {0,10,0}\r\n" + //
                "\taxis : y\r\n" + //
                "\tcolor: {255,255,255}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Plane {\r\n" + //
                "\tposition: {0,0,10}\r\n" + //
                "\taxis : z\r\n" + //
                "\tcolor: {255,255,255}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Plane {\r\n" + //
                "\tposition : {10,0,0}\r\n" + //
                " \taxis : x\r\n" + //
                "\tcolor: {255,255,255}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Plane {\r\n" + //
                "\tposition : {-10,0,0}\r\n" + //
                " \taxis : x\r\n" + //
                "\tcolor: {255,255,255}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Light {\r\n" + //
                "\tposition: {0,5,-5}\r\n" + //
                "}");
        inputField.setPrefHeight((height / 3) * 2);
        inputField.setPrefWidth(width / 4);

        TextArea logField = new TextArea();
        logField.setPrefHeight((height / 3) - height / 10);
        logField.setPrefWidth(width / 4);
        logField.setEditable(false);

        Button button = new Button("Run");
        button.setPrefHeight(height / 10);
        button.setPrefWidth(width / 4);
        button.setOnAction((evt) -> {
            inputField.setPrefHeight((height / 3) * 2);
            inputField.setPrefWidth(width / 4);
            logField.setPrefHeight((height / 3) - height / 10);
            logField.setPrefWidth(width / 4);
            button.setPrefHeight(height / 10);
            button.setPrefWidth(width / 4);
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;

                System.setOut(ps);

                frame = new Renderer(new Interpreter().interpret(inputField.getText()), (width / 4) * 3, height)
                        .render();
                imageView.setImage(frame);

                System.out.flush();
                System.setOut(old);
                logField.setText(baos.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        button.fire();

        inputFieldBox.getChildren().addAll(inputField, logField, button);

        return inputFieldBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}