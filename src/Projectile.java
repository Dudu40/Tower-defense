import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Projectile {
	
	double x,y;
	int vitesse,puissance;
	int rayon=Jeu.TAILLE_BLOC/4;
	
	/**
	 * Constructeur 
	 * Méthode Projectile (double x,double y,int puissance)
	 * @param x
	 * @param y
	 * @param puissance
	 */
	public Projectile (double x,double y,int puissance) {
		this.x=x+rayon+rayon/2;
		this.y=y+rayon+rayon/2;
		this.vitesse=4;
		this.puissance=puissance;
	}
	

	/**
	 * Affiche le projectile envoyé par l'allié
	 * @author Lagahe Hugo
	 * @param gc
	 */
	public void affichage(GraphicsContext gc) {
		//Projectile pour les deux canons
		try {
			//On charge l'image
			Image imageB = new Image(new FileInputStream("src/dungeon2/topdown_shooter/other/bulletb.png"));
			//On l'affiche
			gc.drawImage(imageB, x,(y-17),rayon,rayon);
		} catch (FileNotFoundException e) {
			//On affiche un message d'erreur si l'image n'a pas été trouvée
			System.out.println("FileNotFoundException"+ e.getMessage());
		}
	}
	
	/**
	 * Affiche le projectile envoyé par l'allié
	 * @author Lagahe Hugo
	 * @return retourne la case qui correspond aux coordonnées du projectile
	 */
	
	public Case getCase() {	
		
		Case caseProjectile= new Case((int)x/Jeu.TAILLE_BLOC,(int)y/Jeu.TAILLE_BLOC);
		
		return caseProjectile;
		
	}
	
	/**
	 * retourne la position du premier ennemi touché
	 * @author Ducasse Quentin
	 * @return retourne la position de l'ennemi touché dans la liste d'ennemi
	 */
	public int posEnnemiPremier(Case casee) {
		int i=0;
		boolean verif=false;
		while(  (!verif) && (i<Jeu.listeEnnemi.size())) {
			if ((casee.getX()==Jeu.listeEnnemi.get(i).getCase().getX()) &&  (casee.getY()==Jeu.listeEnnemi.get(i).getCase().getY())) {
				verif=true;
			}
			else {
				i++;
			}
		
			
		}
		return i;
	}
	
	/**
	 * retourne si un ennemi a été touché par le projectile
	 * @author Ducasse Quentin
	 * @return retourne vrai si un ennemi est touché et faux sinon
	 */
	public boolean rencontreEnnemi() {
		boolean res=false;
		int hpActuel;
		for(int i=0;i<Jeu.listeEnnemi.size();i++) {
			if (this.getHitBox().intersects(Jeu.listeEnnemi.get(i).getHitBox())){
				int pos=posEnnemiPremier(Jeu.listeEnnemi.get(i).getCase());
				
				if (pos <Jeu.listeEnnemi.size()) {
					res=true;
					hpActuel=Jeu.listeEnnemi.get(pos).getHpActuel()-puissance;
					
					if(hpActuel>0) {
						
						Jeu.listeEnnemi.get(pos).setHpActuel(hpActuel);
					}
					else {
						Jeu.listeEnnemi.get(pos).mourir();
					}
				}
				
			}
		}
		return res;
	}
	/**
	 * retourne si un projectile touche un mur
	 * @author Ducasse Quentin
	 * @return retourne vrai si le projectile touche un mur et non sinon
	 */
	public boolean rencontreObstacle() {
		boolean res=false;
		
		// on recupere la case de l'ennemi
		Case caseProjectile=getCase();
		double yCase=caseProjectile.getY();
		double xCase=caseProjectile.getX();
		
		// si la case du plateau correspond a la case de l'ennemi
			if ((xCase<Jeu.longueur) && (xCase>=0) && (yCase<Jeu.largeur) && (yCase>=0)) {
				// si la case du plateau est un obstacle
				if(Jeu.plateau[(int)yCase][(int)xCase].isMur()) {
					
					res=true;
				}
		}
		return res;
		
	}
	
	/**
	 * verifie que le projectile ne sors pas de l'ecran
	 * @author Ducasse Quentin
	 * @return retourne vrai si le projectile sors de la map et faux sinon
	 */
	
	public boolean sorsDeLaMap() {
		boolean res=false;
		if((x>Jeu.longueur*Jeu.TAILLE_BLOC) || (x<0)) {
			res=true;
		}
		return res;
	}
	
	/**
	 * retourne modifie la position du projectile chaque milliseconde
	 * @author Ducasse Quentin
	 */
	public void update() {
		x+=vitesse;
	}
	
	// getter et setter
	Rectangle getHitBox() {
		return new Rectangle((int)x,(int)y,rayon,rayon);
	}


	public int getPuissance() {
		return puissance;
	}

	public void setPuissance(int puissance) {
		this.puissance = puissance;
	}
	
	
	

}
