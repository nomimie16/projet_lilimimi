public class Grille {
    private int[][] grille;
    private int[][] solution;

    public Grille() {
        grille = new int[9][9];
        solution = new int[9][9];
        genererSudoku();
    }

    public int[][] getGrille() {
        int[][] copie = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(grille[i], 0, copie[i], 0, 9);
        return copie;
    }

    public int[][] getSolution() {
        int[][] copie = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(solution[i], 0, copie[i], 0, 9);
        return copie;
    }

    private boolean chiffreEstDansLigne(int chiffre, int ligne) {
        for (int i = 0; i < 9; i++)
            if (grille[ligne][i] == chiffre)
                return true;
        return false;
    }

    private boolean chiffreEstDansColonne(int chiffre, int colonne) {
        for (int i = 0; i < 9; i++)
            if (grille[i][colonne] == chiffre)
                return true;
        return false;
    }

    private boolean chiffreEstDansCarre(int chiffre, int ligne, int colonne) {
        int startL = ligne - ligne % 3;
        int startC = colonne - colonne % 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (grille[startL + i][startC + j] == chiffre)
                    return true;
        return false;
    }

    public boolean estPossibleDePlacer(int chiffre, int ligne, int colonne) {
        return !chiffreEstDansLigne(chiffre, ligne)
                && !chiffreEstDansColonne(chiffre, colonne)
                && !chiffreEstDansCarre(chiffre, ligne, colonne);
    }

    private int getAleatoire() {
        return (int) (Math.random() * 9 + 1);
    }

    private void remplirCarre(int ligne, int colonne) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                int chiffre;
                do {
                    chiffre = getAleatoire();
                } while (chiffreEstDansCarre(chiffre, ligne, colonne));
                grille[ligne + i][colonne + j] = chiffre;
            }
    }

    private void remplirDiagonale() {
        for (int i = 0; i < 9; i += 3)
            remplirCarre(i, i);
    }

    private boolean remplirGrille(int ligne, int colonne) {
        if (ligne == 9)
            return true;
        if (colonne == 9)
            return remplirGrille(ligne + 1, 0);

        if (grille[ligne][colonne] != 0)
            return remplirGrille(ligne, colonne + 1);

        for (int chiffre = 1; chiffre <= 9; chiffre++) {
            if (estPossibleDePlacer(chiffre, ligne, colonne)) {
                grille[ligne][colonne] = chiffre;
                if (remplirGrille(ligne, colonne + 1))
                    return true;
                grille[ligne][colonne] = 0;
            }
        }
        return false;
    }

    private void supprimerChiffres(int n) {
        while (n > 0) {
            int ligne = (int) (Math.random() * 9);
            int colonne = (int) (Math.random() * 9);
            if (grille[ligne][colonne] != 0) {
                grille[ligne][colonne] = 0;
                n--;
            }
        }
    }

    public void genererSudoku() {
        remplirDiagonale();
        remplirGrille(0, 0);

        // Copier solution
        for (int i = 0; i < 9; i++)
            System.arraycopy(grille[i], 0, solution[i], 0, 9);

        supprimerChiffres(10);
    }
}