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
    private Controller controller;
    public Tuple2<Object, Object> source;
    public Tuple2<Object, Object> target;
    public ChessListener(Controller controller){
        this.controller = controller;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int x,y = 0;
        int size = 50;
        int default_pos = 100;

        if(hasSelectedSource == false) {
            Rectangle pos = ((JPanel) e.getSource()).getBounds();
            x = (pos.x - default_pos) / size;
            y = (pos.y - default_pos) / size;

            source = new Tuple2<>(new Integer(y), new Integer(x));
            //System.out.println("Source " + x + ", " + y);

            hasSelectedSource = true;
            if (e.getSource().getClass().toString().contains("BauerTile"))
                ((BauerTile) e.getSource()).repaint();
        }else{
            Rectangle pos = ((JPanel) e.getSource()).getBounds();
            x = (pos.x - default_pos) / size;
            y = (pos.y - default_pos) / size;

            target=  new Tuple2<>(y, x);


            System.out.println(source + ", " + target);
            ChessException ex = controller.putFigureTo(source, target);

            if(ex.getClass().toString().contains("NoAllowedMoveException")){
                System.out.println("Move is not allowed");
            }
            else if(ex.getClass().toString().contains("NoFigureException")){
                System.out.println("You dont have suhc a figure");
            }
            else if(ex.getClass().toString().contains("OwnTargetException")){
                System.out.println("Cannot go to place which is occupied by your own figure");
            }

            hasSelectedSource = false;


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

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
