import com.sun.tools.javac.util.Iterators;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

public class Test extends JPanel {
    private int height;
    private int width;
    private int liczbaPol;
    private Pionek[][] pionkiTab;
    private Pole[][] plansza;
    private Pionek aktywny;
    private int kolej;
    private BufferedImage icon;
    private Gracz gracz1, gracz2;


    private Test(int width, int height, int liczbaPol) {
        try {
            icon = ImageIO.read(new java.io.File("icons/pionek-100.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.liczbaPol = liczbaPol;
        this.height = height / liczbaPol;
        this.width = width / liczbaPol;
        pionkiTab = new Pionek[liczbaPol][liczbaPol];
        plansza = new Pole[liczbaPol][liczbaPol];
        addMouseListener(new Ruchy());
        kolej = 1;
        gracz1 = new Gracz("gracz1", -1);
        gracz2 = new Gracz("gracz2", 1);
        ustawPionki();
        generujPlansze();
        //TODO usun nizej
        pionkiTab[5][2].ustawDamke();
        pionkiTab[2][5].ustawDamke();
        sprRuchyPionkow();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Warcaby");
        frame.setSize(500, 500);
        Test test = new Test(400, 400, 8);
        frame.add(test);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage(test.icon);
        frame.setVisible(true);
    }

    private void generujPlansze() {
        Pionek.setSize(width, height);
        for (int i = 0; i < liczbaPol; i++)
            for (int j = 0; j < liczbaPol; j++)
                if ((i + j) % 2 == 1) plansza[i][j] = new Pole(i, j, Pole.getCiemne());
                else plansza[i][j] = new Pole(i, j, Pole.getJasne());
    }

    private void ustawPionki() {
        for (int j = 0; j < 3; j++)
            for (int i = 0; i < liczbaPol; i++)
                if ((i + j) % 2 == 1) dodajPionek(i, j, gracz1);

        for (int j = 1; j <= 3; j++)
            for (int i = 1; i <= liczbaPol; i++)
                if ((i + j) % 2 == 1) dodajPionek(liczbaPol - i, liczbaPol - j, gracz2);

    }

    private void dodajPionek(int x, int y, Gracz gracz) {
        Pionek p = new Pionek(x, y, gracz.getK());
        pionkiTab[x][y] = p;
        gracz.dodajPionek(p);
    }

    public void sprCzyDamka(Pionek pionek) {
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

    private void czyscAktywnePionki() {
        Gracz przeciwnik = kolej == gracz1.getK() ? gracz2 : gracz1;
        for (Pionek p : przeciwnik.getPionki()) {
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
        kolej *= -1;
    }

    private void sprRuchyPionkow() {
        czyscAktywnePionki();
        Gracz gracz = kolej == gracz1.getK() ? gracz1 : gracz2;
        for (Pionek p : gracz.getPionki())
            ustawAktywnePola(p);
        czyscAktywnePola();
    }


    public void paint(Graphics g) {
        super.paintComponents(g);
        if (kolej == 1) g.setColor(Pionek.getJasny());
        else g.setColor(Pionek.getCiemny());
        g.fillOval(10, liczbaPol * height + 10, 20, 20);
        g.drawString(" <- twój ruch", 40, liczbaPol * height + 25);
        paintPlansza(g);
        paintPionki(g);
    }

    private void paintPionki(Graphics g) {
        if (aktywny != null) {
            g.setColor(aktywny.getKolor());
            g.fillOval(aktywny.getX() * width - 2, aktywny.getY() * height - 2, width + 4, height + 4);
        }

        for (Pionek p : gracz1.getPionki()) {
            g.setColor((p.isMaRuch() || p.isMozeBic()) ? p.getAktywnyKolor() : p.getKolor());
            g.fillOval(p.getX() * width, p.getY() * width, p.getWidth(), p.getHeight());
            if (p.getRodzajPionka() == RodzajPionka.JDama || p.getRodzajPionka() == RodzajPionka.CDama)
                g.drawImage(Pionek.getKorona(), p.getX() * width, p.getY() * height, p.getWidth(), p.getHeight(), null);
        }


    }

    public boolean graczMaBicia() {
        Gracz gracz = kolej == gracz1.getK() ? gracz1 : gracz2;
        return gracz.getPionki().stream().filter(p -> p.isMozeBic()).count() != 0;
    }

    public boolean graczMaRuchy() {
        Gracz gracz = kolej == gracz1.getK() ? gracz1 : gracz2;
        return gracz.getPionki().stream().filter(p -> p.isMaRuch()).count() != 0;
    }


    private void paintPlansza(Graphics g) {
        for (Pole[] pp : plansza)
            for (Pole p : pp) {
                g.setColor(p.getColor());
                g.fillRect(p.getKolumna() * width, p.getWiersz() * height, width, height);
                if (p.isAktywne()) {
                    g.setColor(Pole.getAktywneKolor());
                    g.fillRect(p.getKolumna() * width, p.getWiersz() * height, width, height);
                    g.setColor(p.getColor());
                    g.fillRect(p.getKolumna() * width + 2, p.getWiersz() * height + 2, width - 4, height - 4);
                }
                if (p.isPoleBicia()) {
                    g.setColor(Pole.getPoleBiciaKolor());
                    g.fillRect(p.getKolumna() * width, p.getWiersz() * height, width, height);
                    g.setColor(p.getColor());
                    g.fillRect(p.getKolumna() * width + 2, p.getWiersz() * height + 2, width - 4, height - 4);
                }

            }
    }


    class Ruchy implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int i = e.getX() / width;
            int j = e.getY() / height;
            if (aktywny != null && aktywny.isMozeBic() && plansza[i][j].isPoleBicia())
                bij(i, j);
            else if (aktywny != null && aktywny.isMaRuch() && plansza[i][j].isAktywne())
                ruch(i, j);
            else {
                if (pionkiTab[i][j] != null && pionkiTab[i][j].getK() == kolej) {
                    aktywny = pionkiTab[i][j];
                    ustawAktywnePola(aktywny);
                }
                repaint();
            }
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

    private void bij(int i, int j) {
        int vi = (aktywny.getX() - i) / Math.abs(aktywny.getX() - i);
        int vj = (aktywny.getY() - j) / Math.abs(aktywny.getY() - j);
        pionkiTab[aktywny.getX()][aktywny.getY()] = null;
        pionkiTab[i + vi][j + vj] = null;
        aktywny.ustaw(i, j);
        pionkiTab[i][j] = aktywny;
        repaint(); ////
        sprCzyDamka(aktywny);
        sprRuchyPionkow();
        if (!aktywny.isMozeBic()) {
            aktywny = null;
            zmienKolej();
            repaint(); ////
            sprRuchyPionkow();
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
        repaint(); //
        System.out.println("ruch");
        sprRuchyPionkow();
        repaint();
    }

    private boolean jestNasepnymPolem(int i, int j) {
        return Math.abs(i - aktywny.getX()) == 1 && aktywny.getK() * (aktywny.getY() - j) == 1;
    }
}
