

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;


public class Ennemi extends Attaquant {
	//
	private int longueur=Jeu.TAILLE_BLOC/2;
	private int largeur=Jeu.TAILLE_BLOC/2;
	private double vitesse;
	private int hpActuel;//a mettre das attaquant
	private ArrayList<Case> chemin;
	private Case caseEnnemi;
	private boolean etatAttaque=false;
	private boolean estpremier=false;
	private int timerAttaque=30;
	private int timerFrame=0;
	private int frequence=4;
	private int timer=timerAttaque;
//	System.out.println("correspondance plateau:"+Jeu.plateau[0][14]);
	private double dx,dy;
	private int direction;
	int iterateur;
	private int niveau;


	private boolean mort=false;
	private BarreVie barreVie;

	private int destination;//pour que les ennemis se repartisse sur des colonnes equivalentes a la fin

	
	public Ennemi(int Id, double X,double Y, int HP, int Attaque, int VitesseAttaque, int typeButin, double Vitesse,int niveau) {
		super(Id,X,Y,HP,Attaque,VitesseAttaque);
		this.vitesse = Vitesse;
		this.dx=-vitesse;
		this.dy=0;
		this.direction=1;
		this.hpActuel=getHp();
		this.destination=(int)(Math.random()*Jeu.largeur);	
		barreVie= new BarreVie(this.getX()+longueur/2,this.getY()+largeur/6,longueur,largeur/6,2);	
		this.chemin=this.chercheChemin();
		this.iterateur = 0;
		this.niveau=niveau;
	}
	

	
	/**
	 * regarde si un ennemi est present sur la meme case
	 * @author Ducasse Quentin
	 * @return faux si il y e aucun ennemi, vrai si il y en a un
	 */
	public Case getCase() {	
		
		Case caseEnnemi= new Case((int)getX()/Jeu.TAILLE_BLOC,(int)getY()/Jeu.TAILLE_BLOC);
		
		return caseEnnemi;
		
	}
	
	
	/**
	 * regarde si un ennemi rencontre un obstacle
	 * @author Ducasse Quentin
	 * @return faux si l'ennemi n'est pas en contact avec un mur vrai sinon
	 */
	
	public boolean rencontreObstacle(int xCase,int yCase) {
		boolean res=false;
		
		// on recupere la case de l'ennemi
		
		// si la case du plateau correspond a la case de l'ennemi
			if ((xCase<Jeu.longueur) && (yCase>=1)) {
				// si la case du plateau est un obstacle
				if(Jeu.plateau[yCase][xCase].isMur()) {
					res=true;
				}
				
		}
		return res;
		
	}
	
	/**
	 * regarde si un ennemi rencontre un allié
	 * @author Ducasse Quentin
	 * @return faux si l'ennemi n'est pas en contact avec un allié vrai sinon
	 */
	
	public boolean rencontreAllie(int xCase,int yCase) {
		boolean res=false;
		
		// si la case du plateau correspond a la case de l'ennemi
			if (xCase<Jeu.longueur && yCase>=0) {
				// si la case du plateau est un obstacle
		
					if(Jeu.plateau[yCase][xCase].getPosAllie()!=-1) {
						res=true;
					}
					
				}
		return res;
		
	}
	
	
	/**
	 * met a jour la barre de vie si l'allie est touché
	 * @author Ducasse Quentin
	 */
	public void updateBarreVie() {
		// on update les degats infliges sur la barre de vie
		if (hpActuel>0) {
			barreVie.setLongueurVie(barreVie.getLongueurBarre()*hpActuel/this.getHp());
		}
		
		barreVie.setX(getX()+longueur/2);
		barreVie.setY(getY()+largeur/4);
	}
	
	/**
	 * verifie si les ennemis sortent de la map
	 * @author Ducasse Quentin
	 * @return vrai si l'ennemi sors de la map faux sinon
	 */
	public boolean sorsDeLaMap() {
		boolean res=false;
		if(getX()<0) {
			res=true;
		}
		return res;
	}
	
	/**
	 * deplacent les ennemis sur la trajectoire du chemin généré aleatoirement
	 * @author Ducasse Quentin,Cuyala Tristan
	 */
	public void se_deplacer(int xCase,int yCase) {
				
				// si l'ennemi rencontre un allié
				if (this.rencontreAllie(xCase,yCase)) {	
						// l'ennemi s'arrete et son etatAttaque devient vrai
						dx=0;
						dy=0;
						etatAttaque=true;
				}
				// si l'ennemi ne rencontre pas d'alliés
				else if (iterateur<chemin.size()){
					// on sauvegarde les coordonnées de la case que l'ennemi vise sur le chemin
					
					int xBut=chemin.get(this.iterateur).getX()*Jeu.TAILLE_BLOC+8;
					int yBut=chemin.get(this.iterateur).getY()*Jeu.TAILLE_BLOC+4;
					
					// si l'ennemi va vers la gauche
					if (direction==1) {		
						
						// si on a atteint la case But
						if(xBut>=getX()) {
							// si la  case but se situe une case au dessus de l'ennemi
							if((int)yBut/Jeu.TAILLE_BLOC==(int)getY()/Jeu.TAILLE_BLOC-1) {
								// on fait monter l'ennemi et on change sa direction vers le haut
								dy=-vitesse;
								dx=0;
								direction=2;
							
							}
							// si la  case but se situe une case en dessous  de l'ennemi
							else if((int)yBut/Jeu.TAILLE_BLOC==(int)getY()/Jeu.TAILLE_BLOC+1){
								// on fait descendre l'ennemi et on change sa direction vers le bas
								dy=vitesse;
								dx=0;
								direction=3;
								
							}
							// si la  case but se situe sur la meme colonne que ligne que l'ennemi alors on fait avancer l'ennemi vers la gauche
							else {
								
								dx=-vitesse;
								dy=0;
								direction=1;
								
							}
							
							iterateur++;		
						
						}
						
						// si on a pas atteint le but 
						
						else {
							// on continue le deplacement
							dx=-vitesse;
							dy=0;
							direction=1;
						}
					}
					
					// meme processus pour la direction vers le haut
					if (direction==2) {
						// si on a pas atteint le but
				
						if(yBut>=getY()) {
							
							if((int)xBut/Jeu.TAILLE_BLOC==(int)getX()/Jeu.TAILLE_BLOC-1) {
								dx=-vitesse;
								dy=0;
								direction=1;
							
							}
							else {
								dy=-vitesse;
								dx=0;
								direction=2;
				
							}
							iterateur++;
							
						}
						else {
							dy=-vitesse;
							dx=0;
							direction=2;
						}
					}
					
					// meme processus pour la direction vers le bas
					if (direction==3) {
						// si on a pas atteint le but
				
						if(yBut<=getY()) {
							
							if((int)xBut/Jeu.TAILLE_BLOC==(int)getX()/Jeu.TAILLE_BLOC-1) {
								dx=-vitesse;
								dy=0;
								direction=1;
								
							}
							else {
								dy=vitesse;
								dx=0;
								direction=3;
							
							}
							iterateur++;
							
						}
						else {
							dy=vitesse;
							dx=0;
							direction=3;
						}
					}
				}
				
				// par securité si aucun cas n'est respecté on fait deplacer l'ennemi vers la gauche
				else {
					direction=1;
					dx=-vitesse;
					dy=0;
				}
												
		// on update la position de l'ennemi
		this.setX(getX()+dx);
		this.setY(getY()+dy);
	}
	
	/**
	 * modifie la barre de vie des alliés a un intervalle de temps régulier
	 * @author Ducasse Quentin
	 */
	
	public void attaquer(double xCase,double yCase) {
		
		
		int pos=-1;
		
		// on sauvegarde la position de l'allié rencontré
		pos= Jeu.plateau[(int)yCase][(int)xCase].getPosAllie();
	
		Allié a=null;
		int hpA;
		
		// si l'allié existe
		if ((pos!=-1) && (pos<Jeu.listeAllie.size())) {
			// on modifie les hp Actuels de l'allié
			a = Jeu.listeAllie.get(pos);
			hpA=a.getHpActuel()-attaque;
			// si l'allié a encore de la vie on diminue ses points de vie
			if (hpA>0) {
				 a.setHpActuel(hpA);
			}
			// sinon on le tue
			else{
				a.setMort(true);
				// on libere la case du tableau qui passe a -1
				Jeu.plateau[(int)yCase][(int)xCase].setPosAllie(-1);
			
			}
				
				// on sort de l'etat d'attaque
				etatAttaque=false;
			}
		// par securité on garde un etat de non attaque par default
		else {
			etatAttaque=false;
		}
		
	}
	
	
	/**
	 * meta jour les ennemis (deplacement et attaque)
	 * @author Ducasse Quentin
	 */
	public void update() {
		caseEnnemi=getCase();
		// on sauvegarde les coordonnées de la case actuel de l'ennemi
		int xCase=caseEnnemi.getX();
		int yCase=caseEnnemi.getY();
		
		// si l'ennemi n'attaque pas
		if (!etatAttaque) {
			// il se deplace
			this.se_deplacer(xCase,yCase);
		}
		// sinon
		else {
			// si l'intervalle de temps d'attaque est atteint l'ennemi attaque
			if(timer>=timerAttaque) {
				attaquer(xCase,yCase);
				
				timer=0;
			}
			timer++;
			
	
		}
		
		// on update la barre de vie de l'ennemi
		this.updateBarreVie();
		
	}
	

	/** 
	 * Permet d'afficher les ennemis
	 * @author Lagahe Hugo,Cuyala Tristan,Ducasse Quentin
	 * @param gc
	 */
	public void afficher(GraphicsContext gc) {

		//On affiche la destination de l'ennemi
		try {
			//On charge l'image
			Image imageB = new Image(new FileInputStream("src/DungeonTileset/frames/floor_red.png"));
			//On l'affiche
			gc.drawImage(imageB, chemin.get(chemin.size()-1).getX()*Jeu.TAILLE_BLOC,chemin.get(chemin.size()-1).getY()*Jeu.TAILLE_BLOC,(Jeu.TAILLE_BLOC),(Jeu.TAILLE_BLOC));
		} catch (FileNotFoundException e) {
			//On affiche un message d'erreur si l'image n'a pas été trouvée
			System.out.println("FileNotFoundException"+ e.getMessage());
		}		
		
		Image img;
		String nom;
		if (getId()==1) {
			nom="big_demon";
		}
		else if (getId()==2) {
			nom="big_zombie";
		}
		else {
			nom="chort";
		}
		String str="src/DungeonTileset/frames/"+nom+"_run_anim_f0.png";
		timerFrame++;
		// si le personnage est arrete
		if (dx==0 && dy==0) {
			if (timerFrame/frequence==0) {		
				str="src/DungeonTileset/frames/"+nom+"_idle_anim_f0.png";	
			}
			else if (timerFrame/frequence==1 )  {
				if (nom=="big_zombie") {
					str="src/DungeonTileset/frames/"+nom+"_idle_anim_f0.png";
				}
				else {
					str="src/DungeonTileset/frames/"+nom+"_idle_anim_f1.png";
				}
				
			}
			else if (timerFrame/frequence==2) {
				str="src/DungeonTileset/frames/"+nom+"_idle_anim_f2.png";
			}
			else if (timerFrame/frequence==3){
				str="src/DungeonTileset/frames/"+nom+"_idle_anim_f3.png";
			}
			else if (timerFrame>3*frequence) {
				timerFrame=0;
			}
		}
		// sinon
		else {
			if (timerFrame/frequence==0) {		
				str="src/DungeonTileset/frames/"+nom+"_run_anim_f0.png";	
			}
			else if (timerFrame/frequence==1)  {
				str="src/DungeonTileset/frames/"+nom+"_run_anim_f1.png";
			}
			else if (timerFrame/frequence==2) {
				str="src/DungeonTileset/frames/"+nom+"_run_anim_f2.png";
			}
			else if (timerFrame/frequence==3){
				str="src/DungeonTileset/frames/"+nom+"_run_anim_f3.png";
			}
			else if (timerFrame>3*frequence) {
				timerFrame=0;
			}
		}
		
	
		try {
			img= new Image(new FileInputStream(str));
			gc.drawImage(img, this.getX(),this.getY(),(longueur*2),(largeur*2));
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException"+ e.getMessage());
		}

			// ennemis si l'ennemi à été touché par un allié
		if (hpActuel!=hp) {
			barreVie.affichage(gc);
		}
		//On affiche le niveau de l'ennemi
		barreVie.affichageLevel(gc,niveau);		
	}
	
	/**
	 * Méthode getHitBox()
	 * @return Rectange
	 * Retourne la hitbox des ennemis
	 */

	
	
	/**
	 * Enlève un ennemi de la liste et ajoute son item à la liste
	 * @author Lagahe Hugo
	 */
	public void mourir() {
		//On déclare l'ennemi mort
		this.setMort(true);
		//On crée un nouvel item avec la position de l'ennemi
		Item item1 = new Item(1,10,(int)this.x+1,(int)this.y);
		//On ajoute l'item
		Jeu.listeItem.add(item1);
	}
	
	
	/**
	 * recherche le sommet de poid minimum et renvoie la case associée
	 * @author Lacoux Quentin
	 * @param hm , hashmap reliant une case et son sommet associé
	 * @return la case avec le sommet de poids minimum
	 */
	public Case rechercheMin(Map<Case,Sommet> hm) {
//		on initialise le poid minimum egal au nombre de case du tab
		int intmin=Jeu.largeur*Jeu.longueur;
//		la case renvoyer est nulle au depart
		Case min = null;
//		pour chaque case du plateau
		for (int i=0;i<Jeu.largeur;i++) {
			for (int j=0;j<Jeu.longueur;j++) {
//				si la case n'set pas checkée et est inferieur au min trouvé
				if (hm.get(Jeu.plateau[i][j]).getPoids()<intmin & !hm.get(Jeu.plateau[i][j]).isCheck()) {
//					on change la valeur du min et la valeur de la case renvoyée
					min=Jeu.plateau[i][j];
					intmin=hm.get(min).getPoids();
			
				}
			}
		}
		return min;
	}

	/**
	 * recherche le chemin le plus court pour un ennemi suivant l'algorithme de Dijkstra
	 * @author Lacoux Quentin
	 * @return une liste de case a suivre pour faire le chemin le plus court du point de spawn à l'arrivée
	 */
	public ArrayList<Case> chercheChemin(){
		ArrayList<Case> chemin = new ArrayList<Case>();
		Map<Case,Sommet> hm = new HashMap<>();
		int nbmur=0;
//		initialisation des sommets a partir du plateau
		for (int i=0;i<Jeu.largeur;i++) {
			for (int j=0;j<Jeu.longueur;j++) {
				hm.put(Jeu.plateau[i][j],new Sommet());
			}
		}
//		on recupere les coordonnees de la case de l'ennemi
		int yCase=getCase().getY();
		int xCase=getCase().getX();
//		on met le poid de la case du depart à 0 et le pere nul
		hm.get(Jeu.plateau[yCase][xCase-1]).setPoids(0);
		hm.get(Jeu.plateau[yCase][xCase-1]).setPere(null);
//		tant que rechercheMin renvoie une case
		while (rechercheMin(hm)!=null) {
//			la variable kase prend cette case
			Case kase=rechercheMin(hm);
//			on recupere tout les voisin de cette case
			ArrayList<Case> voisins=kase.getVoisins();
//			pour chaque voisin
			for (int j=0;j<voisins.size();j++) {
//				si le poid du voisin+1 et inferieur au poids de la case
				if (hm.get(kase).getPoids()+1<hm.get(voisins.get(j)).getPoids()) {
//					alors on actualise le poid de la case et on change de pere
					hm.get(voisins.get(j)).setPoids(hm.get(kase).getPoids()+1);
					hm.get(voisins.get(j)).setPere(kase);
				}
			}
//			on marque ma case comme checkée
			hm.get(kase).setCheck(true);
		}
//		sur les 5 derenier cases du chemin, l'ennemi ne changera pas de colonne
		for (int i=0;i<5;i++) {
			chemin.add(Jeu.plateau[destination][i]);
		}
//		la case de fin de Dijkstra 
		Case kase=Jeu.plateau[destination][5];//destination
//		pour chaque case de la case d'arrivée a la case de départ
		while (hm.get(kase).getPere()!=null) {
//			on ajoue a l'itinéraire la case courante et prenons le pere de celle ci
			chemin.add(kase);
			kase=hm.get(kase).getPere();
		}
//		on ajoute la case de début à l'itinéraire
		chemin.add(Jeu.plateau[yCase][xCase-1]);
//		on inverse le chemin pour qu'il soit dans le bon sens
		Collections.reverse(chemin);
		return chemin;
		
	}
	// getter et setter
	public int getIterateur() {
		return(iterateur);
	}
	
	public void setIterateur( int Iterateur) {
		this.iterateur = Iterateur;
	}
	
	public Rectangle getHitBox() {
		return new Rectangle((int)x,(int)y,largeur,longueur);
	}

	public int getHpActuel() {
		return hpActuel;
	}
	
	public void setHpActuel(int val) {
		hpActuel=val;
	}
	
	public boolean isMort() {
		return mort;
	}
	
	public void setMort(boolean val) {
		mort=val;
	}
	
}

