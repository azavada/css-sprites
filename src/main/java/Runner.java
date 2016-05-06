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
import java.util.List;
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
            List<File> files = (List<File>) FileUtils.searchFiles(inputDir);
            Layout layout = createLayout();
            Sprite sprite = layout.createSprite(getImages(files));
            saveSprite(sprite, files, inputDir);
        }
    }

    public Map<String, Dimension> getImages(Collection<File> files) throws IOException {
        Map<String, Dimension> images = new HashMap<>(files.size());
        cachedImages = new HashMap<>();

        for (File file : files) {
            String path = file.getAbsolutePath();
            BufferedImage image = ImageIO.read(file);

            cachedImages.put(path, image);
            images.put(path, new Dimension(image.getWidth(), image.getHeight()));
        }

        return images;
    }

    public void saveSprite(Sprite sprite, Collection<File> inputFiles, String inputDir) throws IOException {
        String resultImage = cmd.getOptionValue("img");
        String resultCss = cmd.getOptionValue("css");

        BufferedImage bufferedImage = new BufferedImage(sprite.getWidth(), sprite.getHeight(), TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        StringBuilder stringBuilder = new StringBuilder();

        String cssImagePath = getCssImagePath(resultImage);

        for (File file : inputFiles) {
            String key = file.getAbsolutePath();
            BufferedImage img = cachedImages.get(key);
            Point point = sprite.getPoint(key);
            graphics.drawImage(img, point.x, point.y, null);

            stringBuilder.append(getStyle(key, inputDir, cssImagePath, sprite, img, point));
        }

        FileUtils.saveImage(resultImage, bufferedImage);
        FileUtils.saveTextFile(resultCss, stringBuilder);

        printStatus(sprite, resultImage);
    }

    private String getStyle(String imagePath, String inputDir, String cssImagePath, Sprite sprite, BufferedImage bufferedImage, Point point) {
        String className = StyleSheet.generateClassName(FileUtils.getRelativePath(imagePath, inputDir));

        if (cmd.hasOptionValue("percents")) {
            return StyleSheet.createStyleWithPercent(
                    className,
                    cssImagePath,
                    point.x,
                    point.y,
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),
                    sprite.getWidth(),
                    sprite.getHeight());
        } else {
            return StyleSheet.createStyle(
                    className,
                    cssImagePath,
                    point.x,
                    point.y,
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight());
        }
    }

    private String getCssImagePath(String outputImage) {
        String cssImagePath = FilenameUtils.getName(outputImage);
        if (cmd.hasOptionValue("cssurl")) {
            cssImagePath = cmd.getOptionValue("cssurl");
        }
        return cssImagePath;
    }

    private static void printStatus(Sprite sprite, String filename) {
        double spriteArea = sprite.getWidth() * sprite.getHeight();
        double efficiency = (sprite.getImagesArea() / spriteArea) * 100;
        System.out.println(String.format("Filled: %.2f%%", efficiency));
        System.out.println(String.format("Output: %s (%d x %d)", filename, sprite.getWidth(), sprite.getHeight()));
    }

    private Layout createLayout() {
        int whiteSpace = cmd.getIntegerValue("white-spacing");
        switch (cmd.getOptionValue("layout")) {
            case "packed":
                return new PackedLayout(whiteSpace);

            case "horizontal":
                return new HorizontalLayout(whiteSpace);

            case "vertical":
            default:
                return new VerticalLayout(whiteSpace);
        }

    }
}
