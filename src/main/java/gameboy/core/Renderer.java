package gameboy.core;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import gameboy.utilities.Camera;
import gameboy.utilities.Color;
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
            boolean dof, double distance) {
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

        if (dof) {
            PixelData[][] blurBuffer = new PixelData[resHeight][resWidth];
            int blurRadius = 0;

            for (int y = 0; y < resHeight; y++) {
                for (int x = 0; x < resWidth; x++) {
                    int r = 0, g = 0, b = 0, count = 0;
                    blurRadius = (int) (Math.abs(((distance - pixelBuffer[y][x].getDistance()))) * resolution);
                    if (blurRadius > 16)
                        blurRadius = 16;
                    if (blurRadius >= 1) {
                        for (int dy = -blurRadius; dy <= blurRadius; dy++) {
                            for (int dx = -blurRadius; dx <= blurRadius; dx++) {
                                try {
                                    PixelData p = pixelBuffer[y + dy][x + dx];
                                    r += p.getColor().getRed();
                                    g += p.getColor().getGreen();
                                    b += p.getColor().getBlue();
                                    count++;
                                } catch (Exception e) {
                                }
                            }
                        }
                        r /= count;
                        g /= count;
                        b /= count;
                        blurBuffer[y][x] = new PixelData(new Color(r, g, b), pixelBuffer[y][x].getDistance(),
                                pixelBuffer[y][x].getEmission());
                    }
                }
            }

            for (int y = 0; y < resHeight; y++)
                for (int x = 0; x < resWidth; x++)
                    if (blurBuffer[y][x] != null)
                        pixelBuffer[y][x] = blurBuffer[y][x];
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

    public static void renderToImage(Scene scene, int width, int height, boolean dof, double distance)
            throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        System.out.println("Rendering to image...");
        image = render(scene, width, height, 1, true, dof, distance);
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
        Vector3 rayDir = new Vector3(u, v, 0).subtract(eyePos).rotate(cam.getPitch(), cam.getYaw()).normalize();
        Ray ray = new Ray(eyePos.add(cam.getAnchor()), rayDir);
        RayHit hit = ray.cast(scene.getObjects());
        if (hit == null)
            return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
        if (ray.getOrigin().distance(hit.getHitPoint()) > GlobalSettings.MAX_RENDER_DISTANCE)
            return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
        return new PixelData(
                hit.getShape().getMaterial().getShader().shade(hit, scene.getLights(), scene.getObjects(),
                        hit.getShape().getMaterial()),
                ray.getOrigin().distance(hit.getHitPoint()), hit.getShape().getMaterial().getEmission());
    }

    public static RayHit getLookingAt(Scene scene, int width, int height) {
        double[] screenUV = getNormalizedScreenCoordinates(width / 2, height / 2, width, height);
        Camera cam = scene.getCamera();
        Vector3 eyePos = new Vector3(0, 0, (-1 / Math.tan(cam.getFOV() / 2)));
        Vector3 rayDir = new Vector3(screenUV[0], screenUV[1], 0).subtract(eyePos).rotate(cam.getPitch(), cam.getYaw())
                .normalize();
        Ray ray = new Ray(eyePos.add(cam.getAnchor()), rayDir);
        return ray.cast(scene.getObjects());
    }
}
