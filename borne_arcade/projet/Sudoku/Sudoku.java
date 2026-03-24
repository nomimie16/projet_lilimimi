import MG2D.*;
import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texte;
import MG2D.geometrie.Couleur;
import MG2D.geometrie.Ligne;

import java.awt.Color;
import java.awt.Font;

import MG2D.audio.*;

public class Sudoku {

    // ------------ CONSTANTES ------------
    private static final int LARGEUR = 1280;
    private static final int HAUTEUR = 1024;
    private static final int TAILLE_GRILLE = 9;
    private static final int TAILLE_CASE = 80;
    private static final int LARGEUR_ITEM = 300, HAUTEUR_ITEM = 50;
    private static final int MARGE_SELECTION = 10;
    private static final int Y_ITEMS = 200;

    //Fenêtre
    private FenetrePleinEcran f;
    private ClavierBorneArcade clavier;
    private int status = 0;

    //Grilles de sudoku
    private int[][] grille;
    private int[][] grilleComplete;
    private int[][] grilleSolution;

    //Menu
    private Rectangle[] boutonMenu = new Rectangle[2];
    private Rectangle selection;
    private Rectangle jouer, exit;
    private int pointeur = 0;

    //Curseur pour la case sélectionnée
    private int posLigneSelection = 0;
    private int posColonneSelection = 0;
    private Texte[][] textesGrille;
    private Rectangle curseur;

    //Poistion de départ
    private int startX = 100;
    private int startY = 100;

    private Musique m;

    // ------------ CONSTRUCTEUR ------------
    public Sudoku() {
        //Création de la fen$etre plein écran
        f = new FenetrePleinEcran("SUDOKU");
        f.setVisible(true);
        f.setBackground(Color.BLACK);

        //Initialisation du clavier
        clavier = new ClavierBorneArcade();
        f.addKeyListener(clavier);
        f.getP().addKeyListener(clavier);

        //Géneartion du menu principal
        generateMenu();
    }

    // ------------ MENU -----------
    public void generateMenu() {
        //Effacer l'ecran et l'initialiser à noir
        f.effacer();
        f.setBackground(Color.BLACK);
        Couleur bleuFond = new Couleur(18, 22, 35);
        Couleur bleuBordure = new Couleur(30, 50, 80);

        //Grille décorative
        for (int i = 0; i < TAILLE_GRILLE; i++) {
            for (int j = 0; j < TAILLE_GRILLE; j++) {
                Rectangle cellFond = new Rectangle(bleuFond,
                        new Point(50 + j * 130, 50 + i * 100),
                        new Point(50 + (j + 1) * 130, 50 + (i + 1) * 100), false);
                f.ajouter(cellFond);
            }
        }

        //Ligne épaisses pour les blocs de 3
        for (int bloc = 0; bloc <= 3; bloc++) {
            int xV = 50 + bloc * 3 * 130;
            int yH = 50 + bloc * 3 * 100;
            for (int e = -1; e <= 1; e++) {
                f.ajouter(new Ligne(bleuBordure, new Point(xV + e, 50), new Point(xV + e, 950)));
                f.ajouter(new Ligne(bleuBordure, new Point(50, yH + e), new Point(1230, yH + e)));
            }
        }

        //Coins pour la déco
        int marge = 30, tailleCoin = 35;
        for (int e = 0; e < 3; e++) {
            ajouterCoinDecoratif(e, marge, tailleCoin);
        }

        ajouterTitreSousTitre();
        ajouterBoutons();
        f.rafraichir();
    }

    private void ajouterCoinDecoratif(int e, int marge, int tailleCoin) {
        //Haut-gauche
        f.ajouter(new Ligne(Couleur.BLANC, new Point(marge + e, marge), new Point(marge + tailleCoin, marge + e)));
        f.ajouter(new Ligne(Couleur.BLANC, new Point(marge + e, marge), new Point(marge + e, marge + tailleCoin)));
        //Haut-droit
        f.ajouter(new Ligne(Couleur.BLANC, new Point(LARGEUR - marge - tailleCoin, marge + e), new Point(LARGEUR - marge - e, marge + e)));
        f.ajouter(new Ligne(Couleur.BLANC, new Point(LARGEUR - marge - e, marge), new Point(LARGEUR - marge - e, marge + tailleCoin)));
        //Bas-gauche
        f.ajouter(new Ligne(Couleur.BLANC, new Point(marge + e, HAUTEUR - marge), new Point(marge + tailleCoin, HAUTEUR - marge - e)));
        f.ajouter(new Ligne(Couleur.BLANC, new Point(marge + e, HAUTEUR - marge - tailleCoin), new Point(marge + e, HAUTEUR - marge)));
        //Bas-droit
        f.ajouter(new Ligne(Couleur.BLANC, new Point(LARGEUR - marge - tailleCoin, HAUTEUR - marge - e), new Point(LARGEUR - marge - e, HAUTEUR - marge - e)));
        f.ajouter(new Ligne(Couleur.BLANC, new Point(LARGEUR - marge - e, HAUTEUR - marge - tailleCoin), new Point(LARGEUR - marge - e, HAUTEUR - marge)));
    }

    private void ajouterTitreSousTitre() {
        //Sous-titre
        Texte soustitre = new Texte(Couleur.BLEU, "P U Z Z L E   G A M E", new Font("Calibri", Font.PLAIN, 18), new Point(0, 0));
        soustitre.setA(new Point((LARGEUR - soustitre.getLargeur()) / 2, 585));
        f.ajouter(soustitre);

        //Titre principal
        Texte titre = new Texte(Couleur.BLANC, "SUDOKU", new Font("Arial", Font.BOLD, 130), new Point(0, 0));
        titre.setA(new Point((LARGEUR - titre.getLargeur()), 710));
        f.ajouter(titre);

        //Ligne déco sous le titre
        int cx = LARGEUR / 2;
        f.ajouter(new Ligne(Couleur.BLEU, new Point(cx - 80, 720), new Point(cx - 14, 720)));
        f.ajouter(new Ligne(Couleur.BLEU, new Point(cx + 14, 720), new Point(cx + 80, 720)));
        f.ajouter(new Rectangle(Couleur.BLEU, new Point(cx - 5, 715), new Point(cx + 5, 725), true));
    }

    //Bouton début du jeu : jouer ou quitter
    private void ajouterBoutons() {

        Couleur bleu = new Couleur(74, 158, 255);
        int bx = (LARGEUR - LARGEUR_ITEM) / 2;
        
        //Création du bouton jouer
        jouer = new Rectangle(bleu, new Point(bx, Y_ITEMS), new Point(bx + LARGEUR_ITEM, Y_ITEMS + HAUTEUR_ITEM), true);
        f.ajouter(jouer);

        //Création du bouton quitter
        exit = new Rectangle(Couleur.NOIR, new Point(bx, Y_ITEMS - 2 * HAUTEUR_ITEM), new Point(bx + LARGEUR_ITEM, Y_ITEMS - HAUTEUR_ITEM), true);
        f.ajouter(exit);

        //Contours blancs de boutons
        f.ajouter(new Rectangle(Couleur.BLANC, new Point(bx, Y_ITEMS), new Point(bx + LARGEUR_ITEM, Y_ITEMS + HAUTEUR_ITEM), false));
        f.ajouter(new Rectangle(Couleur.BLANC, new Point(bx, Y_ITEMS - 2 * HAUTEUR_ITEM), new Point(bx + LARGEUR_ITEM, Y_ITEMS - HAUTEUR_ITEM), false));

        //Textes des boutons
        Texte t1 = new Texte(Couleur.BLANC, "JOUER", new Font("Arial", Font.BOLD, 26), new Point(0, 0));
        t1.setA(new Point((LARGEUR - t1.getLargeur()) / 2, Y_ITEMS + 10));
        f.ajouter(t1);
        Texte t2 = new Texte(Couleur.BLANC, "QUITTER", new Font("Arial", Font.BOLD, 24), new Point(0, 0));
        t2.setA(new Point((LARGEUR - t2.getLargeur()) / 2, Y_ITEMS - 2 * HAUTEUR_ITEM + 13));
        f.ajouter(t2);

        //Ajouter les boutons dans le tableau et initialiser le pointeur
        boutonMenu[0] = jouer;
        boutonMenu[1] = exit;
        pointeur = 0;//commence à joueur
    }

    //Gère interaction du menu
    public int majMenu() {
        Couleur bleu = new Couleur(74, 158, 255);

        //pointeur vers le haut
        if (clavier.getJoyJ1HautTape() && pointeur > 0) {
            pointeur--;
            jouer.setCouleur(bleu);
            exit.setCouleur(Couleur.NOIR);
        }
        //vers le bouton
        if (clavier.getJoyJ1BasTape() && pointeur < 1) {
            pointeur++;
            jouer.setCouleur(Couleur.NOIR);
            exit.setCouleur(bleu);
        }
        //bouton cliqué
        if (clavier.getBoutonJ1ATape()) {
            if (pointeur == 1) {
                //quitte le jeu
                System.exit(0);
            } else {
                //démarre le jeu
                generateJeuSudoku();
                status = 1;
            }
        }

        f.rafraichir();
        return status;
    }

    // ---------------- JEU ----------------
    //genere la grille de sudoku
    public void generateJeuSudoku() {
        f.effacer();
        grille = new int[TAILLE_GRILLE][TAILLE_GRILLE];
        grilleComplete = new int[TAILLE_GRILLE][TAILLE_GRILLE];
        grilleSolution = new int[TAILLE_GRILLE][TAILLE_GRILLE];

        //génération de la grille
        Grille g = new Grille();
        grille = g.getGrille();
        grilleSolution = g.getSolution();
        //copie de la grille pour comparer les résultats plus tard
        for (int i = 0; i < TAILLE_GRILLE; i++) {
            System.arraycopy(grille[i], 0, grilleComplete[i], 0, TAILLE_GRILLE);
        }
        //initialisation des textes pour chaque case de la grille
        textesGrille = new Texte[TAILLE_GRILLE][TAILLE_GRILLE];
        posLigneSelection = 0;
        posColonneSelection = 0;
        //affichage de la grille et du curseur
        afficherGrille();
        mettreAJourCurseur();
        //lance la musique
        m = new Musique("Tied_Up.mp3");
        m.lecture();
    }

    //dessine la grille (bordures et les cases)
    private void afficherGrille() {
        //affiche les cases et bordures
        for (int i = 0; i < TAILLE_GRILLE; i++) {
            for (int j = 0; j < TAILLE_GRILLE; j++) {
                //affichage du fond de la case
                Rectangle fond = new Rectangle(Couleur.BLANC,
                        new Point(startX + j * TAILLE_CASE, startY + i * TAILLE_CASE),
                        new Point(startX + (j + 1) * TAILLE_CASE, startY + (i + 1) * TAILLE_CASE), true);
                f.ajouter(fond);
                //affichage de la bordure noire
                Rectangle bord = new Rectangle(Couleur.NOIR,
                        new Point(startX + j * TAILLE_CASE, startY + i * TAILLE_CASE),
                        new Point(startX + (j + 1) * TAILLE_CASE, startY + (i + 1) * TAILLE_CASE), false);
                f.ajouter(bord);
                //affiche le chiffre dans la case, si vide alors ""
                String txt = (grille[i][j] == 0) ? "" : String.valueOf(grille[i][j]);
                Couleur c = (grilleComplete[i][j] == 0) ? Couleur.BLEU : Couleur.NOIR;
                textesGrille[i][j] = new Texte(c, txt, new Font("Calibri", Font.TYPE1_FONT, 40),
                        new Point(startX + j * TAILLE_CASE + 25, startY + i * TAILLE_CASE + 25));
                f.ajouter(textesGrille[i][j]);
            }
        }

        //création des bordures épaisses pour les blocs 3x3
        int epaisseur = 3;
        for (int bloc = 0; bloc <= 3; bloc++) {
            int xV = startX + bloc * 3 * TAILLE_CASE;
            int yH = startY + bloc * 3 * TAILLE_CASE;

            for (int e = -epaisseur; e <= epaisseur; e++) {
                Ligne ligneV = new Ligne(Couleur.NOIR,
                        new Point(xV + e, startY), new Point(xV + e, startY + TAILLE_GRILLE * TAILLE_CASE));
                f.ajouter(ligneV);

                Ligne ligneH = new Ligne(Couleur.NOIR,
                        new Point(startX, yH + e), new Point(startX + TAILLE_GRILLE * TAILLE_CASE, yH + e));
                f.ajouter(ligneH);
            }
        }

        //création du curseur (case pour voir la sélection du joueur)
        curseur = new Rectangle(Couleur.ROUGE, new Point(startX, startY),
        new Point(startX + TAILLE_CASE, startY + TAILLE_CASE), false);
        f.ajouter(curseur);

        //afficher les informations des touches
        Texte info = new Texte(Couleur.BLANC,
                "F:+1  G:-1  H:effacer  Y:quitter",
                new Font("Calibri", Font.TYPE1_FONT, 20),
                new Point(500, 50));
        f.ajouter(info);

        f.rafraichir();
    }

    //réaction du jeu selon les touches cliquées par le joueur
    public void maj() {
        //déplacements du curseur selon le clic
        if (clavier.getJoyJ1HautTape() && posLigneSelection > 0) posLigneSelection--;
        if (clavier.getJoyJ1BasTape() && posLigneSelection < 8) posLigneSelection++;
        if (clavier.getJoyJ1GaucheTape() && posColonneSelection > 0) posColonneSelection--;
        if (clavier.getJoyJ1DroiteTape() && posColonneSelection < 8) posColonneSelection++;

        //saisie d'un chiffre dans la case
        if (grilleComplete[posLigneSelection][posColonneSelection] == 0) {
            if (clavier.getBoutonJ1ATape()) {
                grille[posLigneSelection][posColonneSelection] =
                        (grille[posLigneSelection][posColonneSelection] % 9) + 1;
                updateAffichageGrille();//maj l'affichage de notre jeu
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

        //quitter
        if (clavier.getBoutonJ1ZTape()) {
            generateMenu();
            status = 0;
        }

        //si le joueur rempli la grille
        if (isGrilleComplete()) {
            Texte t = new Texte(Couleur.VERT, "GAGNE !", new Font("Calibri", Font.TYPE1_FONT, 80), new Point(400, 500));
            f.ajouter(t);
            f.rafraichir();
            //attente et retour vers le menu
            try { Thread.sleep(4000); } catch (Exception e) {}
            generateMenu();
            status = 0;
        }

        mettreAJourCurseur();
        f.rafraichir();
    }

    //met à jour l'affichage des chiffres dans ttes les cases
    private void updateAffichageGrille() {
        for (int i = 0; i < TAILLE_GRILLE; i++)
            for (int j = 0; j < TAILLE_GRILLE; j++) {
                //si la case est vide afficher "" sinon le chiffre    
                String txt = (grille[i][j] == 0) ? "" : String.valueOf(grille[i][j]);
                textesGrille[i][j].setTexte(txt);
            }
    }

    //met à jour la postition du curseur sur la case actuellement sélectionnée
    private void mettreAJourCurseur() {
        int x = startX + posColonneSelection * TAILLE_CASE;
        int y = startY + posLigneSelection * TAILLE_CASE;
        curseur.setA(new Point(x, y));
        curseur.setB(new Point(x + TAILLE_CASE, y + TAILLE_CASE));
    }

    //vérifie si la grille est complètement et correctement remplie
    private boolean isGrilleComplete() {
        for (int i = 0; i < TAILLE_GRILLE; i++)
            for (int j = 0; j < TAILLE_GRILLE; j++)
                //si une case est fausse, la grille n'est pas complète
                if (grille[i][j] != grilleSolution[i][j])
                    return false;
        return true;//sinon la grille est correcte
    }

    //retourne létat actuel du jeu (0=menu, 1=jeu en cours)
    public int getStatus() {
        return status;
    }
}