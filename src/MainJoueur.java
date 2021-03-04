
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainJoueur  {
	int x,y,longueurMain,largeurMain,longueurTotale,largeurTotale;
	int espaceX;
	int espaceY;
	int nbCartes=6;
	
	private ArrayList<Carte>listeCarte;
	
	/**
	 * Constructeur 
	 * @param x
	 * @param y
	 * @param longueurTotale
	 * @param largeurTotale
	 */
	public MainJoueur(int x,int y,int longueurTotale,int largeurTotale) {
		this.x=x;
		this.y=y;
		this.longueurTotale=longueurTotale;
		this.largeurTotale=largeurTotale;
		this.espaceX=Jeu.TAILLE_BLOC/2;
		this.espaceY=Jeu.TAILLE_BLOC/2;
		this.longueurMain=nbCartes*Jeu.TAILLE_BLOC+(nbCartes+1)*espaceX;
		this.largeurMain=Jeu.TAILLE_BLOC+Jeu.TAILLE_BLOC/4+2*espaceY;
		listeCarte=initListeCarte();
	
		
	}
	
	/**
	 * Initialise la liste de carte de la main du Joueur
	 * @author Lagahe Hugo,Cuyala Tristan
	 * @return ArrayList<Carte>
	 */
	 public  ArrayList<Carte> initListeCarte() {
		 //On déclare la liste
		 ArrayList<Carte> liste = new  ArrayList<Carte>();
		 //On ajoute tous les alliés dedans
		 // un canon normal
		 liste.add(new Carte(1,x+espaceX,y+espaceY,100,10,1,40,0,5,"Canon 1"));						
		 //Un canon amélioré
		 liste.add(new Carte(2,x+espaceX+espaceX+Jeu.TAILLE_BLOC,y+espaceY,50,10,2,80,0,5,"Canon 2"));
		 //Un piège
		 liste.add(new Carte(6,x+espaceX+2*espaceX+2*Jeu.TAILLE_BLOC,y+espaceY,0,0,0,30,0,20,"Piege"));
		 // une bombe
		 liste.add(new Carte(3,x+espaceX+3*espaceX+3*Jeu.TAILLE_BLOC,y+espaceY,0,0,0,110,0,35,"Bombe"));
		 //Une mine
		 liste.add(new Carte(5,x+espaceX+4*espaceX+4*Jeu.TAILLE_BLOC,y+espaceY,50,10,0,20,0,5,"Mine"));
		 //Une muraille
		 liste.add(new Carte(4,x+espaceX+5*espaceX+5*Jeu.TAILLE_BLOC,y+espaceY,500,0,0,30,0,20,"Muraille"));
		 //On retourne la liste complète
		 return liste;

		}
	
	/**
	 * Afficher le texte dans la main du joueur
	 * @author Lagahe Hugo
	 * @param i
	 * @param gc
	 * @param carte
	 */
	public void afficherTexte(int i,GraphicsContext gc,Carte carte) {
		//On met la police du jeu
		Font font=null;
		try {
			//On charge la police avec une taille de 28
			font = Font.loadFont(new FileInputStream(new File("police/dungeon.TTF")),28);
		} catch (FileNotFoundException e) {
			//Exception si la police n'a pas été trouvée
			e.printStackTrace();
		}
		//On met la police
		gc.setFont(font);
		//On aligne le texte
		gc.getTextAlign();
		//On met le texte de couleur beige 
		gc.setFill(Color.BEIGE);	
	 	//On affiche le nom des alliés
		gc.fillText(carte.getNom(),x+i*Jeu.TAILLE_BLOC+i*espaceX+espaceX+carte.getLongueur()/6,y+espaceY-carte.getLongueur()/32);
		//On affiche le coût des alliés
		try {
			//On charge la police avec une taille de 50
			font = Font.loadFont(new FileInputStream(new File("police/dungeon.TTF")),50);
		} catch (FileNotFoundException e) {
			//Exception si la police n'a pas été trouvée
			e.printStackTrace();
		}
		//On met la police		
		gc.setFont(font);
		//On met le texte de couleur doré 
		gc.setFill(Color.GOLD);	
	 	//On affiche le prix de la main
		gc.fillText(Integer.toString(carte.getPrix()),x+i*Jeu.TAILLE_BLOC+i*espaceX+espaceX+carte.getLongueur()/4,y+espaceY+carte.getLongueur()+carte.getLongueur()/2);
	}
	
	/**
	 * Affichage de chaque carte de la main
	 * @author Lagahe Hugo
	 * @param gc
	 */
	public void affichageListeCarte(GraphicsContext gc) {
		for(int i=0;i<listeCarte.size();i++) {
			listeCarte.get(i).affichage(gc);
			afficherTexte(i,gc,listeCarte.get(i));
			
		}
		
	}
	
	/**
	 * Affichage de la main entiere avec les cartes a l'interieur
	 * @author Lagahe Hugo
	 * @param gc
	 */
	public void affichage(GraphicsContext gc) {
		//Affiche sur les côtés extérieurs de la main
		gc.setFill(Color.DIMGREY);
		gc.fillRect(0,Jeu.largeur*Jeu.TAILLE_BLOC,Jeu.longueur*Jeu.TAILLE_BLOC,4*Jeu.TAILLE_BLOC);
		//On affiche autour de la main
		gc.setFill(Color.DARKGREY);
		gc.fillRect(x,y,longueurMain,largeurMain);
		//On affiche la liste de carte
		affichageListeCarte(gc);
	}
	
	//Getter et setter
	ArrayList<Carte> getListeCarte(){
		return listeCarte;
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

	public int getEspaceX() {
		return espaceX;
	}

	public void setEspaceX(int espaceX) {
		this.espaceX = espaceX;
	}

	public int getEspaceY() {
		return espaceY;
	}

	public void setEspaceY(int espaceY) {
		this.espaceY = espaceY;
	}
}
