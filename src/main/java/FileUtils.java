import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class FileUtils {
    public static void saveTextFile(String cssFile, StringBuilder stringBuilder) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(cssFile)));
        writer.write(stringBuilder.toString());
        writer.close();
    }

    public static void saveImage(String imageFile, BufferedImage bufferedImage) throws IOException {
        ImageIO.write(bufferedImage, "PNG", new File(imageFile));
    }

    private static void sortFiles(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                if (file1.isDirectory() == file2.isDirectory()) {
                    String name1 = FilenameUtils.removeExtension(file1.getName());
                    String name2 = FilenameUtils.removeExtension(file2.getName());

                    int compareTo = name1.compareTo(name2);

                    if (compareTo == 0) {
                        return file1.getName().compareTo(file2.getName());
                    }

                    return compareTo;
                } else {
                    return file1.isDirectory() ? 1 : -1;
                }
            }
        });
    }

    private static void visitDirectory(File dir, String fileFilter, Collection<File> result) {
        File[] files = dir.listFiles();
        sortFiles(files);

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    visitDirectory(file, fileFilter, result);
                } else {
                    if (fileFilter.compareToIgnoreCase(FilenameUtils.getExtension(file.getName())) == 0) {
                        result.add(file);
                    }
                }
            }
        }
    }

    public static Collection<File> searchFiles(String dir) {
        Collection<File> result = new ArrayList<>();
        visitDirectory(new File(dir), "png", result);
        return result;
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
