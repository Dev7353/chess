package view;

import controller.Controller;
import model.Player;
import view.GraphicUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by kmg on 04.10.17.
 */
public class ButtonListener implements ActionListener {

    private Controller controller;
    private GraphicUi graphicUi;

    public ButtonListener(Controller controller, GraphicUi graphicUi){
        this.controller = controller;
        this.graphicUi = graphicUi;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        graphicUi.getLp().setVisible(false);
        String playerA = graphicUi.getTxtFieldContent()[0];
        String playerB = graphicUi.getTxtFieldContent()[1];

        if(controller.currentPlayer() == null){
            System.out.println("[debug] no player specified, create new...");
            controller.setPlayerA(new Player(playerA));
        }

        if(controller.enemyPlayer() == null){
            System.out.println("[debug] no player specified, create new...");
            controller.setPlayerB(new Player(playerB));
        }


        graphicUi.draw(graphicUi.getContentPane());
        graphicUi.getContentPane().validate();


    }

}
