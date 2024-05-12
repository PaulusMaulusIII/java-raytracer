package gameboy;

import javax.swing.*;

import gameboy.core.Viewport;

public class App {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        Viewport viewport = new Viewport(1280, 720,
                "Camera {\n" + "position:{0,0,-10}\n" + "}\n" + "Cube {\n" + "position:{0,0,0}\n" + "sidelength:2.5\n"
                        + "}\n" + "Light {\n" + "position:{0,5,-5}\n" + "}\n" + "Plane {\n" + "position:{0,-5,0}\n"
                        + "axis:y\n" + "color:{255,255,255}\n" + "}");
        frame.add(viewport);
        frame.setVisible(true);
        viewport.run();
    }

}