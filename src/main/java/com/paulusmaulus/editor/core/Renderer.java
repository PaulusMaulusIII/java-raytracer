package com.paulusmaulus.editor.core;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import com.paulusmaulus.editor.core.interfaces.Effect;
import com.paulusmaulus.editor.utilities.Camera;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Scene;
import com.paulusmaulus.editor.utilities.data.PixelData;
import com.paulusmaulus.editor.utilities.math.Ray;
import com.paulusmaulus.editor.utilities.math.RayHit;
import com.paulusmaulus.editor.utilities.math.Vector3;

public class Renderer {

    public static BufferedImage render(Scene scene, int width, int height, double resolution, boolean verbose,
            List<Effect> effects, double distance, boolean showArrows) {
        int resWidth = (int) (width * resolution);
        int resHeight = (int) (height * resolution);
        PixelData[][] pixelBuffer = new PixelData[resHeight][resWidth];
        int totalPixels = resWidth * resHeight;

        if (verbose)
            System.out.println("Casting rays");

        int currentProgress = -1;
        for (int y = 0; y < resHeight; y++) {
            for (int x = 0; x < resWidth; x++) {
                double[] screenUV = getNormalizedScreenCoordinates(x / resolution, y / resolution, width, height);
                pixelBuffer[y][x] = getPixelData(scene, screenUV[0], screenUV[1], showArrows);

                if (verbose) {
                    int currentPixel = y * resWidth + x + 1;
                    int progress = (currentPixel * 100) / totalPixels;
                    if (progress > currentProgress) {
                        System.out.print("\rCasting: " + "(" + progress + " %)" + "#".repeat(progress));
                        currentProgress = progress;
                    }
                }
            }
        }
        if (verbose)
            System.out.println();

        if (verbose)
            System.out.println("Applying FX");

        int effectIndex = 0;
        for (Effect effect : effects) {
            if (verbose)
                System.out.println("Applying " + effect.getName());

            pixelBuffer = effect.apply(pixelBuffer, resolution);

            if (verbose) {
                effectIndex++;
                int progress = (effectIndex * 10) / effects.size();
                System.out.print("\rApplying FX: " + "#".repeat(progress));
            }

            if (verbose)
                System.out.println("\nApplied " + effect.getName());
        }
        if (verbose)
            System.out.println();

        if (verbose)
            System.out.println("Drawing image");
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gfx = temp.getGraphics();
        int blockSize = (int) (1 / resolution);

        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                int pixelY = (int) (y * resolution);
                int pixelX = (int) (x * resolution);
                // Ensure pixelY and pixelX are within bounds
                if (pixelY >= resHeight)
                    pixelY = resHeight - 1;
                if (pixelX >= resWidth)
                    pixelX = resWidth - 1;

                gfx.setColor(pixelBuffer[pixelY][pixelX].getColor().toAWT());
                gfx.fillRect(x, y, blockSize, blockSize);
            }
        }

        return temp;
    }

    public static void renderToImage(Scene scene, int width, int height, List<Effect> effects, double distance)
            throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        System.out.println("Rendering to image...");
        image = render(scene, width, height, 1, true, effects, distance, false);
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

    public static PixelData getPixelData(Scene scene, double u, double v, boolean showArrows) {
        Camera cam = scene.getCamera();
        Vector3 eyePos = new Vector3(0, 0, (-1 / Math.tan(cam.getFOV() / 2)));
        Vector3 rayDir = new Vector3(u, v, 0).subtract(eyePos).rotate(cam.getPitch(), cam.getYaw(), cam.getTilt())
                .normalize();
        Ray ray = new Ray(eyePos.add(cam.getAnchor()), rayDir);
        RayHit hit = ray.cast(scene.getObjects());
        if (showArrows) {
            RayHit arrowHit = ray.castArrows(scene.getObjects());
            if (arrowHit != null)
                hit = arrowHit;
        }
        if (hit == null)
            return new PixelData(scene.getSkybox().getColor(rayDir), Double.POSITIVE_INFINITY,
                    GlobalSettings.SKY_EMISSION);
        if (ray.getOrigin().distance(hit.getHitPoint()) > GlobalSettings.MAX_RENDER_DISTANCE)
            return new PixelData(scene.getSkybox().getColor(rayDir), Double.POSITIVE_INFINITY,
                    GlobalSettings.SKY_EMISSION);
                    
        Color shadedColor = hit.getShape().getMaterial().getShader().shade(hit, scene, hit.getShape().getMaterial(), 0);
        double distance = ray.getOrigin().distance(hit.getHitPoint());
        double emission = hit.getShape().getMaterial().getEmissionAt(hit.getHitPoint());
        return new PixelData(shadedColor, distance, emission);
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