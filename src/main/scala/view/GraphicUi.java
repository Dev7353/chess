package view;

import controller.Controller;
import model.GameField;
import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GraphicUi extends JFrame implements Observer {

    public void update(){
        this.gamefield.removeAll();
        redraw(coord);
    }
    private static JLayeredPane gamefield;
    public static JPanel[][] coord;
    private Container container;
    private static Controller controller;
    private JLayeredPane lp;
    private static GameField field;
    private static ChessListener chesslistener;

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


    public static void draw(Container c){
        gamefield = new JLayeredPane();
        coord = new JPanel[8][8];
        chesslistener = new ChessListener(coord, controller);
        gamefield.setLayout(null);
        gamefield.setPreferredSize(new Dimension(600,600));
        redraw(coord);
        c.add(gamefield);

    }

    public static void redraw(JPanel[][]coord){
        int ct = 0;
        int size = 50;
        for(int i = 0; i < field.field().length; ++i){
            for(int j = 0; j < field.field().length; ++j){

                String figure = field.field()[j][i].getClass().toString();

                Tile beige = new Tile(255, 235, 205);
                beige.setBounds(100+ i*size,100+j*size,size,size);
                beige.addMouseListener(chesslistener);

                Tile brown = new Tile(139, 69, 19);
                brown.setBounds(100+i*size,100+j*size,size,size);
                brown.addMouseListener(chesslistener);


                if(ct % 2 == 0) {
                    coord[i][j] = beige;
                    gamefield.add(beige, JLayeredPane.DEFAULT_LAYER);
                }
                else{
                    coord[i][j] = brown;
                    gamefield.add(brown, JLayeredPane.DEFAULT_LAYER);
                }



                if(figure.contains("Bauer")) {
                    BauerTile bauer = new BauerTile();
                    bauer.setBounds(100+i*size,100+j*size,size,size);
                    bauer.addMouseListener(chesslistener);
                    gamefield.add(bauer, JLayeredPane.DEFAULT_LAYER.intValue());

                }
                else if(figure.contains("Turm")) {
                    TurmTile turm = new TurmTile();
                    turm.setBounds(100+i*size,100+j*size,size,size);
                    turm.addMouseListener(chesslistener);
                    gamefield.add(turm, JLayeredPane.DEFAULT_LAYER.intValue());

                }
                else if(figure.contains("Läufer")) {
                    LäuferTile läufer= new LäuferTile();
                    läufer.setBounds(100+i*size,100+j*size,size,size);
                    läufer.addMouseListener(chesslistener);
                    gamefield.add(läufer, JLayeredPane.DEFAULT_LAYER.intValue());
                }
                else if(figure.contains("Offizier")) {
                    OffizierTile offizier= new OffizierTile();
                    offizier.setBounds(100+i*size,100+j*size,size,size);
                    offizier.addMouseListener(chesslistener);
                    gamefield.add(offizier, JLayeredPane.DEFAULT_LAYER.intValue());

                }
                else if(figure.contains("König")) {
                    KönigTile könig= new KönigTile();
                    könig.setBounds(100+i*size,100+j*size,size,size);
                    könig.addMouseListener(chesslistener);
                    gamefield.add(könig, JLayeredPane.DEFAULT_LAYER.intValue());

                }
                else if(figure.contains("Dame")) {
                    DameTile dame= new DameTile();
                    dame.setBounds(100+i*size,100+j*size,size,size);
                    dame.addMouseListener(chesslistener);
                    gamefield.add(dame, JLayeredPane.DEFAULT_LAYER.intValue());

                }
                if(j == 7)
                    ct += 2;
                else ++ct;

            }
        }
    }

}
