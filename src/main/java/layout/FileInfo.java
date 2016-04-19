package layout;

import java.awt.image.BufferedImage;

public class FileInfo {
    public final BufferedImage image;
    public final String path;

    public FileInfo(BufferedImage image, String path) {
        this.image = image;
        this.path = path;
    }
}
