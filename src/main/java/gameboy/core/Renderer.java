package gameboy.core;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import gameboy.post_processing.Effect;
import gameboy.utilities.Camera;
import gameboy.utilities.GlobalSettings;
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

    public static BufferedImage render(Scene scene, int width, int height, double resolution, boolean verbose,
            List<Effect> effects, double distance) {
        int resWidth = (int) (width * resolution);
        int resHeight = (int) (height * resolution);
        PixelData[][] pixelBuffer = new PixelData[resHeight][resWidth];

        for (int y = 0; y < resHeight; y++) {
            for (int x = 0; x < resWidth; x++) {
                double[] screenUV = getNormalizedScreenCoordinates((int) (x / resolution), (int) (y / resolution),
                        width, height);
                pixelBuffer[y][x] = getPixelData(scene, screenUV[0], screenUV[1]);
            }
        }

        for (Effect effect : effects) {
            pixelBuffer = effect.apply(pixelBuffer, resolution);
        }

        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gfx = temp.getGraphics();
        int blockSize = (int) (1 / resolution);

        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                gfx.setColor(pixelBuffer[(int) (y * resolution)][(int) (x * resolution)].getColor().toAWT());
                gfx.fillRect(x, y, blockSize, blockSize);
            }
        }

        return temp;
    }

    public static void renderToImage(Scene scene, int width, int height, List<Effect> effects, double distance)
            throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        System.out.println("Rendering to image...");
        image = render(scene, width, height, 1, true, effects, distance);
        File imgFile = new File("output.png");
        ImageIO.write(image, "PNG", new FileOutputStream(imgFile));
        System.out.println("Image saved.");
        Desktop.getDesktop().open(imgFile);
    }

    public static double[] getNormalizedScreenCoordinates(int x, int y, double width, double height) {
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

    public static PixelData getPixelData(Scene scene, double u, double v) {
        Camera cam = scene.getCamera();
        Vector3 eyePos = new Vector3(0, 0, (-1 / Math.tan(cam.getFOV() / 2)));
        Vector3 rayDir = new Vector3(u, v, 0).subtract(eyePos).rotate(cam.getPitch(), cam.getYaw(), cam.getTilt())
                .normalize();
        Ray ray = new Ray(eyePos.add(cam.getAnchor()), rayDir);
        RayHit hit = ray.cast(scene.getObjects());
        if (hit == null)
            return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
        if (ray.getOrigin().distance(hit.getHitPoint()) > GlobalSettings.MAX_RENDER_DISTANCE)
            return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
        return new PixelData(
                hit.getShape().getMaterial().getShader().shade(hit, scene.getLights(), scene.getObjects(),
                        hit.getShape().getMaterial(), 0),
                ray.getOrigin().distance(hit.getHitPoint()), hit.getShape().getMaterial().getEmission());
    }

    public static RayHit getLookingAt(Scene scene, int width, int height) {
        double[] screenUV = getNormalizedScreenCoordinates(width / 2, height / 2, width, height);
        Camera cam = scene.getCamera();
        Vector3 eyePos = new Vector3(0, 0, (-1 / Math.tan(cam.getFOV() / 2)));
        Vector3 rayDir = new Vector3(screenUV[0], screenUV[1], 0).subtract(eyePos)
                .rotate(cam.getPitch(), cam.getYaw(), cam.getTilt()).normalize();
        Ray ray = new Ray(eyePos.add(cam.getAnchor()), rayDir);
        return ray.cast(scene.getObjects());
    }
}
