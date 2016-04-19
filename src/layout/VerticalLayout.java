package layout;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class VerticalLayout implements Layout {
    @Override
    public Sprite createSprite(List<FileInfo> images, String inputDir, String outputDir) {
        Size size = getSize(images);
        BufferedImage bi = new BufferedImage(size.width, size.height, TYPE_INT_ARGB);
        Graphics2D graphics = bi.createGraphics();

        int y = 0;
        StringBuilder sb = new StringBuilder();

        for (FileInfo image : images) {
            graphics.drawImage(image.image, 0, y, null);

            String className = StyleSheet.generateClassName(image.path);
            sb.append(StyleSheet.createStyle(className, 0, y, image.image.getWidth(), image.image.getHeight()));

            y += image.image.getHeight();
        }

        return new Sprite(bi, sb.toString());
    }

    private Size getSize(List<FileInfo> files) {
        int height = 0;
        int width = 0;

        for (FileInfo file : files) {
            height += file.image.getHeight();
            width = Math.max(width, file.image.getWidth());
        }

        return new Size(width, height);
    }

}
