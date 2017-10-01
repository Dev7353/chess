package view;

import javax.swing.*;
import java.awt.*;

class TurmTile extends JPanel {

    private Image img = new ImageIcon("C:\\Users\\kiril\\IdeaProjects\\chess\\src\\main\\scala\\view\\turm1.png").getImage();

    public TurmTile() {
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
