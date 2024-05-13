package gameboy;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import gameboy.core.Viewport;

public class App extends JFrame {

    public App() {
        super();
        setSize(1280, 720);
        setTitle("Java Ray-Tracer");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Viewport viewport = new Viewport("Camera {\r\n" + //
                "\tposition : {0, 10, -10}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Light {\r\n" + //
                "\tposition : {5, 5, -5}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Cube {\r\n" + //
                "\tposition : {0, 0, 10}\r\n" + //
                "\tmaterial : checker\r\n" + //
                "\tgridsize : 2\r\n" + //
                "\tsidelength : 4\r\n" + //
                "\tcolor : {255,150,255}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Plane {\r\n" + //
                "\tposition : {0, -2.5, 0}\r\n" + //
                "\tmaterial : checker\r\n" + //
                "\taxis : y\r\n" + //
                "\tcolor : {255,255,255}\r\n" + //
                "\tcolor2 : {25,25,25}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Sphere {\r\n" + //
                "\tposition : {0, 0, 0}\r\n" + //
                "\tmaterial : checker\r\n" + //
                "\tcolor : {255, 255, 255}\r\n" + //
                "\tgridsize : 2\r\n" + //
                "\tradius : 2.5\r\n" + //
                "\trefl : .9\r\n" + //
                "}", this);
        add(viewport);
        setVisible(true);
        viewport.run();
    }

    public static void main(String[] args) {
        new App();
    }
}