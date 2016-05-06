package layout;

import java.awt.*;
import java.util.Iterator;
import java.util.Map;

public class HorizontalLayout implements Layout {
    private int whiteSpace = 0;

    public HorizontalLayout(int whiteSpace) {
        this.whiteSpace = whiteSpace;
    }

    @Override
    public Sprite createSprite(Map<String, Dimension> images) {
        Sprite sprite = new Sprite();
        int x = 0;

        Iterator<Map.Entry<String, Dimension>> it = images.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, Dimension> entry = it.next();
            Dimension dimension = entry.getValue();

            int whiteSpace = 0;
            if (it.hasNext()) {
                whiteSpace = this.whiteSpace;
            }

            sprite.placeImage(entry.getKey(), x, 0, dimension.width + whiteSpace, dimension.height);
            x += dimension.width + whiteSpace;
        }

        return sprite;
    }
}
