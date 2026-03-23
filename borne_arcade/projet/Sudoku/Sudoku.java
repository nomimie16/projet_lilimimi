import MG2D.*;

import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Texte;
import MG2D.geometrie.Couleur;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import MG2D.audio.*;
import 
grille.*;
public class Sudoku {

    //CONSTANTES
    private int largeur = 1280;
    private int hauteur = 1024;
    
    private final int tailleGrille = 9;
    private final int tailleCase = 80;
    private final int margeGrille = 50;
    
    private int largeurItem = 300, hauteurItem = 50;
    private int margeSelection = 10;
    private int yItems = 200;
    
    private FenetrePleinEcran f;
    private ClavierBorneArcade clavier;
    
    //ATTRIBUTS
    private int status;
    
    private int[][] grille;
    private int[][] grilleComplete;
    private int[][] grilleSolution;
    
    private Rectangle[] boutonMenu = new Rectangle[2];
    private Rectangle selection;
    
    private Rectangle jouer, exit;
    
    private int pointeur = 0;
    
    private int posLigneSelection = 0;
    private int posColonneSelection = 0;
    
    private Musique m;
    
    private Texte[][] textesGrille;
    
    
    //CONSTRUCTEUR
    public Sudoku(){
        f = new FenetrePleinEcran("SUDOKU");
        f.setVisible(true);
        f.setBackground(Color.BLUE);
        clavier = new ClavierBorneArcade();
        f.addKeyListener(clavier);
        f.getP().addKeyListener(clavier);
        
        jouer = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem)/2,yItems),largeurItem,hauteurItem, true);
        exit = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem)/2,yItems-2*hauteurItem),largeurItem,hauteurItem, true);
        selection = new Rectangle(Couleur.ROUGE, new Point((largeur-largeurItem)/2-margeSelection,yItems-margeSelection),largeurItem+2*margeSelection,hauteurItem+2*margeSelection, true);
        
        f.ajouter(selection);
        this.boutonMenu[0]= jouer;
        this.boutonMenu[1]=exit;
        f.ajouter(boutonMenu[0]);
        f.ajouter(boutonMenu[1]);
        
        Texte texteJouer=new Texte(Couleur.NOIR,"PLAY",new Font("Calibri", Font.TYPE1_FONT, 40),new Point());
        texteJouer.setA(new Point(((largeur-texteJouer.getLargeur())/2),yItems+(hauteurItem-texteJouer.getHauteur())/2));
        f.ajouter(texteJouer);
        
        Texte texteExit=new Texte(Couleur.NOIR,"EXIT",new Font("Calibri", Font.TYPE1_FONT, 40),new Point());
        texteExit.setA(new Point(((largeur-texteExit.getLargeur())/2),(yItems+hauteurItem-texteExit.getHauteur())/2));
        f.ajouter(texteExit);
        
        Texte textSudoku=new Texte(Couleur.BLEU,"SUDOKU",new Font("Arial", Font.TYPE1_FONT, 100),new Point());
        textSudoku.setA(new Point(((largeur-textSudoku.getLargeur())/2),(((hauteur-textSudoku.getHauteur())/2)+100)));
        f.ajouter(textSudoku);
    }
    
    //GENERATION DU MENU
    public void generateMenu() {
        f.effacer();
        f.setVisible(true);
        f.setBackground(Color.BLACK);
        clavier = new ClavierBorneArcade();
        f.addKeyListener(clavier);
        f.getP().addKeyListener(clavier);
        
        jouer = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem)/2,yItems),largeurItem,hauteurItem, true);
        exit = new Rectangle(Couleur.BLANC, new Point((largeur-largeurItem)/2,yItems-2*hauteurItem),largeurItem,hauteurItem, true);
        selection = new Rectangle(Couleur.ROUGE, new Point((largeur-largeurItem)/2-margeSelection,yItems-margeSelection),largeurItem+2*margeSelection,hauteurItem+2*margeSelection, true);
        
        f.ajouter(selection);
        this.boutonMenu[0]= jouer;
        this.boutonMenu[1]=exit;
        
        f.ajouter(boutonMenu[0]);
        f.ajouter(boutonMenu[1]);
        
        Texte texteJouer=new Texte(Couleur.NOIR,"PLAY",new Font("Calibri", Font.TYPE1_FONT, 40),new Point());
        f.ajouter(texteJouer);
        texteJouer.setA(new Point(((largeur-texteJouer.getLargeur())/2),yItems+(hauteurItem-texteJouer.getHauteur())/2));
        f.supprimer(texteJouer);
        f.ajouter(texteJouer);
        
        Texte texteExit=new Texte(Couleur.NOIR,"EXIT",new Font("Calibri", Font.TYPE1_FONT, 40),new Point());
        f.ajouter(texteExit);
        texteExit.setA(new Point(((largeur-texteExit.getLargeur())/2),(yItems+hauteurItem-texteExit.getHauteur())/2));
        f.supprimer(texteExit);
        f.ajouter(texteExit);
        
        Texte textSudoku=new Texte(Couleur.BLANC,"SUDOKU",new Font("Calibri", Font.TYPE1_FONT, 200),new Point());
        f.ajouter(textSudoku);
        textSudoku.setA(new Point(((largeur-textSudoku.getLargeur())/2),(((hauteur-textSudoku.getHauteur())/2)+100)));
        f.supprimer(textSudoku);
        f.ajouter(textSudoku);
        
        f.rafraichir();
    }
    
    //MISE A JOUR MENU
    public int majMenu() {
        int status = 0;
        
        if(clavier.getJoyJ1HautTape() && this.pointeur >0) {
            this.pointeur--;
            selection.translater(0, hauteurItem*2);
        }
        
        if(clavier.getJoyJ1BasTape() && this.pointeur < (this.boutonMenu.length-1)) {
            this.pointeur++;
            selection.translater(0, -hauteurItem*2);
        }
        
        if(clavier.getBoutonJ1ATape()){
            if(pointeur==1) {
                System.exit(5);
            }
            else {
                GeneratejeuSudoku();
                this.status = 1;
            }
        }
        
        f.rafraichir();
        return status;
    }
    
    //UN PAS DU JEU
    public void maj(){
        
        // Déplacement sélection haut
        if(clavier.getJoyJ1HautTape()){
            if(posLigneSelection > 0) {
                posLigneSelection--;
            }
        }
        
        // Déplacement sélection bas
        if(clavier.getJoyJ1BasTape()){
            if(posLigneSelection < tailleGrille-1) {
                posLigneSelection++;
            }
        }
        
        // Déplacement sélection gauche
        if(clavier.getJoyJ1GaucheTape()){
            if(posColonneSelection > 0) {
                posColonneSelection--;
            }
        }
        
        // Déplacement sélection droite
        if(clavier.getJoyJ1DroiteTape()){
            if(posColonneSelection < tailleGrille-1) {
                posColonneSelection++;
            }
        }
        
        // Saisie compatible borne :
        // J1A (F) = valeur suivante, J1B (G) = valeur précédente, J1C (H) = effacer.
        if(grilleComplete[posLigneSelection][posColonneSelection] == 0) {
            if(clavier.getBoutonJ1ATape()) {
                int valeurActuelle = grille[posLigneSelection][posColonneSelection];
                grille[posLigneSelection][posColonneSelection] = (valeurActuelle % 9) + 1;
                updateAffichageGrille();
            }

            if(clavier.getBoutonJ1BTape()) {
                int valeurActuelle = grille[posLigneSelection][posColonneSelection];
                grille[posLigneSelection][posColonneSelection] = (valeurActuelle == 0) ? 9 : valeurActuelle - 1;
                updateAffichageGrille();
            }

            if(clavier.getBoutonJ1CTape()) {
                grille[posLigneSelection][posColonneSelection] = 0;
                updateAffichageGrille();
            }
        }
        
        // Quitter
        if(clavier.getBoutonJ1ZTape()){
            System.exit(5);
        }
        
        // Vérifier si gagné
        if(isGrilleComplete()) {
            Texte t=new Texte(Couleur.VERT,"SUDOKU COMPLETE !!!",new Font("Calibri", Font.TYPE1_FONT, 60),new Point(largeur/2,hauteur/2));
            f.ajouter(t);
            f.rafraichir();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            generateMenu();
            this.status=0;
        }
        
        updateSelectionGraphique();
        f.rafraichir();
    }

    // Grille 

    
    //INITIALISATION DU JEU
    public void GeneratejeuSudoku() {
        f.effacer();
        clavier = new ClavierBorneArcade();
        f.addKeyListener(clavier);
        f.getP().addKeyListener(clavier);
        
        // Fond noir
        f.ajouter(new Rectangle(Couleur.NOIR, new Point(0,0), new Point(largeur, hauteur), true));
        
        // Initialiser les grilles
        initGrille();
        
        // Créer les textes de la grille
        textesGrille = new Texte[tailleGrille][tailleGrille];
        
        // Afficher la grille
        afficherGrille();
        
        // Position initiale de la sélection
        posLigneSelection = 0;
        posColonneSelection = 0;
        
        // Musique
        m=new Musique("Tied_Up.mp3");
        m.lecture();
    }
    
    //INITIALISER LA GRILLE SUDOKU
    private void initGrille() {
        // Grille de départ (0 = case vide)
        grille = new int[][] {
            {5,3,0,0,7,0,0,0,0},
            {6,0,0,1,9,5,0,0,0},
            {0,9,8,0,0,0,0,6,0},
            {8,0,0,0,6,0,0,0,3},
            {4,0,0,8,0,3,0,0,1},
            {7,0,0,0,2,0,0,0,6},
            {0,6,0,0,0,0,2,8,0},
            {0,0,0,4,1,9,0,0,5},
            {0,0,0,0,8,0,0,7,9}
        };
        
        // Copier pour la grille complète (cases données)
        grilleComplete = new int[tailleGrille][tailleGrille];
        for(int i = 0; i < tailleGrille; i++) {
            for(int j = 0; j < tailleGrille; j++) {
                grilleComplete[i][j] = grille[i][j];
            }
        }
    }
    
    //AFFICHER LA GRILLE
    private void afficherGrille() {
        int startX = 100;
        int startY = 100;
        
        for(int i = 0; i < tailleGrille; i++) {
            for(int j = 0; j < tailleGrille; j++) {
                // Fond de la case
                Rectangle case_rect = new Rectangle(Couleur.BLANC, 
                    new Point(startX + j*tailleCase, startY + i*tailleCase),
                    new Point(startX + (j+1)*tailleCase, startY + (i+1)*tailleCase),
                    true);
                f.ajouter(case_rect);
                
                // Bordure
                Rectangle bordure = new Rectangle(Couleur.NOIR,
                    new Point(startX + j*tailleCase, startY + i*tailleCase),
                    new Point(startX + (j+1)*tailleCase, startY + (i+1)*tailleCase),
                    false);
                f.ajouter(bordure);
                
                // Texte du nombre
                String texte = (grille[i][j] == 0) ? "" : String.valueOf(grille[i][j]);
                Couleur couleur = (grilleComplete[i][j] == 0) ? Couleur.BLEU : Couleur.GRIS;
                
                textesGrille[i][j] = new Texte(couleur, texte, 
                    new Font("Calibri", Font.TYPE1_FONT, 40),
                    new Point(startX + j*tailleCase + tailleCase/2, 
                              startY + i*tailleCase + tailleCase/2));
                f.ajouter(textesGrille[i][j]);
            }
        }
        
        // Titre
        Texte titre = new Texte(Couleur.BLANC, "SUDOKU - J1A:+1  J1B:-1  J1C:effacer  J1Z:quitter",
            new Font("Calibri", Font.TYPE1_FONT, 20),
            new Point(100, 50));
        f.ajouter(titre);
    }
    
    //METTRE A JOUR L'AFFICHAGE DE LA GRILLE
    private void updateAffichageGrille() {
        for(int i = 0; i < tailleGrille; i++) {
            for(int j = 0; j < tailleGrille; j++) {
                String texte = (grille[i][j] == 0) ? "" : String.valueOf(grille[i][j]);
                textesGrille[i][j].setTexte(texte);
            }
        }
    }
    
    //METTRE A JOUR LA POSITION DE LA SELECTION GRAPHIQUE
    private void updateSelectionGraphique() {
        int startX = 300;
        int startY = 200;
        
        // Effacer la sélection précédente
        f.effacer();
        
        // Redessiner tout
        f.ajouter(new Rectangle(Couleur.NOIR, new Point(100,100), new Point(largeur, hauteur), true));
        
        for(int i = 0; i < tailleGrille; i++) {
            for(int j = 0; j < tailleGrille; j++) {
                // Fond de la case
                Couleur couleurFond = (i == posLigneSelection && j == posColonneSelection) ? 
                    Couleur.ROSE : Couleur.BLANC;
                
                Rectangle case_rect = new Rectangle(couleurFond, 
                    new Point(startX + j*tailleCase, startY + i*tailleCase),
                    new Point(startX + (j+1)*tailleCase, startY + (i+1)*tailleCase),
                    true);
                f.ajouter(case_rect);
                
                // Bordure
                Rectangle bordure = new Rectangle(Couleur.ROSE,
                    new Point(startX + j*tailleCase, startY + i*tailleCase),
                    new Point(startX + (j+1)*tailleCase, startY + (i+1)*tailleCase),
                    false);
                f.ajouter(bordure);
                
                // Texte du nombre
                String texte = (grille[i][j] == 0) ? "" : String.valueOf(grille[i][j]);
                Couleur couleur = (grilleComplete[i][j] == 0) ? Couleur.BLEU : Couleur.NOIR;
                
                textesGrille[i][j] = new Texte(couleur, texte, 
                    new Font("Calibri", Font.TYPE1_FONT, 40),
                    new Point(startX + j*tailleCase + tailleCase/2, 
                              startY + i*tailleCase + tailleCase/2));
                f.ajouter(textesGrille[i][j]);
            }
        }
        
        // Titre
        Texte titre = new Texte(Couleur.BLANC, "SUDOKU - J1A:+1  J1B:-1  J1C:effacer  Y :quitter",
            new Font("Calibri", Font.TYPE1_FONT, 20),
            new Point(600, 150));
        f.ajouter(titre);
    }
    
    //VERIFIER SI LA GRILLE EST COMPLETE ET CORRECTE
    private boolean isGrilleComplete() {
        for(int i = 0; i < tailleGrille; i++) {
            for(int j = 0; j < tailleGrille; j++) {
                if(grille[i][j] == 0) return false;
            }
        }
        return true;
    }
    
    public int getStatus() {
        return this.status;
    }
}
