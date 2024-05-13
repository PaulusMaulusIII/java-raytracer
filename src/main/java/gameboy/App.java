package gameboy;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import gameboy.core.Viewport;

public class App {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setTitle("Java Ray-Tracer");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Viewport viewport = new Viewport("Camera {\r\n" + //
                "\tposition : {0,0,-5}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "Cube {    \r\n" + //
                "\tposition : {0,0,5}\r\n" + //
                "\tsidelength : 3\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Light {\r\n" + //
                "\tposition : {-2.5,1,0}\r\n" + //
                "\tcolor : {255,0,0}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Light {\r\n" + //
                "\tposition : {0,1,0}\r\n" + //
                "\tcolor : {0,255,0}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Light {\r\n" + //
                "\tposition : {2.5,1,0}\r\n" + //
                "\tcolor : {0,0,255}\r\n" + //
                "}\r\n" + //
                "\r\n" + //
                "Plane {\r\n" + //
                "\tposition : {0,-2.5,0}\r\n" + //
                "\taxis : y\r\n" + //
                "\tcolor : {255,255,255}\r\n" + //
                "}", frame);

        frame.add(viewport);
        frame.setVisible(true);

        viewport.run();
    }

}