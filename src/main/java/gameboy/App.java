package gameboy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

import gameboy.math.Vector3;
import gameboy.math.shapes.*;
import gameboy.rendering.Camera3D;
import gameboy.rendering.Light;
import gameboy.rendering.Renderer;
import gameboy.rendering.Scene3D;
import gameboy.rendering.Shape3D;

public class App extends Application {

    private Canvas canvas;
    private Scene3D scene;
    private Camera3D camera;
    private double resolution = .5;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("3D Projection Mapping");
        canvas = new Canvas(512, 512);

        VBox root = new VBox();
        root.getChildren().addAll(canvas);

        camera = new Camera3D(new Vector3(0, 0, -5), Math.toRadians(30));
        camera.setPitch(Math.toRadians(0));
        camera.setYaw(Math.toRadians(0));
        scene = new Scene3D(canvas, camera, new LinkedList<Shape3D>(List.of(new Cube(new Vector3(0, 0, 0), 6))));
        scene.getLights().add(new Light(scene.getCurrentCamera().getPosition()));

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed((KeyEvent event) -> {
            double moveSpeed = 1;
            switch (event.getCode()) {
            case W:
                camera.translate(new Vector3(0, 0, moveSpeed).rotate(camera.getPitch(), camera.getYaw()));
                break;
            case A:
                camera.translate(new Vector3(-moveSpeed, 0, 0).rotate(camera.getPitch(), camera.getYaw()));
                break;
            case S:
                camera.translate(new Vector3(0, 0, -moveSpeed).rotate(camera.getPitch(), camera.getYaw()));
                break;
            case D:
                camera.translate(new Vector3(moveSpeed, 0, 0).rotate(camera.getPitch(), camera.getYaw()));
                break;
            case SPACE:
                camera.translate(new Vector3(0, moveSpeed, 0).rotate(camera.getPitch(), camera.getYaw()));
                break;
            case SHIFT:
                camera.translate(new Vector3(0, -moveSpeed, 0).rotate(camera.getPitch(), camera.getYaw()));
                break;
            case LEFT:
                camera.setYaw(camera.getYaw() - moveSpeed / (moveSpeed * 100));
                break;
            case F:
                camera.setYaw(camera.getYaw() - moveSpeed / (moveSpeed * 10));
                break;
            case RIGHT:
                camera.setYaw(camera.getYaw() + moveSpeed / (moveSpeed * 100));
                break;
            case H:
                camera.setYaw(camera.getYaw() + moveSpeed / (moveSpeed * 10));
                break;
            case UP:
                camera.setPitch(camera.getPitch() - moveSpeed / 100);
                break;
            case T:
                camera.setPitch(camera.getPitch() - moveSpeed / 10);
                break;
            case DOWN:
                camera.setPitch(camera.getPitch() + moveSpeed / 100);
                break;
            case G:
                camera.setPitch(camera.getPitch() + moveSpeed / 10);
                break;
            case Q:
                camera.setFOV(camera.getFOV() - Math.toRadians(1));
                break;
            case E:
                camera.setFOV(camera.getFOV() + Math.toRadians(1));
                break;
            case R:
                resolution /= 1.1;
                break;
            case Z:
                resolution *= 1.1;
                if (resolution > 1)
                    resolution = 1;
                break;
            case ESCAPE:
                System.exit(1);
                break;

            default:

                break;
            }
            scene.getLights().get(0).setAnchor(camera.getPosition());
            redrawCanvas(resolution);
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        redrawCanvas(resolution);
    }

    private void redrawCanvas(double resolution) {
        new Renderer().render(scene, canvas.getGraphicsContext2D(), ((int) canvas.getWidth()),
                ((int) canvas.getHeight()), resolution);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
