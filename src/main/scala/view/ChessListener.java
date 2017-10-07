package view;

import controller.Controller;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.mutable.ListBuffer;
import state.ChessException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

public class ChessListener implements MouseListener {

    private boolean hasSelectedSource = false;
    private Controller controller;
    public Tuple2<Object, Object> source;
    public Tuple2<Object, Object> target;
    private GraphicUi graphicUi;
    private LinkedList<JPanel> möglicheZüge;
    public ChessListener(Controller controller, GraphicUi graphicUi){

        this.controller = controller;
        this.graphicUi = graphicUi;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int x,y = 0;
        int size = 50;
        int default_pos = 100;

        if(!hasSelectedSource) {
            Rectangle pos = ((JPanel) e.getSource()).getBounds();
            x = (pos.x - default_pos) / size;
            y = (pos.y - default_pos) / size;

            source = new Tuple2<>(y, x);
            hasSelectedSource = true;

            möglicheZüge = new LinkedList<>();

            ListBuffer<Tuple2<Object, Object>> buf = controller.getPossibleMoves(new Tuple2<>(y,x));
            Iterator<Tuple2<Object, Object>> it = buf.iterator();
            while(it.hasNext()){

                Tuple2<Object, Object> zug = it.next();
                int posx = (int) zug._2();
                int posy = (int) zug._1();
                if(posx < 0 || posy < 0 || posx > 7 || posy > 7)
                    continue;

                if(controller.currentPlayer().hasFigure(controller.getFigure(zug)))
                    continue;

                JPanel panel = new JPanel();
                panel.setBounds(100+50*posx, 100+50*posy, 50,50);
                panel.setBackground(new Color(255, 255, 0));
                möglicheZüge.add(panel);
                graphicUi.getGamefieldLp().add(panel, 10);
            }
            graphicUi.getLp().revalidate();
        }else{
            for(JPanel zug: möglicheZüge){
                graphicUi.getGamefieldLp().remove(zug);
            }
            graphicUi.getGamefieldLp().revalidate();
            graphicUi.getGamefieldLp().repaint();

            Rectangle pos = ((JPanel) e.getSource()).getBounds();
            x = (pos.x - default_pos) / size;
            y = (pos.y - default_pos) / size;

            target=  new Tuple2<>(y, x);

            ChessException ex = controller.putFigureTo(source, target);

            if(ex.getClass().toString().contains("NoAllowedMoveException")){
                System.out.println("[chesslistener] Move is not allowed");
            }
            else if(ex.getClass().toString().contains("NoFigureException")){
                System.out.println("[chesslistener] You dont have suhc a figure");
            }
            else if(ex.getClass().toString().contains("OwnTargetException")){
                System.out.println("[chesslistener] Cannot go to place which is occupied by your own figure");
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
