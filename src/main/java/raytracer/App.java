package raytracer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import raytracer.core.Viewport;
import raytracer.core.SettingPanel;

public class App {

    public App() {

        JFrame main = new JFrame();
        main.setSize(1280, 720);
        main.setTitle("Java Ray-Tracer");
        main.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        JDialog settingsDialog = new JDialog(main, "Settings");
        SettingPanel settings = new SettingPanel(main);
        Viewport viewport = new Viewport(main, settings, settingsDialog);

        settingsDialog.add(settings);
        settingsDialog.setSize(settings.getSize());
        settingsDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        main.setVisible(true);
        main.add(viewport);
        viewport.run();
    }

    public static void main(String[] args) {
        new App();
    }
}