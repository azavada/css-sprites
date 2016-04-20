package layout;

import java.awt.*;
import java.util.Map;

public class HorizontalLayout implements Layout {

    @Override
    public Sprite createSprite(Map<String, Dimension> images) {
        Sprite sprite = new Sprite();
        int x = 0;

        for (Map.Entry<String, Dimension> entry : images.entrySet()) {
            Dimension dimension = entry.getValue();
            sprite.placeImage(entry.getKey(), x, 0, dimension.width, dimension.height);
            x += dimension.width;
        }

        return sprite;
    }
}
