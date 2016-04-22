import layout.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class Main {

    private static Map<String, BufferedImage> cachedImages;

    public static Collection<File> searchFiles(String dir) {
        return FileUtils.listFiles(
                new File(dir),
                FileFilterUtils.suffixFileFilter("png"),
                TrueFileFilter.INSTANCE);
    }

    public static String getRelativePath(String absolutePath, String dir) {
        int startIndex = absolutePath.indexOf(dir);
        return absolutePath.substring(startIndex + dir.length());
    }

    public static Map<String, Dimension> getImages(Collection<File> files, String inputDir) throws IOException {
        Map<String, Dimension> images = new LinkedHashMap<>(files.size());
        cachedImages = new HashMap<>();

        for (File file : files) {
            String path = getRelativePath(file.getAbsolutePath(), inputDir);
            BufferedImage image = ImageIO.read(file);

            cachedImages.put(path, image);
            images.put(path, new Dimension(image.getWidth(), image.getHeight()));
        }

        return images;
    }

    public static void saveSprite(Sprite sprite, String imageFile, String cssFile) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(sprite.getWidth(), sprite.getHeight(), TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, Point> entry : sprite.getImages().entrySet()) {
            BufferedImage img = cachedImages.get(entry.getKey());
            Point point = entry.getValue();
            graphics.drawImage(img, point.x, point.y, null);

            String className = StyleSheet.generateClassName(entry.getKey());
            stringBuilder.append(StyleSheet.createStyle(className, point.x, point.y, img.getWidth(), img.getHeight()));
        }

        ImageIO.write(bufferedImage, "PNG", new File(imageFile));

        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(cssFile)));
        writer.write(stringBuilder.toString());
        writer.close();

        double spriteArea = sprite.getWidth() * sprite.getHeight();
        double efficiency = (sprite.getImagesArea() / spriteArea) * 100;
        System.out.println(String.format("Filled: %.2f%%", efficiency));
        System.out.println(String.format("Output: %d x %d", sprite.getWidth(), sprite.getHeight()));
    }

    public static Layout createLayout(String option) {
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

    private static String normalizeDir(String dir) {
        if (StringUtils.isNotEmpty(dir)) {
            return FilenameUtils.normalize(dir + File.separatorChar);
        }

        return "";
    }

    public static void main(String[] args) throws IOException {
        Cmd cmd = new Cmd(args);

        String inputDir = normalizeDir(cmd.getOptionValue("dir"));
        String img = cmd.getOptionValue("img");
        String css = cmd.getOptionValue("css");

        if (StringUtils.isNotEmpty(inputDir) && StringUtils.isNotEmpty(img) && StringUtils.isNotEmpty(css)) {
            Collection<File> files = searchFiles(inputDir);

            Layout layout = createLayout(cmd.getOptionValue("layout"));
            Sprite sprite = layout.createSprite(getImages(files, inputDir));
            saveSprite(sprite, img, css);
        }
    }
}
