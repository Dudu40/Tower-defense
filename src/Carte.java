import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Carte extends Attaquant {
	int prix,portee;
	int longueur,largeur,recharge;
	private int compteur=0;
	Color color=null;
	String nom;
	private boolean etatPret=true;
	
	public Carte(int Id, double X, double Y, int HP, int Attaque, int VitesseAttaque, int Prix, int Portee,int recharge,String nom) {
		super(Id,X,Y,HP,Attaque,VitesseAttaque);
		this.nom=nom;
		this.prix=Prix;
		this.portee=Portee;
		this.recharge=recharge;
		longueur=Jeu.TAILLE_BLOC+Jeu.TAILLE_BLOC/4;
		largeur=Jeu.TAILLE_BLOC+Jeu.TAILLE_BLOC/4;
		
	}
	

	/** 
	 * Permet de lancer le temps de rechargement de la carte
	 * @author Lagahe Hugo
	 */
	
		public void lanceRecharge() {
			Timer time = new Timer();
	        time.schedule(new TimerTask() {
	            @Override
	            public void run() {
	                 	compteur++;
	                    if (compteur==recharge) {
	                    	compteur=0;
	                    	etatPret=true;
	                    	cancel();
	                    }
	            }
	        },1000,1000);
		}
		
	/** 
	* Permet de placer un allié a la position de la carte sur le plateau de jeu
	* @author Lagahe Hugo
	*/
	public void placerAllie(int xCase,int yCase) {
		Jeu.listeAllie.add(new Allié(getId(),xCase*Jeu.TAILLE_BLOC,yCase*Jeu.TAILLE_BLOC,getHp(),getAttaque(),getVitesseAttaque(),getPrix(),getPortee()));
		Jeu.plateau[yCase][xCase].setPosAllie(Jeu.listeAllie.size()-1);
		etatPret=false;
		compteur=1;
		lanceRecharge();
	}
	
	/** 
	* Permet de placer un allié a la position de la carte sur le plateau de jeu
	* @author Lagahe Hugo
	*@return retourne vrai si la carte est posable et non sinon
	*/ 
	
	public boolean estPosable(int xCase,int yCase) {
		boolean res=false;
		// si on se site dans la zone
		if ((xCase<Jeu.longueur) && (xCase>=0) && (yCase<Jeu.largeur) && (yCase>=0)) {
					// si la case du plateau est disponible
					if((Jeu.plateau[yCase][xCase].isMur()==false) && (Jeu.plateau[yCase][xCase].getPosAllie()==-1)){
						res=true;
					}
			}
		return res;
	}
	
	/**
	 * Affiche les differents types d'allié
	 * @author Lagahe Hugo,Cuyala Tristan
	 * @param gc
	 */
	public void affichage(GraphicsContext gc) {
		//affichage du chargement des alliés		
		gc.setStroke(getColor());
		if(color!=null) {
			gc.setLineWidth(8);
		}
		else {
			gc.setLineWidth(1);
			gc.setStroke(Color.BLACK);
		}
		//On donne la position
		gc.strokeRect(getX(),getY(),longueur,largeur);
		//Couleur lorsque l'allié est remplie
		gc.setFill(Color.LIGHTGOLDENRODYELLOW);
		if (compteur==0) {
			//Si compteur à 0 alors la case est remplie
			gc.fillRect(getX(),getY(),longueur,largeur);
		}
		else {
			//Sinon elle se remplie au fur et à mesure
			gc.fillRect(getX(),getY()+largeur-(compteur*largeur)/recharge,longueur,(compteur*largeur)/recharge);
		}
		if (getId()==1) {
			//le canon de base
			try {
				//On charge l'image
				Image imageB = new Image(new FileInputStream("src/other/canon1.png"));
				//On l'affiche
				gc.drawImage(imageB, getX(),getY(),(longueur),(largeur));
			} catch (FileNotFoundException e) {
				//On affiche un message d'erreur si l'image n'a pas été trouvée
				System.out.println("FileNotFoundException"+ e.getMessage());
			}
		}
		else if (getId()==2) {
			//le canon v2
			try {
				//On charge l'image
				Image imageB = new Image(new FileInputStream("src/other/canon2.png"));
				//On l'affiche
				gc.drawImage(imageB, this.getX(),this.getY(),(longueur),(largeur));
			} catch (FileNotFoundException e) {
				//On affiche un message d'erreur si l'image n'a pas été trouvée
				System.out.println("FileNotFoundException"+ e.getMessage());
			}
			
			
		}
		else if (getId()==3) {
			//La bombe
			try {
				//On charge l'image
				Image imageB = new Image(new FileInputStream("src/other/bomb.png"));
				//On l'affiche
				gc.drawImage(imageB, this.getX(),this.getY(),longueur,largeur);
			} catch (FileNotFoundException e) {
				//On affiche un message d'erreur si l'image n'a pas été trouvée
				System.out.println("FileNotFoundException"+ e.getMessage());
			}
		
		}
		else if (getId()==4) {
			//La muraille
			try {
				//On charge l'image
				Image imageB = new Image(new FileInputStream("src/dungeon2/topdown_shooter/other/base.png"));
				//On l'affiche
				gc.drawImage(imageB, this.getX(),this.getY(),(longueur),(largeur));
			} catch (FileNotFoundException e) {
				//On affiche un message d'erreur si l'image n'a pas été trouvée
				System.out.println("FileNotFoundException"+ e.getMessage());
			}
		
		}
		else if (getId()==5){
			//La money
			try {
				//On charge l'image
				Image imageB = new Image(new FileInputStream("src/other/dollard.png"));
				//On l'affiche
				gc.drawImage(imageB, this.getX(),this.getY(),(longueur),(largeur));
			} catch (FileNotFoundException e) {
				//On affiche un message d'erreur si l'image n'a pas été trouvée
				System.out.println("FileNotFoundException"+ e.getMessage());
			}
		
		}
		else {
			//Le piège
			try {
				//On charge l'image
				Image imageB = new Image(new FileInputStream("src/other/pique.png"));
				//On l'affiche
				gc.drawImage(imageB, this.getX(),this.getY()+largeur/8,longueur,largeur-largeur/4);
			} catch (FileNotFoundException e) {
				//On affiche un message d'erreur si l'image n'a pas été trouvée
				System.out.println("FileNotFoundException"+ e.getMessage());
			}
		}
	}
	
	//Getter et setter
	public void setColor(Color c) {
		color=c;
	}
	
	public boolean estEtatPret() {
		return etatPret;
	}
	
	public void setEtatPret(boolean val) {
		etatPret=val;
	}
	
	public Rectangle getHitBox() {
		return new Rectangle(getX(),getY(),longueur,largeur);
	}
	
	public Color getColor () {
		return color;
	}
	
	public String getNom() {
		return nom;
	}
	
	public int getPortee() {
		return portee;

	
	
	
	
	}

	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	
	public void setLongueur(int l){
		longueur = l;
	}
	public int getLongueur() {
		return longueur;
	}
	public Case getCase() {	
	
	Case caseCarte= new Case((int)getX()/Jeu.TAILLE_BLOC,(int)getY()/Jeu.TAILLE_BLOC);
	
	return caseCarte;
	
	}

}
