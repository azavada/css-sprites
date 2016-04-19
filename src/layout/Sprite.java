package layout;

import java.awt.image.BufferedImage;

public class Sprite {
    public final BufferedImage image;
    public final String styleSheet;

    public Sprite(BufferedImage image, String styleSheet) {
        this.image = image;
        this.styleSheet = styleSheet;
    }
}
