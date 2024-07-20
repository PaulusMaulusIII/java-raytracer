package raytracer.core;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import raytracer.post_processing.Effect;
import raytracer.utilities.Camera;
import raytracer.utilities.GlobalSettings;
import raytracer.utilities.Scene;
import raytracer.utilities.data.PixelData;
import raytracer.utilities.math.Ray;
import raytracer.utilities.math.RayHit;
import raytracer.utilities.math.Vector3;

public class Renderer {

    public static BufferedImage render(Scene scene, int width, int height, double resolution, boolean verbose,
            List<Effect> effects, double distance) {
        int resWidth = (int) (width * resolution);
        int resHeight = (int) (height * resolution);
        PixelData[][] pixelBuffer = new PixelData[resHeight][resWidth];

        if (verbose)
            System.out.println("Casting rays");

        for (int y = 0; y < resHeight; y++) {
            for (int x = 0; x < resWidth; x++) {
                double[] screenUV = getNormalizedScreenCoordinates(x / resolution, y / resolution, width, height);
                pixelBuffer[y][x] = getPixelData(scene, screenUV[0], screenUV[1]);
            }
            if (verbose && y != 0 && y % (resHeight / 100) == 0)
                System.out.print("#");
        }

        if (verbose)
            System.out.println("\nApplying FX");

        for (Effect effect : effects) {
            if (verbose)
                System.out.println("Applying " + effect.getName());
            pixelBuffer = effect.apply(pixelBuffer, resolution);
            if (verbose)
                System.out.println("Applied " + effect.getName());
        }

        if (verbose)
            System.out.println("Drawing image");
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gfx = temp.getGraphics();
        int blockSize = (int) (1 / resolution);

        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                gfx.setColor(pixelBuffer[(int) (y * resolution)][(int) (x * resolution)].getColor().toAWT());
                gfx.fillRect(x, y, blockSize, blockSize);
            }
            if (verbose && y != 0 && y % (height / 100) == 0)
                System.out.print("#");
        }
        if (verbose)
            System.out.println();

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

    public static double[] getNormalizedScreenCoordinates(double x, double y, double width, double height) {
        double u, v;
        if (width > height) {
            u = ((x - width / 2 + height / 2) / height) * 2 - 1;
            v = -((y / height) * 2 - 1);
        }
        else {
            u = (x / width) * 2 - 1;
            v = -((y - height / 2 + width / 2) / width) * 2 - 1;
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
        RayHit arrowHit = ray.castArrows(scene.getObjects());
        if (arrowHit != null)
            hit = arrowHit;
        if (hit == null)
            return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
        if (ray.getOrigin().distance(hit.getHitPoint()) > GlobalSettings.MAX_RENDER_DISTANCE)
            return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
        return new PixelData(
                hit.getShape().getMaterial().getShader().shade(hit, scene.getLights(), scene.getObjects(),
                        hit.getShape().getMaterial(), 0),
                ray.getOrigin().distance(hit.getHitPoint()),
                hit.getShape().getMaterial().getEmissionAt(hit.getHitPoint()));
    }

    public static RayHit getLookingAt(Scene scene, int width, int height) {
        return getAt(scene, width / 2, height / 2, width, height);
    }

    public static RayHit getAt(Scene scene, int posX, int posY, double width, double height) {
        double[] screenUV = getNormalizedScreenCoordinates(posX, posY, width, height);
        Camera cam = scene.getCamera();
        Vector3 eyePos = new Vector3(0, 0, (-1 / Math.tan(cam.getFOV() / 2)));
        Vector3 rayDir = new Vector3(screenUV[0], screenUV[1], 0).subtract(eyePos)
                .rotate(cam.getPitch(), cam.getYaw(), cam.getTilt()).normalize();
        Ray ray = new Ray(eyePos.add(cam.getAnchor()), rayDir);
        RayHit hit = ray.cast(scene.getObjects());
        RayHit arrowHit = ray.castArrows(scene.getObjects());
        return (arrowHit == null) ? hit : arrowHit;
    }
}