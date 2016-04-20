package layout;

import java.util.List;

public interface Layout {
    Sprite createSprite(List<FileInfo> images);
}
