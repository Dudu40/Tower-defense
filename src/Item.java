import javafx.scene.canvas.GraphicsContext;

import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;  

public class Item {
	//Déclaration variables
	private int id;
	private int value;
	private int x;
	private int y;
	private int longueur=Jeu.TAILLE_BLOC/2+Jeu.TAILLE_BLOC/4;
	private int largeur=Jeu.TAILLE_BLOC/2+Jeu.TAILLE_BLOC/4;
	private Color color =null;
	
	/**
	 * Constructeur Item(int id, int value, int x, int y)
	 * @param id
	 * @param value
	 * @param x
	 * @param y
	 */
	public Item(int id, int value, int x, int y) {
		this.id = id;
		this.value = value;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Permet d'afficher un item 
	 * @author Lagahe Hugo
	 * Méthode afficherI(GraphicsContext gc)
	 * @param gc
	 */
	public void afficherI(GraphicsContext gc) {		
		//Si l'id de l'item est égal à 1
		if (this.getId() ==1) {
			try {
				//Alors on affiche une pièce d'or
				Image imageB = new Image(new FileInputStream("src/other/piece.png"));
				gc.drawImage(imageB, this.getX()+longueur/6,this.getY()+longueur/6,(longueur),(largeur));
			} catch (FileNotFoundException e) {
				//Si l'image n'a pas été trouvée
				System.out.println("FILE NOT FOUND");;
			}
		}
		
	}

	/**
	 * Méthode Rectangle getHitBox()
	 * @return la hitbox de l'item
	 */
	public Rectangle getHitBox() {
		return new Rectangle(x,y,longueur,largeur);
	}
	
	//Getter et SETTER
	public Case getCase() {	
		return new Case((int)getX()/Jeu.TAILLE_BLOC,(int)getY()/Jeu.TAILLE_BLOC);
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color y) {
		color=y;
	}
}
