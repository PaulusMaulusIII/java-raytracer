package gameboy.utilities.data;

public class PixelBuffer {
    private PixelData[][] buffer;
    private int height;
    private int width;

    public PixelBuffer(int width, int height) {
        buffer = new PixelData[height][width];
        this.height = height;
        this.width = width;
    }

    public void setPixelData(int x, int y, PixelData data) {
        buffer[y][x] = data;
    }

    public PixelData getPixelData(int x, int y) {
        return buffer[y][x];
    }

    public PixelData[][] getBuffer() {
        return buffer;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
