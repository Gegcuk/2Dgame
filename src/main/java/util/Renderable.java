package util;

import java.awt.*;

public interface Renderable {

    int getWorldX();
    int getWorldY();
    int getScreenX();
    int getScreenY();
    int getHeight();
    void draw(Graphics2D graphics2D);

}
