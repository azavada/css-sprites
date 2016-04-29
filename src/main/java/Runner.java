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

            Layout layout = createLayout(cmd.getOptionValue("layout"));
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

        String relativeResultImagePath = "../images/" + FilenameUtils.getName(resultImage);

        for (File file : inputFiles) {
            String key = file.getAbsolutePath();
            BufferedImage img = cachedImages.get(key);
            Point point = sprite.getPoint(key);
            graphics.drawImage(img, point.x, point.y, null);

            String className = StyleSheet.generateClassName(FileUtils.getRelativePath(key, inputDir));
            stringBuilder.append(
//                    StyleSheet.createStyle(className, relativeResultImagePath, point.x, point.y, img.getWidth(), img.getHeight()));
                    StyleSheet.createStyleWithPercent(className, relativeResultImagePath, point.x, point.y, img.getWidth(), img.getHeight(), sprite.getWidth(), sprite.getHeight()));
        }

        FileUtils.saveImage(resultImage, bufferedImage);
        FileUtils.saveTextFile(resultCss, stringBuilder);

        printStatus(sprite, resultImage);
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
