import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class FileUtils {
    public static void saveTextFile(String cssFile, StringBuilder stringBuilder) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(cssFile)));
        writer.write(stringBuilder.toString());
        writer.close();
    }

    public static void saveImage(String imageFile, BufferedImage bufferedImage) throws IOException {
        ImageIO.write(bufferedImage, "PNG", new File(imageFile));
    }

    public static Collection<File> searchFiles(String dir) {
        return org.apache.commons.io.FileUtils.listFiles(
                new File(dir),
                FileFilterUtils.suffixFileFilter("png"),
                TrueFileFilter.INSTANCE);
    }

    public static String getRelativePath(String absolutePath, String dir) {
        int startIndex = absolutePath.indexOf(dir);
        return absolutePath.substring(startIndex + dir.length());
    }

    public static String normalizeDir(String dir) {
        if (StringUtils.isNotEmpty(dir)) {
            return FilenameUtils.normalize(dir + File.separatorChar);
        }

        return "";
    }
}
