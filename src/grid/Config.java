package grid;

import java.awt.*;

/**
 * Configuration of grid and it's objects.
 * Configuration of grid component and classes or objects that belong to it
 * and you can config everything you want or use default values.
 * @author Majid Feyzi
 * */
public class Config {

    // Define drawable objects dimensions
    public final static int VERTEX_RADIUS = 10;
    public final static int EDGE_THICKNESS = 3;
    public final static int HIGHLIGHTED_EDGE_THICKNESS = EDGE_THICKNESS * 3;

    // Font name and font size of edge label
    public final static String LABEL_FONT_NAME = "Arial";
    public final static int LABEL_FONT_SIZE = 10;

    // Color of MST edges highlight and normal edges highlight
    public final static Color HIGHLIGHT_MST_EDGE_COLOR = new Color(209, 88, 88, 90);
    public final static Color HIGHLIGHT_EDGE_COLOR = Color.decode("#D15858");
}
