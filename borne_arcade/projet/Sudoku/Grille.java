
public class Grille {
    private int[][] Grille;
    private int[][] Solution;

    public Grille(){
        Grille = new int[9][9];
        Solution = new int[9][9];
        this.genererSudoku();
    }
    public int[][] getGrille(){
        return this.Grille.clone();
    }
    public int[][] getSolution(){
        return this.Solution.clone();
    }
    public int getCase(int chiffre, int ligne, int colonne){
        return Grille[ligne][colonne];
    }
    public void setCase(int chiffre, int ligne, int colonne){
        this.Grille[ligne][colonne] = chiffre;
    }
    private boolean chiffreEstDansLigne(int chiffre, int ligne){
        boolean reponse = false;
        for(int i =0; i<9; i++){
            if(Grille[ligne][i] == chiffre){
                reponse = true;
                break;
            }
        }
        return reponse;
    }
    private boolean chiffreEstDansColonne(int chiffre, int colonne){
        boolean reponse = false;
        for(int i =0; i<9; i++){
            if(Grille[colonne][i] == chiffre){
                reponse = true;
                break;
            }
        }
        return reponse;
    }
    private boolean chiffreEstDansCarre(int chiffre, int ligne, int colonne){
        boolean reponse = false;
        for(int i =0; i<3; i++){
            for(int j=0; j<3; j++){
                if(Grille[ligne+i][colonne+i] == chiffre){
                    reponse = true;
                    break;
                }
            }
        }
        return reponse;
    }
    public boolean estPossibleDePlacer(int chiffre, int ligne, int colonne){
        boolean reponse = true;
        if(chiffreEstDansLigne(chiffre, ligne) || chiffreEstDansColonne(chiffre, colonne) || chiffreEstDansCarre(chiffre, ligne-ligne%3, colonne-colonne%3)){
            reponse=false;
        }
        return reponse;
    }
    private int getAleatoire(){
        return (int) Math.floor(Math.random()*9+1);
    }
    private int supprimerChiffre(){
        int n = 50;
        boolean suppressionencours = true;
        while(suppressionencours){
            int ligne = getAleatoire()-1;
            int colonne = getAleatoire()-1;
            if(Grille[ligne][colonne]!=0){
                Grille[ligne][colonne]=0;
                n--;
                if(n<=0){
                    suppressionencours = false;
                }
            }
        }
        return n;
    }
    private void remplirCarre(int ligne, int colonne) {
		int chiffre = getAleatoire();
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				while(chiffreEstDansCarre(chiffre, ligne, colonne)) {
					chiffre = getAleatoire();
				}
				Grille[ligne+i][colonne+j] = chiffre;
			}
		}
	}
	private void remplirDiagonale() {
		for (int i=0; i<9; i+=3) {
			remplirCarre(i,i);
		}
	}
	private boolean remplirGrille(int ligne, int colonne) {
		if (colonne >=9 && ligne<8) {
			colonne=0; ligne+=1;
		}
		if (colonne>=9 && ligne >=9) {
			return true;
		}
		if (ligne<3) {
			if (colonne<3) {
				colonne =3;
			}
		}
		else if (ligne<6) {
			if (colonne >=3 && colonne<6) {
				colonne = 6;
			}
		}
		else if (colonne >=6) {
			ligne+=1; colonne=0;
			if (ligne >=9) {
				return true;
			}
		}
		for (int chiffre=1; chiffre<=9; chiffre++) {
			if (estPossibleDePlacer(chiffre, ligne, colonne)) {
				Grille[ligne][colonne]=chiffre;
				if (remplirGrille(ligne, colonne+1)) {
					return true;
				}
				Grille[ligne][colonne]=0;
			}
		}
		return false;
	}
	public void genererSudoku() {
		remplirDiagonale();
		remplirGrille(0,3);
		for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.Solution[i][j] = Grille[i][j];
            }
        }
		supprimerChiffre();
	}
}
