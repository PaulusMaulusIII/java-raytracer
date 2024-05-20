package gameboy.core;

import java.awt.Desktop;
import gameboy.utilities.GlobalSettings;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameboy.utilities.Camera;
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
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gfx = temp.getGraphics();
        int blockSize = (int) (1 / resolution);

        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                double[] screenUV = getNormalizedScreenCoordinates(x, y, width, height);
                PixelData pixelData = getPixelData(screenUV[0], screenUV[1]);
                gfx.setColor(GlobalSettings.SKY_BOX_COLOR.toAWT());
                if (pixelData != null) {
                    gfx.setColor(pixelData.getColor().toAWT());
                }
                gfx.fillRect(x, y, blockSize, blockSize);
            }
        }

        return temp;
    }

    public void renderToImage(Scene scene, int i, int j) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        System.out.println("Rendering to image...");

        image = new Renderer(scene, i, j).render(1);

        File imgFile = new File("output.png");
        ImageIO.write(image, "PNG", new FileOutputStream(imgFile));
        System.out.println("Image saved.");

        Desktop.getDesktop().open(imgFile);
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
        Camera cam = scene.getCurrentCamera();
        Vector3 eyePos = new Vector3(0, 0, (-1 / Math.tan(cam.getFOV() / 2)));
        Vector3 rayDir = new Vector3(u, v, 0).subtract(eyePos).rotate(cam.getPitch(), cam.getYaw()).normalize();
        Ray ray = new Ray(eyePos.add(cam.getPosition()), rayDir);

        RayHit hit = ray.castRay(scene.getChildren());
        if (hit == null)
            return null;
        if (ray.getOrigin().distance(hit.getHitPoint()) > GlobalSettings.MAX_RENDER_DISTANCE)
            return null;

        return new PixelData(hit, scene.getLights(), scene.getChildren(), scene.getOptions());
    }
}
