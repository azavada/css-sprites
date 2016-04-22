package layout;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Sprite {
    private Map<String, Point> images = new HashMap<>();

    private int width = 0;
    private int height = 0;
    private int area = 0;

    public void placeImage(String img, int x, int y, int width, int height) {
        images.put(img, new Point(x, y));
        area += width * height;

        this.width = Math.max(this.width, x + width);
        this.height = Math.max(this.height, y + height);
    }

    public Map<String, Point> getImages() {
        return images;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getImagesArea() {
        return area;
    }
}