package layout;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PackedLayout implements Layout {

    private Map<String, Dimension> createSortedMap(Map<String, Dimension> images) {
        List<Map.Entry<String, Dimension>> list = new LinkedList<>(images.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Dimension>>() {

            @Override
            public int compare(Map.Entry<String, Dimension> o1, Map.Entry<String, Dimension> o2) {
                int width1 = o1.getValue().width;
                int height1 = o1.getValue().height;
                int width2 = o2.getValue().width;
                int height2 = o2.getValue().height;

                int diff = Integer.compare(Math.max(width1, height1), Math.max(width2, height2));

                if (diff == 0) {
                    diff = Integer.compare(Math.min(width1, height1), Math.min(width2, height2));
                }

                if (diff == 0) {
                    diff = Integer.compare(width1, width2);
                }

                if (diff == 0) {
                    diff = Integer.compare(height1, height2);
                }

                return diff * -1; // reverse order
            }
        });

        Map<String, Dimension> result = new LinkedHashMap<>();
        for (Map.Entry<String, Dimension> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public Sprite createSprite(Map<String, Dimension> images) {
        Sprite sprite = new Sprite();
        Map<String, Dimension> map = createSortedMap(images);

        Dimension rect = map.values().iterator().next();
        RectangularPacker packer = new RectangularPacker(rect.width, rect.height);

        for (Map.Entry<String, Dimension> entry : map.entrySet()) {
            Dimension size = entry.getValue();

            Point point = packer.packRectangle(size.width, size.height);
            sprite.placeImage(entry.getKey(), point.x, point.y, size.width, size.height);
        }

        return sprite;
    }
}

