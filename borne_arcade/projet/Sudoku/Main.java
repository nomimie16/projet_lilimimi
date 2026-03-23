

public class Main {
    


    public static void main ( String [] args ){
	//à régler en fonction de l'ordinateur utilisé
	int vitesse = 10;
	int status = 0;
	
	
	Sudoku p = new Sudoku();
	
	
	while (true) {
    try {
        Thread.sleep(vitesse);
    } catch (Exception e) {
        System.out.println(e);
    }

    if (status == 0) {
        p.majMenu();
        status = p.getStatus();
    } else {
        p.maj();
        status = p.getStatus();
    }
}
	
   }}//main




//class Main
