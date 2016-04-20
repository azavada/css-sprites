package layout;

import java.awt.*;
import java.util.Map;

public class VerticalLayout implements Layout {

    @Override
    public Sprite createSprite(Map<String, Dimension> images) {
        Sprite sprite = new Sprite();
        int y = 0;

        for (Map.Entry<String, Dimension> entry : images.entrySet()) {
            Dimension dimension = entry.getValue();
            sprite.placeImage(entry.getKey(), 0, y, dimension.width, dimension.height);
            y += dimension.height;
        }

        return sprite;
    }

}
