public class Kontroll {

    //Kontrollen fungerer som bindeledd mellom modell og Gui. "Hjernen" til applikasjonen.

    private char retning = 'n';
    private Gui gui;
    private Modell modell;
    private int rader, kolonner;
    private Thread slangetraad;
    private boolean spillStartet = false;
    private boolean gameOver = false;
    int hastighet = 1000; //millis.

    public Kontroll(int r, int k) {
        rader = r;
        kolonner = k;
        gui = new Gui(this, rader, kolonner);
        modell = new Modell(gui, this, rader, kolonner);
        slangetraad = new Thread(new Slangetraad());
    }

    //TODO traad som kaller paa flyttSlange etter gitt tid.
    class Slangetraad implements Runnable{
        @Override
        public void run() {
            while(!gameOver) {
                try {
                    Thread.sleep(hastighet);
                    flyttSlange();
                } catch (InterruptedException e) {
                    System.exit(1);
                }
            }
        }
    }

    public void avslutt() {
        System.exit(0);
    }

    public void skiftRetning(char r) {
        //Denne if gjor at brukeren ikke kan gaa retningen der de kom fra.
        if(!(retning == 'n' && r == 's' || retning == 's' && r == 'n' || retning == 'v' && r == 'o' || retning == 'o' && r == 'v')) {
            retning = r;
        }
    }

    private void flyttSlange() {

        int[] slangeHode = modell.hentHodet();
        int nyRad, nyKol;

        if(retning == 'n'){
            nyRad = slangeHode[0]-1;
            nyKol = slangeHode[1];
        }
        else if(retning == 's') {
            nyRad = slangeHode[0]+1;
            nyKol = slangeHode[1];
        }
        else if(retning == 'o') {
            nyRad = slangeHode[0];
            nyKol = slangeHode[1]+1;
        }
        else { //Da er retning 'v'
            nyRad = slangeHode[0];
            nyKol = slangeHode[1]-1;
        }

        try {
            int[] nyPos = {nyRad, nyKol};
            if(gui.hentRute(nyRad, nyKol).equals("o")) { //Hvis slangen treffer seg selv.
                gameOver();
            }
            modell.oppdaterSlangePos(nyPos);
            modell.skrivUtSlange();
        }
        catch (IndexOutOfBoundsException e) { //Hvis slangen krasjer i veggen.
            gameOver();
        }
    }

    public void nyDollar() {
        int tilfeldigRad = (int)(Math.random()*(rader-1));
        int tilfeldigKol = (int)(Math.random()*(kolonner-1));

        //Sjekker at pos ikke er i slangekroppen eller allerede er dollar.
        //TODO heller sjekke om den er i en liste heller enn aa kalle paa equals tre ganger.
        if (!gui.hentRute(tilfeldigRad, tilfeldigKol).equals("o") 
        && !gui.hentRute(tilfeldigRad, tilfeldigKol).equals("$")
        && !gui.hentRute(tilfeldigRad, tilfeldigKol).equals("@")) {
            gui.endreRute("$", tilfeldigRad, tilfeldigKol);
        } 
        else {
            nyDollar();
        }
    }

    public int hentScore() {
        return modell.hentScore();
    }

    public void gameOver() {
        gameOver = true;
        gui.blinke();
    }

    public void oekHastighet() {
        if(hastighet > 100) {
            hastighet -= 20;
        }
    }

    public boolean hentSpillStartet() {
        return spillStartet;
    }

    public void startSpill() {
        modell.skrivUtSlange();
        for (int i = 0; i < 10; i++) {
            nyDollar();
        }
        spillStartet = true;
        slangetraad.start();
    }

    public void restart() {
        modell.resett();
        gui.resett();
        startSpill();
    }
}
