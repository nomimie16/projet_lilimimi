import MG2D.*;
import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texte;
import MG2D.geometrie.Couleur;

import java.awt.Color;
import java.awt.Font;

import MG2D.audio.*;

public class Sudoku {

    private final int largeur = 1280;
    private final int hauteur = 1024;
    private final int tailleGrille = 9;
    private final int tailleCase = 80;

    private int largeurItem = 300, hauteurItem = 50;
    private int margeSelection = 10;
    private int yItems = 200;

    private FenetrePleinEcran f;
    private ClavierBorneArcade clavier;

    private int status = 0;

    private int[][] grille;
    private int[][] grilleComplete;
    private int[][] grilleSolution;

    private Rectangle[] boutonMenu = new Rectangle[2];
    private Rectangle selection;
    private Rectangle jouer, exit;

    private int pointeur = 0;

    private int posLigneSelection = 0;
    private int posColonneSelection = 0;

    private Texte[][] textesGrille;
    private Rectangle curseur;

    private int startX = 100;
    private int startY = 100;

    private Musique m;

    public Sudoku() {
        f = new FenetrePleinEcran("SUDOKU");
        f.setVisible(true);
        f.setBackground(Color.BLACK);

        clavier = new ClavierBorneArcade();
        f.addKeyListener(clavier);
        f.getP().addKeyListener(clavier);

        generateMenu();
    }

    // ---------------- MENU ----------------
    public void generateMenu() {
        f.effacer();

        jouer = new Rectangle(Couleur.BLANC, new Point((largeur - largeurItem) / 2, yItems), largeurItem, hauteurItem, true);
        exit = new Rectangle(Couleur.BLANC, new Point((largeur - largeurItem) / 2, yItems - 2 * hauteurItem), largeurItem, hauteurItem, true);
        selection = new Rectangle(Couleur.ROUGE,
                new Point((largeur - largeurItem) / 2 - margeSelection, yItems - margeSelection),
                largeurItem + 2 * margeSelection,
                hauteurItem + 2 * margeSelection, true);

        boutonMenu[0] = jouer;
        boutonMenu[1] = exit;

        f.ajouter(selection);
        f.ajouter(jouer);
        f.ajouter(exit);

        Texte t1 = new Texte(Couleur.NOIR, "PLAY", new Font("Calibri", Font.TYPE1_FONT, 40), new Point());
        t1.setA(new Point((largeur - t1.getLargeur()) / 2, yItems + 10));
        f.ajouter(t1);

        Texte t2 = new Texte(Couleur.NOIR, "EXIT", new Font("Calibri", Font.TYPE1_FONT, 40), new Point());
        t2.setA(new Point((largeur - t2.getLargeur()) / 2, yItems - hauteurItem + 10));
        f.ajouter(t2);

        Texte titre = new Texte(Couleur.BLANC, "SUDOKU", new Font("Arial", Font.TYPE1_FONT, 150), new Point(300, 700));
        f.ajouter(titre);

        f.rafraichir();
    }

    public int majMenu() {
        if (clavier.getJoyJ1HautTape() && pointeur > 0) {
            pointeur--;
            selection.translater(0, hauteurItem * 2);
        }

        if (clavier.getJoyJ1BasTape() && pointeur < 1) {
            pointeur++;
            selection.translater(0, -hauteurItem * 2);
        }

        if (clavier.getBoutonJ1ATape()) {
            if (pointeur == 1) {
                System.exit(0);
            } else {
                generateJeuSudoku();
                status = 1;
            }
        }

        f.rafraichir();
        return status;
    }

    // ---------------- JEU ----------------
    public void generateJeuSudoku() {
        f.effacer();

        grille = new int[tailleGrille][tailleGrille];
        grilleComplete = new int[tailleGrille][tailleGrille];
        grilleSolution = new int[tailleGrille][tailleGrille];

        Grille g = new Grille();
        grille = g.getGrille();
        grilleSolution = g.getSolution();

        // Copier grille initiale pour cases fixes
        for (int i = 0; i < tailleGrille; i++)
            System.arraycopy(grille[i], 0, grilleComplete[i], 0, tailleGrille);

        textesGrille = new Texte[tailleGrille][tailleGrille];

        posLigneSelection = 0;
        posColonneSelection = 0;

        afficherGrille();
        mettreAJourCurseur();

        // Musique
        m = new Musique("Tied_Up.mp3");
        m.lecture();
    }

    private void afficherGrille() {
        for (int i = 0; i < tailleGrille; i++) {
            for (int j = 0; j < tailleGrille; j++) {

                // fond case
                Rectangle fond = new Rectangle(Couleur.BLANC,
                        new Point(startX + j * tailleCase, startY + i * tailleCase),
                        new Point(startX + (j + 1) * tailleCase, startY + (i + 1) * tailleCase), true);
                f.ajouter(fond);

                // bordure
                Rectangle bord = new Rectangle(Couleur.NOIR,
                        new Point(startX + j * tailleCase, startY + i * tailleCase),
                        new Point(startX + (j + 1) * tailleCase, startY + (i + 1) * tailleCase), false);
                f.ajouter(bord);

                String txt = (grille[i][j] == 0) ? "" : String.valueOf(grille[i][j]);
                Couleur c = (grilleComplete[i][j] == 0) ? Couleur.BLEU : Couleur.NOIR;

                textesGrille[i][j] = new Texte(c, txt,
                        new Font("Calibri", Font.TYPE1_FONT, 40),
                        new Point(startX + j * tailleCase + 25, startY + i * tailleCase + 25));
                f.ajouter(textesGrille[i][j]);
            }
        }

        // curseur
        curseur = new Rectangle(Couleur.ROUGE,
                new Point(startX, startY),
                new Point(startX + tailleCase, startY + tailleCase), false);
        f.ajouter(curseur);

        // info touches
        Texte info = new Texte(Couleur.BLANC,
                "A:+1  B:-1  C:effacer  Z:quitter",
                new Font("Calibri", Font.TYPE1_FONT, 20),
                new Point(100, 50));
        f.ajouter(info);

        f.rafraichir();
    }

    public void maj() {
        // Déplacements
        if (clavier.getJoyJ1HautTape() && posLigneSelection > 0) posLigneSelection--;
        if (clavier.getJoyJ1BasTape() && posLigneSelection < 8) posLigneSelection++;
        if (clavier.getJoyJ1GaucheTape() && posColonneSelection > 0) posColonneSelection--;
        if (clavier.getJoyJ1DroiteTape() && posColonneSelection < 8) posColonneSelection++;

        // Saisie
        if (grilleComplete[posLigneSelection][posColonneSelection] == 0) {
            if (clavier.getBoutonJ1ATape()) {
                grille[posLigneSelection][posColonneSelection] =
                        (grille[posLigneSelection][posColonneSelection] % 9) + 1;
                updateAffichageGrille();
            }
            if (clavier.getBoutonJ1BTape()) {
                int val = grille[posLigneSelection][posColonneSelection];
                grille[posLigneSelection][posColonneSelection] = (val == 0) ? 9 : val - 1;
                updateAffichageGrille();
            }
            if (clavier.getBoutonJ1CTape()) {
                grille[posLigneSelection][posColonneSelection] = 0;
                updateAffichageGrille();
            }
        }

        // Quitter
        if (clavier.getBoutonJ1ZTape()) {
            generateMenu();
            status = 0;
        }

        // Victoire
        if (isGrilleComplete()) {
            Texte t = new Texte(Couleur.VERT, "GAGNE !",
                    new Font("Calibri", Font.TYPE1_FONT, 80),
                    new Point(400, 500));
            f.ajouter(t);
            f.rafraichir();

            try { Thread.sleep(3000); } catch (Exception e) {}

            generateMenu();
            status = 0;
        }

        mettreAJourCurseur();
        f.rafraichir();
    }

    private void updateAffichageGrille() {
        for (int i = 0; i < tailleGrille; i++)
            for (int j = 0; j < tailleGrille; j++) {
                String txt = (grille[i][j] == 0) ? "" : String.valueOf(grille[i][j]);
                textesGrille[i][j].setTexte(txt);
            }
    }

    private void mettreAJourCurseur() {
        int x = startX + posColonneSelection * tailleCase;
        int y = startY + posLigneSelection * tailleCase;
        curseur.setA(new Point(x, y));
        curseur.setB(new Point(x + tailleCase, y + tailleCase));
    }

    private boolean isGrilleComplete() {
        for (int i = 0; i < tailleGrille; i++)
            for (int j = 0; j < tailleGrille; j++)
                if (grille[i][j] != grilleSolution[i][j])
                    return false;
        return true;
    }

    public int getStatus() {
        return status;
    }
}