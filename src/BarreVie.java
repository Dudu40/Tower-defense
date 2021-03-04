import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BarreVie {
	double x;
	double y;
	int etat;
	// longueur et largeur totale de la barre de vie
	int longueurBarre;
	int largeurBarre;
	int id;
	
	// longueur de la barre de vie 
	int longueurVie;
	
	public BarreVie(double x,double y,int longueur,int largeur,int id) {
		this.x=x;
		this.y=y;
		this.id=id;
		this.longueurBarre=longueur;
		this.largeurBarre=largeur;
		this.longueurVie=longueurBarre;
	}
	
	/**
	 * affiche la barre de vie
	 * @author Ducasse Quentin
	 */
	public void affichage(GraphicsContext gc) {
		if (id==1) {
			gc.setFill(Color.BLUE);
		}
		else {
			gc.setFill(Color.BLUEVIOLET);
		}		
		gc.fillRect(x, y, longueurVie, largeurBarre);
		gc.setFill(Color.BLACK);
		gc.fillRect(x+longueurVie,y,longueurBarre-longueurVie,largeurBarre);
	
	}
	
	/**
	 * affiche le level 
	 * @author Ducasse Quentin
	 */
	public void affichageLevel(GraphicsContext gc,int level) {
		
		Font temp = gc.getFont();
		gc.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
		gc.setFill(Color.WHITE);
		if (id==1) {
		
			gc.fillText("level "+Integer.toString(level),x+longueurBarre/4,y+2*largeurBarre);
		}
		else {
				
			gc.fillText("level "+Integer.toString(level),x,y-largeurBarre);
		}
		gc.setFont(temp);
	}
	
	// getter et setter
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getValeur() {
		return etat;
	}

	public void setValeur(int etat) {
		this.etat = etat;
	}

	public int getLongueurBarre() {
		return longueurBarre;
	}

	public void setLongueur(int width) {
		this.longueurBarre = width;
	}

	public int getLargeurBarre() {
		return largeurBarre;
	}
	
	public void setLongueurVie(int val) {
		this.longueurVie=val;
	}
	
	public int getLongueurVie() {
		return longueurVie;
	}
	

	public void setLargeurBarre(int height) {
		this.largeurBarre = height;
	}

}