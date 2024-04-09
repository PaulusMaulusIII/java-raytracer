package gameboy.core;

import gameboy.utilities.Camera3D;
import gameboy.utilities.Scene3D;
import gameboy.utilities.data.PixelBuffer;
import gameboy.utilities.data.PixelData;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {
    public PixelBuffer render(Scene3D scene, int width, int height) {
        PixelBuffer buffer = new PixelBuffer(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double[] screenUV = getNormalizedScreenCoordinates(x, y, width, height);

                buffer.SetPixelData(x, y, getPixelData(scene, screenUV[0], screenUV[1]));
            }
        }

        return buffer;
    }

    public void render(Scene3D scene, GraphicsContext graphicsContext, int width, int height, double resolution) {
        long time = System.currentTimeMillis();
        int blockSize = (int) (1 / resolution);

        for (int x = 0; x < width; x += blockSize) {
            for (int y = 0; y < height; y += blockSize) {
                double[] uv = getNormalizedScreenCoordinates(x, y, width, height);
                PixelData pixelData = getPixelData(scene, uv[0], uv[1]);

                graphicsContext.setFill(Color.WHITE);
                graphicsContext.fillRect(x, y, blockSize, blockSize);
                if (pixelData != null)
                    graphicsContext.setFill(pixelData.getColor());

                graphicsContext.fillRect(x, y, blockSize, blockSize);
            }
        }

        long deltaTime = System.currentTimeMillis() - time;

        System.out.println("Rendered Scene:");
        System.out.println("Camera at: " + scene.getCurrentCamera().getPosition().toString());
        System.out.println("With " + Math.toDegrees(scene.getCurrentCamera().getPitch()) + "° of Pitch");
        System.out.println("And " + Math.toDegrees(scene.getCurrentCamera().getYaw()) + "° of Yaw");
        System.out.println("With a FOV of: " + Math.toDegrees(scene.getCurrentCamera().getFOV()) + "°");
        System.out.println("At an Resolution of " + resolution * 100 + "%");
        System.out.println("Took " + deltaTime + "ms");
    }

    public double[] getNormalizedScreenCoordinates(int x, int y, double width, double height) {
        double u = 0, v = 0;
        if (width > height) {
            u = (double) (x - width / 2 + height / 2) / height * 2 - 1;
            v = -((double) y / height * 2 - 1);
        } else {
            u = (double) x / width * 2 - 1;
            v = -((double) (y - height / 2 + width / 2) / width * 2 - 1);
        }

        return new double[] { u, v };
    }

    public PixelData getPixelData(Scene3D scene, double u, double v) {
        Camera3D cam = scene.getCurrentCamera();
        Vector3 eyePos = new Vector3(0, 0, (-1 / Math.tan(cam.getFOV() / 2)));
        Vector3 rayDir = new Vector3(u, v, 0).subtract(eyePos).normalize().rotate(cam.getPitch(), cam.getYaw());
        Ray ray = new Ray(eyePos.add(cam.getPosition()), rayDir);

        RayHit hit = scene.castRay(ray, scene.getChildren());
        if (hit != null)
            return new PixelData(hit, scene.getLights(), scene.getChildren());

        return null;
    }
}
