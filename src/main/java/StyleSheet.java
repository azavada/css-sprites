import org.apache.commons.io.FilenameUtils;

import java.util.Locale;

public class StyleSheet {
    private static String formatValue(int val) {
        return val == 0 ? "0" : "-" + val + "px";
    }

    private static String createPercent(double val) {
        if (val == 0.00) {
            return "0";
        }

        return String.format(Locale.US, "%.2f%%", val * 100);
    }

    private static double safeDivide(double dividend, double divisor) {
        if (divisor != 0) {
            return dividend / divisor;
        }

        return 0;
    }

    public static String createStyle(String selector, String imagePath, int x, int y, int width, int height) {
        return String.format("%s { width: %dpx; height: %dpx; background: url(%s) %s %s no-repeat; }\r\n",
                selector, width, height, imagePath, formatValue(x), formatValue(y));
    }

    public static String createStyleWithPercent(String selector, String imagePath, int x, int y, int width, int height,
                                                int spriteWidth, int spriteHeight) {

        double positionX = safeDivide(x, spriteWidth - width);
        double positionY = safeDivide(y, spriteHeight - height);
        double sizeX = safeDivide(spriteWidth, width);
        double sizeY = safeDivide(spriteHeight, height);

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
