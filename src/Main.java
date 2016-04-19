import layout.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {

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

    public static List<FileInfo> getImages(Collection<File> files, String inputDir) throws IOException {
        List<FileInfo> result = new ArrayList<>();

        for (File file : files) {
            String path = getRelativePath(file.getAbsolutePath(), inputDir);
            result.add(new FileInfo(ImageIO.read(file), path));
        }

        return result;
    }

    public static void saveSprite(String dir, Sprite sprite) throws IOException {
        ImageIO.write(sprite.image, "PNG", new File(FilenameUtils.concat(dir, "images.png")));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FilenameUtils.concat(dir, "images.css"))));
        writer.write(sprite.styleSheet);
        writer.close();
    }

    public static Layout createLayout(String option) {
        switch(option) {
            case "horizontal":
                return new HorizontalLayout();

            case "vertical":
            default:
                return new VerticalLayout();
        }

    }

    public static void main(String[] args) throws IOException {
        Cmd cmd = new Cmd(args);

        String inputDir = cmd.getOptionValue("input-dir");
        String outputDir = cmd.getOptionValue("output-dir");

        if (StringUtils.isNotEmpty(inputDir) && StringUtils.isNotEmpty(outputDir)) {
            Collection<File> files = searchFiles(inputDir);

            Layout layout = createLayout(cmd.getOptionValue("layout"));
            Sprite sprite = layout.createSprite(getImages(files, inputDir), inputDir, outputDir);
            saveSprite(outputDir, sprite);
        }
    }
}
