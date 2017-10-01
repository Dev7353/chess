package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

class Tile extends JPanel {

    //private Image img;

    /*public Tile(String img) {
        this(new ImageIcon(img).getImage());
    }*/

    public Tile(int r, int g, int b) {
        setPreferredSize(new Dimension(60,60));
        setBackground(new Color(r, g, b));
    }

}
