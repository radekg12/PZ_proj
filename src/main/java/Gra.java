
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Gra extends JComponent implements ZmienJezykListener {
    private OknoGry frame;
    private int liczbaPol;
    private Pionek aktywny;
    private Pionek[][] pionkiTab;
    private Pole[][] plansza;
    private Gracz aktualnyGracz, aktualnyPrzeciwnik;
    private boolean start;
    private String player1String, player2String, endString, winString, turnString;


    public Gra(OknoGry frame, int width, int height, int liczbaPol) {
        setPreferredSize(new Dimension(width, height));
        this.frame = frame;
        this.liczbaPol = liczbaPol;
        Pionek.setSize(Math.min(width, height) / liczbaPol);
        Pole.setSize(Math.min(width, height) / liczbaPol);
        pionkiTab = new Pionek[liczbaPol][liczbaPol];
        plansza = new Pole[liczbaPol][liczbaPol];
        addMouseListener(new Ruchy());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Pionek.setSize(Math.min(getWidth() / liczbaPol, getHeight() / liczbaPol));
                Pole.setSize(Math.min(getWidth() / liczbaPol, getHeight() / liczbaPol));
            }
        });
        aktualnyPrzeciwnik = new Gracz(player1String, -1);
        aktualnyGracz = new Gracz(player2String, 1);
        generujPlansze();
        ustawPionki();
        sprRuchyPionkow();
        start = true;
        frame.getMenu().addZmienJezykListener(this::changeLocal);
        repaint();
    }

    private void generujPlansze() {
        for (int i = 0; i < liczbaPol; i++)
            for (int j = 0; j < liczbaPol; j++)
                if ((i + j) % 2 == 1) plansza[i][j] = new Pole(i, j, Pole.getCiemne());
                else plansza[i][j] = new Pole(i, j, Pole.getJasne());
    }

    private void ustawPionki() {
        for (int j = 0; j < 3; j++)
            for (int i = 0; i < liczbaPol; i++)
                if ((i + j) % 2 == 1) dodajPionek(i, j, aktualnyPrzeciwnik);

        for (int j = 1; j <= 3; j++)
            for (int i = 1; i <= liczbaPol; i++)
                if ((i + j) % 2 == 1) dodajPionek(liczbaPol - i, liczbaPol - j, aktualnyGracz);

    }

    private void dodajPionek(int x, int y, Gracz gracz) {
        Pionek p = new Pionek(x, y, gracz.getK());
        pionkiTab[x][y] = p;
        gracz.dodajPionek(p);
    }

    private void sprCzyDamka(Pionek pionek) {
        if (!jestNaPlanszy(pionek.getX(), pionek.getY() - pionek.getK()))
            pionek.ustawDamke();
    }

    private void ustawAktywnePola(Pionek pionek) {
        czyscAktywnePola();
        if ((pionek.getRodzajPionka() == RodzajPionka.JDama || pionek.getRodzajPionka() == RodzajPionka.CDama))
            ustawPolaDomki(pionek);
        //if (pionek == null) return;
        int x = pionek.getX() + 1;
        int y = pionek.getY() - pionek.getK();
        if (jestNaPlanszy(x, y)) {
            if (!poleZajete(x, y)) {
                plansza[pionek.getX() + 1][pionek.getY() - pionek.getK()].setAktywne(true);
                pionek.setMaRuch(true);
            } else if (jestNaPlanszy(x + 1, y - pionek.getK()) && !poleZajete(x + 1, y - pionek.getK()) && jetPrzeciwnikiem(x, y, pionek)) {
                plansza[pionek.getX() + 2][pionek.getY() - 2 * pionek.getK()].setPoleBicia(true);
                pionek.setMozeBic(true);
            }
            //moze bic do tylu
            y = pionek.getY() + pionek.getK();
            if (jestNaPlanszy(x, y) && poleZajete(x, y))
                if (jestNaPlanszy(x + 1, y + pionek.getK()) && jetPrzeciwnikiem(x, y, pionek) && !poleZajete(x + 1, y + pionek.getK())) {
                    plansza[pionek.getX() + 2][pionek.getY() + 2 * pionek.getK()].setPoleBicia(true);
                    pionek.setMozeBic(true);
                }
        }

        x = pionek.getX() - 1;
        y = pionek.getY() - pionek.getK();
        if (jestNaPlanszy(x, y)) {
            if (!poleZajete(x, y)) {
                plansza[pionek.getX() - 1][pionek.getY() - pionek.getK()].setAktywne(true);
                pionek.setMaRuch(true);
            } else if (jestNaPlanszy(x - 1, y - pionek.getK()) && jetPrzeciwnikiem(x, y, pionek) && !poleZajete(x - 1, y - pionek.getK())) {
                plansza[pionek.getX() - 2][pionek.getY() - 2 * pionek.getK()].setPoleBicia(true);
                pionek.setMozeBic(true);
            }
            //moze bic w tyl
            y = pionek.getY() + pionek.getK();
            if (jestNaPlanszy(x, y) && poleZajete(x, y))
                if (jestNaPlanszy(x - 1, y + pionek.getK()) && !poleZajete(x - 1, y + pionek.getK()) && jetPrzeciwnikiem(x, y, pionek)) {
                    plansza[pionek.getX() - 2][pionek.getY() + 2 * pionek.getK()].setPoleBicia(true);
                    pionek.setMozeBic(true);
                }
        }
    }

    //TODO rozbić metodę
    private void sprRuch() {
    }

    private void sprBiciePrzod() {
    }

    private void sprBicieTyl() {
    }

    private void ustawPolaDomki(Pionek pionek) {
        int x, y;
        for (x = pionek.getX() + 1, y = pionek.getY() + 1; x < 8 && y < 8; x++, y++)
            if (!mozeSieRuszyc(pionek, x, y, 1, 1)) break;
        for (x = pionek.getX() + 1, y = pionek.getY() - 1; x < 8 && y >= 0; x++, y--)
            if (!mozeSieRuszyc(pionek, x, y, 1, -1)) break;
        for (x = pionek.getX() - 1, y = pionek.getY() + 1; x >= 0 && y < 8; x--, y++)
            if (!mozeSieRuszyc(pionek, x, y, -1, 1)) break;
        for (x = pionek.getX() - 1, y = pionek.getY() - 1; x >= 0 && y >= 0; x--, y--)
            if (!mozeSieRuszyc(pionek, x, y, -1, -1)) break;
    }

    private boolean mozeSieRuszyc(Pionek pionek, int x, int y, int vx, int vy) {
        if (pionkiTab[x][y] == null) {
            plansza[x][y].setAktywne(true);
            pionek.setMaRuch(true);
        } else if (jetPrzeciwnikiem(x, y, pionek) && jestNaPlanszy(x + vx, y + vy) && !poleZajete(x + vx, y + vy)) {
            plansza[x + vx][y + vy].setPoleBicia(true);
            pionek.setMozeBic(true);
            return false;
        } else return false;
        return true;
    }

    private void czyscAktywnePola() {
        for (Pole[] pp : plansza)
            for (Pole p : pp) {
                p.setAktywne(false);
                p.setPoleBicia(false);
            }
    }

    private void czyscPolaRuchu() {
        for (Pole[] pp : plansza)
            for (Pole p : pp) {
                p.setAktywne(false);
            }
    }

    private void czyscAktywnePionki() {
        for (Pionek p : aktualnyPrzeciwnik.getPionki()) {
            p.setMaRuch(false);
            p.setMozeBic(false);
        }
    }


    private boolean jetPrzeciwnikiem(int x, int y, Pionek pionek) {
        return pionek != null && pionkiTab[x][y].getK() != pionek.getK();
    }

    private boolean poleZajete(int x, int y) {
        return pionkiTab[x][y] != null;
    }

    private boolean jestNaPlanszy(int x, int y) {
        return x >= 0 && x <= liczbaPol - 1 && y >= 0 && y <= liczbaPol - 1;
    }

    private void zmienKolej() {
        Gracz tmp = aktualnyGracz;
        aktualnyGracz = aktualnyPrzeciwnik;
        aktualnyPrzeciwnik = tmp;
        sprRuchyPionkow();
        sprCzyKoniecGry();
    }

    private void sprCzyKoniecGry() {
        if (aktualnyGracz.getPionki().isEmpty() || !(graczMaRuchy() || graczMaBicia()))
            start = false;
    }

    private void sprRuchyPionkow() {
        czyscAktywnePionki();
        for (Pionek p : aktualnyGracz.getPionki())
            ustawAktywnePola(p);
        czyscAktywnePola();
        if (graczMaBicia())
            aktualnyGracz.getPionki().stream().filter(p -> p.isMaRuch() && !p.isMozeBic()).forEach(p -> p.setMaRuch(false));
    }


    public void paint(Graphics g) {
        super.paintComponents(g);
        if (start) {
            if (aktualnyGracz.getK() == 1) g.setColor(Pionek.getJasny());
            else g.setColor(Pionek.getCiemny());
            g.fillOval(10, liczbaPol * Pole.getSize() + 10, 20, 20);
            g.drawString("<- "+turnString, 40, liczbaPol * Pole.getSize() + 25);
        } else {
            g.drawString(endString, 40, liczbaPol * Pole.getSize() + 50);
            g.setColor(aktualnyPrzeciwnik.getPionki().get(0).getKolor());
            g.drawString(aktualnyPrzeciwnik.getNazwa() + winString, 40, liczbaPol * Pole.getSize() + 75);
        }
        paintPlansza(g);
        paintPionki(g);
    }

    private void paintPionki(Graphics g) {
        if (aktywny != null) {
            g.setColor(aktywny.getKolor());
            g.fillOval(aktywny.getX() * Pionek.getSize() - 3, aktywny.getY() * Pionek.getSize() - 3, Pionek.getSize() + 6, Pionek.getSize() + 6);
        }

        ArrayList<Pionek> pionki = new ArrayList<>(aktualnyGracz.getPionki());
        pionki.addAll(aktualnyPrzeciwnik.getPionki());
        for (Pionek p : pionki) {
            if (p.isMozeBic()) {
                g.setColor(Pionek.getKolorBicia());
                g.fillOval(p.getX() * Pionek.getSize(), p.getY() * Pionek.getSize(), Pionek.getSize(), Pionek.getSize());
                g.setColor(p.getAktywnyKolor());
                g.fillOval(p.getX() * Pionek.getSize() + 3, p.getY() * Pionek.getSize() + 3, Pionek.getSize() - 6, Pionek.getSize() - 6);
            } else {
                g.setColor(p.isMaRuch() ? p.getAktywnyKolor() : p.getKolor());
                g.fillOval(p.getX() * Pionek.getSize(), p.getY() * Pionek.getSize(), Pionek.getSize(), Pionek.getSize());
            }
            if (p.getRodzajPionka() == RodzajPionka.JDama || p.getRodzajPionka() == RodzajPionka.CDama)
                g.drawImage(Pionek.getKorona(), p.getX() * Pionek.getSize(), p.getY() * Pionek.getSize(), Pionek.getSize(), Pionek.getSize(), null);

        }
    }

    private boolean graczMaBicia() {
        return aktualnyGracz.getPionki().stream().anyMatch(Pionek::isMozeBic);
    }

    private boolean graczMaRuchy() {
        return aktualnyGracz.getPionki().stream().anyMatch(Pionek::isMaRuch);
    }


    private void paintPlansza(Graphics g) {
        for (Pole[] pp : plansza)
            for (Pole p : pp) {
                g.setColor(p.getColor());
                g.fillRect(p.getKolumna() * Pole.getSize(), p.getWiersz() * Pole.getSize(), Pole.getSize(), Pole.getSize());
                if (p.isAktywne()) {
                    g.setColor(Pole.getAktywneKolor());
                    g.fillRect(p.getKolumna() * Pole.getSize(), p.getWiersz() * Pole.getSize(), Pole.getSize(), Pole.getSize());
                    g.setColor(p.getColor());
                    g.fillRect(p.getKolumna() * Pole.getSize() + 2, p.getWiersz() * Pole.getSize() + 2, Pole.getSize() - 4, Pole.getSize() - 4);
                }
                if (p.isPoleBicia()) {
                    g.setColor(Pole.getPoleBiciaKolor());
                    g.fillRect(p.getKolumna() * Pole.getSize(), p.getWiersz() * Pole.getSize(), Pole.getSize(), Pole.getSize());
                    g.setColor(p.getColor());
                    g.fillRect(p.getKolumna() * Pole.getSize() + 2, p.getWiersz() * Pole.getSize() + 2, Pole.getSize() - 4, Pole.getSize() - 4);
                }

            }
    }

    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ResourceBundle rb = event.getRb();
                player1String=rb.getString("player1String");
                player2String=rb.getString("player2String");
                endString=rb.getString("endString");
                winString=rb.getString("winString");
                turnString=rb.getString("turnString");
                repaint();
            }
        });
    }


    private class Ruchy extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            int i = e.getX() / Pole.getSize();
            int j = e.getY() / Pole.getSize();
            if (jestNaPlanszy(i, j))
                if (aktywny != null && aktywny.isMozeBic() && plansza[i][j].isPoleBicia())
                    bij(i, j);
                else if (aktywny != null && aktywny.isMaRuch() && plansza[i][j].isAktywne())
                    ruch(i, j);
                else {
                    if (pionkiTab[i][j] != null && pionkiTab[i][j].getK() == aktualnyGracz.getK() && (pionkiTab[i][j].isMaRuch())) {
                        aktywny = pionkiTab[i][j];
                        ustawAktywnePola(aktywny);
                        if (aktywny.isMozeBic()) czyscPolaRuchu();
                    }
                    repaint();
                }
        }
    }


    private void bij(int i, int j) {
        int vi = (aktywny.getX() - i) / Math.abs(aktywny.getX() - i);
        int vj = (aktywny.getY() - j) / Math.abs(aktywny.getY() - j);
        pionkiTab[aktywny.getX()][aktywny.getY()] = null;
        aktualnyPrzeciwnik.getPionki().remove(pionkiTab[i + vi][j + vj]);
        pionkiTab[i + vi][j + vj] = null;
        aktywny.ustaw(i, j);
        pionkiTab[i][j] = aktywny;
        aktywny.setMozeBic(false);
        //repaint(); ////
        sprCzyDamka(aktywny);
        sprRuchyPionkow();
        if (!aktywny.isMozeBic()) {
            aktywny = null;
            zmienKolej();
            //repaint(); ////
        } else ustawAktywnePola(aktywny);
        repaint();
    }

    private void ruch(int i, int j) {
        pionkiTab[aktywny.getX()][aktywny.getY()] = null;
        aktywny.ustaw(i, j);
        pionkiTab[i][j] = aktywny;
        sprCzyDamka(aktywny);
        aktywny = null;
        zmienKolej();
        //repaint(); //
        System.out.println("ruch");
        repaint();
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(OknoGry frame) {
        this.frame = frame;
    }
}
