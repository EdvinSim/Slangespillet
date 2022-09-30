import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

class Gui{

    //TODO game over vindu/beskjed.
    //TODO bagrunnsbilde.

    Kontroll kontroll;
    JLabel[][] brett; //Denne til aa referere til koordinater og modifisere ruter.
    JPanel rutenett; //Denne for aa visualisere brett.
    int rader, kolonner;
    JLabel score;
    Color morkGronn = new Color(0, 100, 0);
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 25);
    JPanel hovedpanel;

    public Gui(Kontroll kon, int r, int k) {
        kontroll = kon;
        rader = r;
        kolonner = k;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                System.exit(1); }
            
        JFrame vindu = new JFrame("Slangespillet");
        vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        hovedpanel = new JPanel();
        hovedpanel.setLayout(new BorderLayout());
        vindu.add(hovedpanel);

        //Legg til i hovedpanel
        hovedpanel.add(score(), BorderLayout.WEST);
        hovedpanel.add(startAvsluttknapp(), BorderLayout.EAST);
        hovedpanel.add(knappepanel(), BorderLayout.CENTER);
        hovedpanel.add(opprettSpillebrett(), BorderLayout.SOUTH);
        hovedpanel.addKeyListener(new Piltast()); //TODO funker ikke optimalt.
        hovedpanel.setFocusable(true); //Uten denne funker ikke KeyListener. Hvorfor ikke?

        vindu.pack();
        vindu.setVisible(true);
    }

    public JLabel score() {
        score = new JLabel("Score: 0");
        score.setPreferredSize(new Dimension(80, 80));
        score.setHorizontalAlignment(SwingConstants.CENTER);
        score.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        score.setOpaque(true);
        score.setBackground(Color.LIGHT_GRAY);
        return score;
    }

    public JButton startAvsluttknapp(){
        JButton knapp = new JButton("Start");

        class StartAvslutt implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(kontroll.hentSpillStartet() == false) {
                    knapp.setText("Avslutt");
                    kontroll.startSpill();
                    hovedpanel.requestFocus();
                } 
                else {
                    //kontroll.avslutt();
                    kontroll.restart();//TODO fjern
                }
            }
        }

        knapp.addActionListener(new StartAvslutt());
        return knapp;
    }
        
    class Retning implements ActionListener {
        char retning;

        Retning(char r) {
            retning = r;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            kontroll.skiftRetning(retning);
            hovedpanel.requestFocus();
        }
    }

    public JPanel knappepanel() {

        JPanel knapper = new JPanel();
        knapper.setLayout(new BorderLayout());
        JButton nord, sor, ost, vest;

        nord = new JButton("^");
        sor = new JButton("v");
        ost = new JButton(">");
        vest = new JButton("<");
        nord.addActionListener(new Retning('n'));
        sor.addActionListener(new Retning('s'));
        ost.addActionListener(new Retning('o'));
        vest.addActionListener(new Retning('v'));
        knapper.add(nord , BorderLayout.NORTH);
        knapper.add(sor, BorderLayout.SOUTH);
        knapper.add(ost, BorderLayout.EAST);
        knapper.add(vest, BorderLayout.WEST);

        knapper.setPreferredSize(new Dimension(100, 100));

        return knapper;
    }


    public JPanel opprettSpillebrett() {
        rutenett = new JPanel();
        rutenett.setLayout(new GridLayout(rader, kolonner));

        brett = new JLabel[rader][kolonner];
        
        for (int rad = 0; rad < rader; rad++ ){
            for (int kol = 0; kol < kolonner; kol++){
                JLabel nyRute = new JLabel("");
                //nyRute.setBorder(BorderFactory.createLineBorder(Color.BLACK)); //Synlig rutenett.
                nyRute.setPreferredSize(new Dimension(20, 20));
                nyRute.setVerticalAlignment(SwingConstants.CENTER);
                nyRute.setFont(font);
                brett[rad][kol] = nyRute;
                rutenett.add(nyRute);
            }
        }

        return rutenett;
    }

    public void endreRute(String streng, int rad, int kolonne) {
        JLabel rute = brett[rad][kolonne];

        if(streng.equals("$")) {
            rute.setForeground(morkGronn);
        }
        else if(streng.equals("@")) {
            rute.setForeground(Color.BLACK);
        }

        rute.setText(streng);
    }

    public String hentRute(int rad, int kolonne) {
        return brett[rad][kolonne].getText();
    }

    public void oppdaterScore() {
        score.setText("Score: " + kontroll.hentScore());
    }

    public void blinke() {
        for(int i = 0; i < 5; i++) {
            for(JLabel[] linje: brett) {
                for(JLabel rute: linje) {
                    rute.setForeground(Color.WHITE);
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(JLabel[] linje: brett) {
                for(JLabel rute: linje) {
                    rute.setForeground(Color.RED);
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resett(){
        for(JLabel[] rad: brett){
            for(JLabel rute: rad){
                rute.setText("");
            }
        }
    }




    
    class Piltast implements KeyListener{

        @Override
        public void keyPressed(KeyEvent e) {
            int nokkel = e.getKeyCode();
            if(nokkel == KeyEvent.VK_UP) {
                kontroll.skiftRetning('n');
            }
            else if(nokkel == KeyEvent.VK_DOWN) {
                kontroll.skiftRetning('s');
            }
            else if(nokkel == KeyEvent.VK_LEFT) {
                kontroll.skiftRetning('v');
            }
            else if(nokkel == KeyEvent.VK_RIGHT) {
                kontroll.skiftRetning('o');
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {}
        @Override
        public void keyTyped(KeyEvent e) {}
    }

}