package layout;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class HorizontalLayout implements Layout {
    @Override
    public Sprite createSprite(List<FileInfo> images, String inputDir, String outputDir) {
        Size size = getSize(images);
        BufferedImage bi = new BufferedImage(size.width, size.height, TYPE_INT_ARGB);
        Graphics2D graphics = bi.createGraphics();

        int x = 0;
        StringBuilder sb = new StringBuilder();

        for (FileInfo image : images) {
            graphics.drawImage(image.image, x, 0, null);

            String className = StyleSheet.generateClassName(image.path);
            sb.append(StyleSheet.createStyle(className, x, 0, image.image.getWidth(), image.image.getHeight()));

            x += image.image.getWidth();
        }

        return new Sprite(bi, sb.toString());
    }

    private Size getSize(List<FileInfo> files) {
        int height = 0;
        int width = 0;

        for (FileInfo file : files) {
            height = Math.max(height, file.image.getHeight());
            width += file.image.getWidth();
        }

        return new Size(width, height);
    }
}
