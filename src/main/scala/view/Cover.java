package view;

import javax.swing.*;
import java.awt.*;

class Cover extends JPanel {

    private Image img;

    public Cover(String img) {
        this(new ImageIcon(img).getImage());
    }

    public Cover(Image img) {
        this.img = img;
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
