import layout.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class Runner {
    private final Cmd cmd;
    private Map<String, BufferedImage> cachedImages;

    public Runner(Cmd cmd) {
        this.cmd = cmd;
    }

    public void run() throws IOException {
        String inputDir = FileUtils.normalizeDir(cmd.getOptionValue("dir"));

        if (StringUtils.isNotEmpty(inputDir)) {
            Collection<File> files = FileUtils.searchFiles(inputDir);

            Layout layout = createLayout(cmd.getOptionValue("layout"));
            Sprite sprite = layout.createSprite(getImages(files, inputDir));
            saveSprite(sprite);
        }
    }

    public Map<String, Dimension> getImages(Collection<File> files, String inputDir) throws IOException {
        Map<String, Dimension> images = new LinkedHashMap<>(files.size());
        cachedImages = new HashMap<>();

        for (File file : files) {
            String path = FileUtils.getRelativePath(file.getAbsolutePath(), inputDir);
            BufferedImage image = ImageIO.read(file);

            cachedImages.put(path, image);
            images.put(path, new Dimension(image.getWidth(), image.getHeight()));
        }

        return images;
    }

    public void saveSprite(Sprite sprite) throws IOException {
        String imageFilename = cmd.getOptionValue("img");
        String cssFilename = cmd.getOptionValue("css");

        BufferedImage bufferedImage = new BufferedImage(sprite.getWidth(), sprite.getHeight(), TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        StringBuilder stringBuilder = new StringBuilder();

        String relativeImagePath = "../images/" + FilenameUtils.getName(imageFilename);
        //getRelativePath(imageFilename, FilenameUtils.getFullPath(cssFilename));

        for (Map.Entry<String, Point> entry : sprite.getImages().entrySet()) {
            BufferedImage img = cachedImages.get(entry.getKey());
            Point point = entry.getValue();
            graphics.drawImage(img, point.x, point.y, null);

            String className = StyleSheet.generateClassName(entry.getKey());
            stringBuilder.append(
//                    StyleSheet.createStyle(className, relativeImagePath, point.x, point.y, img.getWidth(), img.getHeight()));
                    StyleSheet.createStyleWithPercent(className, relativeImagePath, point.x, point.y, img.getWidth(), img.getHeight(), sprite.getWidth(), sprite.getHeight()));
        }

        FileUtils.saveImage(imageFilename, bufferedImage);
        FileUtils.saveTextFile(cssFilename, stringBuilder);

        printStatus(sprite, imageFilename);
    }

    private static void printStatus(Sprite sprite, String filename) {
        double spriteArea = sprite.getWidth() * sprite.getHeight();
        double efficiency = (sprite.getImagesArea() / spriteArea) * 100;
        System.out.println(String.format("Filled: %.2f%%", efficiency));
        System.out.println(String.format("Output: %s (%d x %d)", filename, sprite.getWidth(), sprite.getHeight()));
    }

    private static Layout createLayout(String option) {
        switch (option) {
            case "packed":
                return new PackedLayout();

            case "horizontal":
                return new HorizontalLayout();

            case "vertical":
            default:
                return new VerticalLayout();
        }

    }
}
