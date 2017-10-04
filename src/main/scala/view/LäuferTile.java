package view;

import javax.swing.*;
import java.awt.*;

class LäuferTile extends JPanel {

    private Image img;

    public LäuferTile(int COLOR) {
        img = new ImageIcon(getClass().getResource("läufer" + COLOR+".png")).getImage();
        Dimension size = new Dimension(img.getWidth(this), img.getHeight(this));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
    }

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }

}
