package gameboy.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.IOException;

import gameboy.utilities.Camera3D;
import gameboy.utilities.Scene;
import gameboy.utilities.data.PixelData;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public class Renderer {

    Scene scene;
    int height;
    int width;

    public Renderer(Scene scene, int width, int height) {
        this.scene = scene;
        this.height = height;
        this.width = width;
    }

    public BufferedImage render(double resolution) {
        long time = System.currentTimeMillis();
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gfx = temp.getGraphics();
        int blockSize = (int) (1 / resolution);

        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                double[] screenUV = getNormalizedScreenCoordinates(x, y, width, height);
                PixelData pixelData = getPixelData(screenUV[0], screenUV[1]);
                gfx.setColor(new Color(25, 25, 25));
                if (pixelData != null) {
                    gfx.setColor(pixelData.getColor());
                }
                gfx.fillRect(x, y, blockSize, blockSize);
            }
        }

        long deltaTime = System.currentTimeMillis() - time;

        System.out.println("Rendered Scene:");
        System.out.println("At: " + width + "px x " + height + "px and " + resolution * 100 + "% Resolution");
        System.out.println("Camera at: " + scene.getCurrentCamera().getPosition().toString());
        System.out.println("With " + Math.toDegrees(scene.getCurrentCamera().getPitch()) + "° of Pitch");
        System.out.println("And " + Math.toDegrees(scene.getCurrentCamera().getYaw()) + "° of Yaw");
        System.out.println("With a FOV of: " + Math.toDegrees(scene.getCurrentCamera().getFOV()) + "°");
        System.out.println("Took " + deltaTime + "ms");

        return temp;
    }

    public void renderToImage(Scene scene3d, int i, int j) throws IOException {

    }

    public double[] getNormalizedScreenCoordinates(int x, int y, double width, double height) {
        double u = 0, v = 0;
        if (width > height) {
            u = (double) (x - width / 2 + height / 2) / height * 2 - 1;
            v = -((double) y / height * 2 - 1);
        }
        else {
            u = (double) x / width * 2 - 1;
            v = -((double) (y - height / 2 + width / 2) / width * 2 - 1);
        }

        return new double[] {
                u, v
        };
    }

    public PixelData getPixelData(double u, double v) {
        Camera3D cam = scene.getCurrentCamera();
        Vector3 eyePos = new Vector3(0, 0, (-1 / Math.tan(cam.getFOV() / 2)));
        Vector3 rayDir = new Vector3(u, v, 0).subtract(eyePos).rotate(cam.getPitch(), cam.getYaw()).normalize();
        Ray ray = new Ray(eyePos.add(cam.getPosition()), rayDir);

        RayHit hit = scene.castRay(ray, scene.getChildren());
        if (hit == null)
            return null;

        return new PixelData(hit, scene.getLights(), scene.getChildren(), scene.getOptions());
    }
}
