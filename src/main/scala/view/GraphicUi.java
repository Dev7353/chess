package view;

import controller.Controller;
import model.GameField;
import observer.Observer;
import scala.Tuple2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GraphicUi extends JFrame implements Observer {

    public void update(){
        updateFigures();
    }
    private static JLayeredPane gamefield;
    public static JPanel[][] coord;
    private Container container;
    private static Controller controller;
    private JLayeredPane lp;
    private static GameField field;
    private static ChessListener chesslistener;
    private static JPanel[][] referenceBackup = new JPanel[8][8];

    private static int WHITE = 0;
    private static int BLACK = 1;

    public GraphicUi(Controller controller, GameField field){
        this.controller = controller;
        this.controller.add(this);
        this.setSize(590, 620);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        container = this.getContentPane();
        this.field = field;
        lp = new JLayeredPane();
        ActionListener btnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                lp.setVisible(false);

                draw(container);

            }
        };
        Cover cover = new Cover(new ImageIcon("C:\\Users\\kiril\\IdeaProjects\\chess\\src\\main\\scala\\view\\chess.jpg").getImage());
        JLabel header= new JLabel("Chess 0.0.0");
        header.setFont(new Font("ComicSans", Font.PLAIN, 50));
        header.setBounds(200, 120 ,300,50);

        JTextField txtPlayerA = new JTextField("Player A...");
        JTextField txtPlayerB = new JTextField("Player B...");
        txtPlayerA.setBounds(250,170,150,30);
        txtPlayerB.setBounds(250,210,150,30);

        JButton btnStart = new JButton("Start");
        btnStart.setBounds(250,300,100,50);

        btnStart.addActionListener(btnListener);

        lp.add(cover, JLayeredPane.DEFAULT_LAYER);
        lp.add(header, new Integer(JLayeredPane.DEFAULT_LAYER.intValue()+1));
        lp.add(txtPlayerA, new Integer(JLayeredPane.DEFAULT_LAYER.intValue()+1));
        lp.add(txtPlayerB, new Integer(JLayeredPane.DEFAULT_LAYER.intValue()+1));
        lp.add(btnStart, new Integer(JLayeredPane.DEFAULT_LAYER.intValue()+1));



        this.add(lp);
        this.setResizable(false);
        this.setVisible(true);
    }


    public  void draw(Container c){
        gamefield = new JLayeredPane();
        JLabel currentPlayer = new JLabel("Current Player: " + controller.currentPlayer());
        currentPlayer.setBounds(100,50,120,30);

        gamefield.add(currentPlayer);
        coord = new JPanel[8][8];
        chesslistener = new ChessListener(controller);
        gamefield.setLayout(null);
        gamefield.setPreferredSize(new Dimension(600,600));
        build();
        c.add(gamefield);

    }
    public JPanel copy;

    public  void build(){
        int ct = 0;
        int size = 50;
        for(int i = 0; i < field.field().length; ++i){
            for(int j = 0; j < field.field().length; ++j){

                Tile beige = new Tile(255, 235, 205);
                beige.setBounds(100+ i*size,100+j*size,size,size);
                beige.addMouseListener(chesslistener);

                Tile brown = new Tile(139, 69, 19);
                brown.setBounds(100+i*size,100+j*size,size,size);
                brown.addMouseListener(chesslistener);


                if(ct % 2 == 0) {
                    gamefield.add(beige, JLayeredPane.DEFAULT_LAYER);
                }
                else{
                    gamefield.add(brown, JLayeredPane.DEFAULT_LAYER);
                }
                String figure = field.field()[j][i].getClass().toString();
                if(figure.contains("Bauer")) {
                    BauerTile bauer;
                    if(j < 2)
                        bauer = new BauerTile(WHITE);
                    else
                        bauer = new BauerTile(BLACK);
                    bauer.setBounds(100+i*size,100+j*size,size,size);
                    bauer.addMouseListener(chesslistener);
                    gamefield.add(bauer, JLayeredPane.DEFAULT_LAYER.intValue());

                    referenceBackup[j][i] = bauer;

                }
                else if(figure.contains("Turm")) {
                    TurmTile turm;
                    if(j < 2)
                         turm = new TurmTile(WHITE);
                    else
                         turm = new TurmTile(BLACK);
                    turm.setBounds(100+i*size,100+j*size,size,size);
                    turm.addMouseListener(chesslistener);
                    gamefield.add(turm, JLayeredPane.DEFAULT_LAYER.intValue());

                    referenceBackup[j][i] = turm;


                }
                else if(figure.contains("Läufer")) {
                    LäuferTile läufer;
                    if(j < 2)
                        läufer = new LäuferTile(WHITE);
                    else
                        läufer = new LäuferTile(BLACK);
                    läufer.setBounds(100+i*size,100+j*size,size,size);
                    läufer.addMouseListener(chesslistener);
                    gamefield.add(läufer, JLayeredPane.DEFAULT_LAYER.intValue());

                    referenceBackup[j][i] = läufer;
                }
                else if(figure.contains("Offizier")) {
                    OffizierTile offizier;
                    if(j < 2)
                        offizier= new OffizierTile(WHITE);
                    else
                        offizier= new OffizierTile(BLACK);
                    offizier.setBounds(100+i*size,100+j*size,size,size);
                    offizier.addMouseListener(chesslistener);
                    gamefield.add(offizier, JLayeredPane.DEFAULT_LAYER.intValue());

                    referenceBackup[j][i] = offizier;
                }
                else if(figure.contains("König")) {
                    KönigTile könig;
                    if(j < 2)
                        könig = new KönigTile(WHITE);
                    else
                        könig = new KönigTile(BLACK);
                    könig.setBounds(100+i*size,100+j*size,size,size);
                    könig.addMouseListener(chesslistener);
                    gamefield.add(könig, JLayeredPane.DEFAULT_LAYER.intValue());

                    referenceBackup[j][i] = könig;
                }
                else if(figure.contains("Dame")) {
                    DameTile dame;
                    if(j < 2)
                        dame = new DameTile(WHITE);
                    else
                        dame = new DameTile(BLACK);
                    dame.setBounds(100+i*size,100+j*size,size,size);
                    dame.addMouseListener(chesslistener);
                    gamefield.add(dame, JLayeredPane.DEFAULT_LAYER.intValue());

                    referenceBackup[j][i] = dame;
                }


                if(j == 7)
                    ct += 2;
                else ++ct;

            }
        }
    }

    public void updateFigures(){

        int size = 50;
        Tuple2<Integer, Integer> source = new Tuple2<>((int)chesslistener.source._1, (int)chesslistener.source._2);
        Tuple2<Integer, Integer> target = new Tuple2<>((int)chesslistener.target._1, (int)chesslistener.target._2);

        /* debugging
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 8; ++j){

                System.out.println("" + i + ", " + j + ": " + referenceBackup[i][j]);
            }
        }*/

        referenceBackup[source._1][source._2].setBounds(100+target._2*size, 100+target._1*size, size, size);

        //update reference backup table
        referenceBackup[target._1][target._2] =  referenceBackup[source._1][source._2];
        referenceBackup[source._1][source._2] = null;

    }

}
