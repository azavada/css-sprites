package layout;

import java.awt.*;
import java.util.Map;

public interface Layout {
    Sprite createSprite(Map<String, Dimension> images);
}
