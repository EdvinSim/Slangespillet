import java.util.ArrayList;

class Modell {
    private Gui gui;
    private ArrayList<int[]> slangePos = new ArrayList<>();
    int score = 0;
    int rader, kolonner;
    Kontroll kontroll;

    public Modell(Gui g, Kontroll kon, int r, int k){
        gui = g;
        rader = r;
        kolonner = k;
        kontroll = kon;

        int[] slangehode = {rader-2, kolonner/2};  //StartPos til slangehodet.
        slangePos.add(slangehode);
    }

    public void oppdaterSlangePos(int[] nyPos){
        slangePos.add(0, nyPos);
        //Hvis slangen ikke skal spise.
        if (!gui.hentRute(nyPos[0], nyPos[1]).equals("$")) {
            int[] sistePos = slangePos.get(slangePos.size()-1);
            gui.endreRute("", sistePos[0], sistePos[1]);    //Visk ut siste pos.
            slangePos.remove(slangePos.size()-1);                   //Tar vekk siste pos.
        }
        else { //Spis
            score++;
            gui.oppdaterScore();
            kontroll.oekHastighet();
            kontroll.nyDollar();
        }
    }

    public void skrivUtSlange() {
        int[] hode = slangePos.get(0);
        gui.endreRute("@", hode[0], hode[1]);

        for (int i = 1; i < slangePos.size(); i++){
            int[] pos = slangePos.get(i);
            gui.endreRute("o", pos[0], pos[1]);
        }
    }

    public int hentScore() {
        return score;
    }

    public int[] hentHodet() {
        return slangePos.get(0);
    }

    public void resett() {
        score = 0;
        slangePos.clear();
    }
}