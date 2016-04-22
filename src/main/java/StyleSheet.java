import org.apache.commons.io.FilenameUtils;

public class StyleSheet {
    private static String formatValue(int val) {
        return val == 0 ? "0" : "-" + val + "px";
    }

    public static String createStyle(String selector, String imagePath, int x, int y, int width, int height) {
        return String.format("%s { width: %dpx; height: %dpx; background: url(%s) %s %s no-repeat; }\r\n",
                selector, width, height, imagePath, formatValue(x), formatValue(y));
    }

    public static String generateClassName(String filePath) {
        String path = FilenameUtils.removeExtension(filePath);
        if (path.matches("^\\d.*")) {
            path = "_" + path;
        }
        return '.' + path.replaceAll("[^\\w-]", "_");
    }

}
