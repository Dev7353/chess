package view;

import controller.Controller;
import scala.Tuple2;
import state.ChessException;
import state.NoAllowedMoveException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ChessListener implements MouseListener {

    boolean hasSelectedSource = false;
    private JPanel[][] coord;
    private Controller controller;
    private Tuple2<Object, Object> source;
    private Tuple2<Object, Object> target;
    public ChessListener(JPanel[][] coord, Controller controller){
        this.coord = coord;
        this.controller = controller;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int x,y = 0;
        int size = 50;

        if(hasSelectedSource == false) {
            Rectangle pos = ((JPanel) e.getSource()).getBounds();
            x = (pos.x - 100) / size;
            y = (pos.y - 100) / size;

            source = new Tuple2<>(new Integer(y), new Integer(x));
            //System.out.println("Source " + x + ", " + y);

            coord[x][y].setBackground(new Color(218, 165, 32)); // tile is yellow means selected
            hasSelectedSource = true;
            if (e.getSource().getClass().toString().contains("BauerTile"))
                ((BauerTile) e.getSource()).repaint();
        }else{
            Rectangle pos = ((JPanel) e.getSource()).getBounds();
            x = (pos.x - 100) / size;
            y = (pos.y - 100) / size;

            target=  new Tuple2<>(new Integer(y), new Integer(x));
            //System.out.println("Target " + x + ", " + y);

            try{
                controller.putFigureTo(source, target);
                hasSelectedSource = false;
            }catch(Exception ex){
                System.out.println("Not possible ma nigga ");
            }

        }
    }

    Color old;
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

       // old=((JPanel)e.getSource()).getBackground();
        //((JPanel)e.getSource()).setBackground(new Color(255,255,51));

    }

    @Override
    public void mouseExited(MouseEvent e) {
       // ((JPanel)e.getSource()).setBackground(old);
    }
}
