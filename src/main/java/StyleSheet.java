import org.apache.commons.io.FilenameUtils;

public class StyleSheet {
    private static String formatValue(int val) {
        return val == 0 ? "0" : "-" + val + "px";
    }

    private static String createPercent(double val) {
        if (val == 0.00) {
            return "0";
        }

        return String.format("%.2f%%", val * 100);
    }

    public static String createStyle(String selector, String imagePath, int x, int y, int width, int height) {
        return String.format("%s { width: %dpx; height: %dpx; background: url(%s) %s %s no-repeat; }\r\n",
                selector, width, height, imagePath, formatValue(x), formatValue(y));
    }

    public static String createStyleWithPercent(String selector, String imagePath, int x, int y, int width, int height,
                                                int spriteWidth, int spriteHeight) {

        double positionX = (double) x / (spriteWidth - width);
        double positionY = (double) y / (spriteHeight - height);
        double sizeX = (double) spriteWidth / width;
        double sizeY = (double) spriteHeight / height;

        return String.format("%s { width: %dpx; height: %dpx; background: url(%s) %s %s no-repeat; background-size: %s %s}\r\n",
                selector, width, height, imagePath, createPercent(positionX), createPercent(positionY), createPercent(sizeX), createPercent(sizeY));
    }

    public static String generateClassName(String filePath) {
        String path = FilenameUtils.removeExtension(filePath);
        if (path.matches("^\\d.*")) {
            path = "_" + path;
        }
        return '.' + path.replaceAll("[^\\w-]", "_");
    }

}
