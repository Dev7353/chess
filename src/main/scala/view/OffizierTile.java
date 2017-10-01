package view;

import javax.swing.*;
import java.awt.*;

class OffizierTile extends JPanel {

    private Image img = new ImageIcon("C:\\Users\\kiril\\IdeaProjects\\chess\\src\\main\\scala\\view\\offizier1.png").getImage();

    public OffizierTile() {
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
