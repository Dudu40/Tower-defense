import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
//import javafx.scene.shape.ArcType;
//import javafx.scene.text.Font;
//
//>>>>>>> 11b02d382775ae67e78dac0e0ff3eb0abe7844ab
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Allié extends Attaquant {
	private int prix;
	private int portee;
	private int hpActuel;
	private int longueur=Jeu.TAILLE_BLOC;
	private int largeur=Jeu.TAILLE_BLOC;
	private boolean mort=false;
	private boolean etatMine=false;
	private Color color;
	private ArrayList<Projectile> listeProjectile = new ArrayList<Projectile>(); 
	private BarreVie barreVie;
	
	private int level;
	
	public Allié (int Id, int X, int Y, int HP, int Attaque, int VitesseAttaque, int Prix, int Portee) {
		super(Id,X,Y,HP,Attaque,VitesseAttaque);
		this.prix = Prix;
		this.portee = Portee;
		this.hpActuel=getHp();
		this.color=null;
		this.level=1;
		barreVie= new BarreVie(this.getX(),this.getY()+1,longueur,largeur/20,1);
		if (getId()==3) {
			lancerBombe();
		}
		else if (getId()==5) {
			genererMoney();
		}
		else if (getAttaque()>0){
			lanceProjectile(2000/getVitesseAttaque());
		}
		else if (getId()==6){
			activerMine();
			
		}
		
		
	}
	/**
	 * ajoute un niveau au unité allié:augment l'attaque et la santé de 50%
	 * @author Lacoux Quentin
	 */
	public void levelUp() {
		this.level=this.level+1;
		this.attaque=(int)(this.attaque*1.5);
		this.hp=(int)(this.hpActuel*1.5);
		this.hpActuel=this.hp;
		
	}
	
	/**
	 * retourne la Case de l'ennemi
	 * @author Lacoux Quentin
	 * @return la case dand laquel l'allié est
	 */
	public Case getCase() {	
		Case caseEnnemi= new Case((int)getX()/Jeu.TAILLE_BLOC,(int)getY()/Jeu.TAILLE_BLOC);
		return caseEnnemi;
		}
		
	/**
	 * renvoie si un ennemi est present sur la meme ligne que l'allie
	 * @author Ducasse Quentin
	 * @return faux si il n'y a pas d'ennemi, vrai si il y en a
	 */
	public boolean existeEnnemiSurLigne() {
		boolean res=false;
		if(Jeu.listeEnnemi!=null) {
			for(int i=0;i<Jeu.listeEnnemi.size();i++) {
				if (Jeu.listeEnnemi.get(i).getCase().getY()==getCase().getY()) {
					res=true;
				}
			}
		}
		return res;
	}
	
	/**
	 * regarde si un ennemi est present sur la meme case
	 * @author Ducasse Quentin
	 * @return faux si il y e aucun ennemi, vrai si il y en a un
	 */
	public boolean checkEnnemiSurCase() {
		boolean res=false;
		Jeu.plateau[getCase().getY()][getCase().getX()].setPosAllie(-1);
		if(Jeu.listeEnnemi!=null) {
			for(int i=0;i<Jeu.listeEnnemi.size();i++) {
				// si l'ennemi est sur la mine
				if (Jeu.listeEnnemi.get(i).getHitBox().contains(getX()+Jeu.TAILLE_BLOC/3,getY()+Jeu.TAILLE_BLOC/3)){
					
					this.mort=true;
				}
				if (mort) {
					if ((Jeu.listeEnnemi.get(i).getCase().getX()==getCase().getX() && Jeu.listeEnnemi.get(i).getCase().getY()==getCase().getY())){
						Jeu.listeEnnemi.get(i).mourir();
					}
				}
			}
		}
		return res;
	}
		

	/**
	 * active un piege 10 seconde apresqu'il soit placé
	 * @author Ducasse Quentin
	 */
		public void activerMine() {
			Timer time = new Timer();
		       
		       time.schedule(new TimerTask() {
		    	 int compteur=0;
		        @Override
		        public void run() {
		        	
		        	compteur++;
		        	if (compteur==15) {
		        		compteur=0;
		        		etatMine=true;
		        	}
		        	            
		               
		            }
		           
		        }, 1000,1000);
			
		}
		
		/**
		 * genere de l'argent toute les 10 secondes
		 * @author Lacoux Quentin,Ducasse Quentin
		 */
		public void genererMoney() {

			Timer time = new Timer();
			       
		       time.schedule(new TimerTask() {
		    	 int compteur=0;
		        @Override
		        public void run() {
		        	
		        	compteur++;
		        	if (compteur==10) {
		        		compteur=0;
		        		Jeu.money+=attaque;
		        	}
		        	            
		               
		            }
		           
		        }, 1000,1000);
		
		}
		
		/**
		 * fait exploser la bombe
		 * @author Ducasse Quentin
		 */
		public void explose() {
			int xCase=getCase().getX();
			int yCase=getCase().getY();
			for(int i=yCase-1;i<=yCase+1;i++) {
				for(int j=xCase-1;j<=xCase+1;j++) {
					
					if(i>=0 && j>=0 && i<Jeu.largeur && j<Jeu.longueur){
						for(int k=0;k<Jeu.listeEnnemi.size();k++) {
							if ((Jeu.listeEnnemi.get(k).getCase().getX()==j) && (Jeu.listeEnnemi.get(k).getCase().getY()==i)) {
								Jeu.listeEnnemi.get(k).mourir();
							}
						}
					}
				}
			}
		}
		/**
		 * lance un projectile sur sa ligne
		 * @author Ducasse Quentin
		 */
		public void lanceProjectile(int delai) {
				Timer time = new Timer();
			       
		        time.schedule(new TimerTask() {
		       
		            @Override
		            public void run() {
		               
		                	if ((existeEnnemiSurLigne())) {
		                		listeProjectile.add(new Projectile(getX(),getY(),getAttaque()));
		                	}
		                   
		            
		               
		            }
		           
		        }, delai,delai);
			
		}
		
		/**
		 * lance la procedure d'explosion de la bombe 2seconde apres l'avoir placée 
		 * @author Ducasse Quentin
		 */
		public void lancerBombe() {
			Timer time = new Timer();
		       
	        time.schedule(new TimerTask() {
	            int timerTir=2;
	            @Override
	            public void run() {
	                timerTir--;
	               
	                if (timerTir==0) {
	  
	                	
	                	explose();
	                	mort=true;
	                	cancel();
	                   
	                }       
	               
	            }
	           
	        }, 1000,1000);
		
	}
		
	/**
	 * Affiche les alliés sur la carte
	 * @author Lagahe Hugo,Ducasse Quentin,Cuyala Tristan
	 * @param gc
	 */
	public void affichage(GraphicsContext gc) {
		if (getId()==1) {
			//le canon de base
			try {
				//On charge l'image
				Image imageB = new Image(new FileInputStream("src/dungeon2/topdown_shooter/towers/cannon/1_right.png"));
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
				Image imageB = new Image(new FileInputStream("src/dungeon2/topdown_shooter/towers/cannon/3_right.png"));
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
				Image imageB = new Image(new FileInputStream("src/other/bombe.png"));
				//On l'affiche
				gc.drawImage(imageB,this.getX()-longueur/4,this.getY()-largeur/3,(longueur+longueur/2),(largeur+largeur/2));
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
			//La mine d'or
			try {
				//On charge l'image
				Image imageB = new Image(new FileInputStream("src/other/mine.png"));
				//On l'affiche
				gc.drawImage(imageB, this.getX(),this.getY(),(longueur),(largeur));
			} catch (FileNotFoundException e) {
				//On affiche un message d'erreur si l'image n'a pas été trouvée
				System.out.println("FileNotFoundException"+ e.getMessage());
			}
		}
		else if (etatMine) {
			//Le piège
			try {
				//On charge l'image
				Image imageB = new Image(new FileInputStream("src/DungeonTileset/frames/floor_spikes_anim_f3.png"));
				//On l'affiche
				gc.drawImage(imageB, this.getX()+10,this.getY()+10,(longueur/1.5),(largeur/1.5));
			} catch (FileNotFoundException e) {
				//On affiche un message d'erreur si l'image n'a pas été trouvée
				System.out.println("FileNotFoundException"+ e.getMessage());
			}
		} else {
			try {
				//On charge l'image
				Image imageB = new Image(new FileInputStream("src/DungeonTileset/frames/floor_spikes_anim_f1.png"));
				//On l'affiche
				gc.drawImage(imageB, this.getX()+10,this.getY()+10,(longueur/1.5),(largeur/1.5));
			} catch (FileNotFoundException e) {
				//On affiche un message d'erreur si l'image n'a pas été trouvée
				System.out.println("FileNotFoundException"+ e.getMessage());
			}
		}
		//On affiche les vies de l'alliés si il a été touché
		if (hpActuel>0 && hpActuel!=hp) {
			barreVie.affichage(gc);
		}
		//On affiche le niveau de l'allié
		barreVie.affichageLevel(gc,level);
		//on appelle la fonction d'affichage des projectiles
		for(int i=0;i<listeProjectile.size();i++) {
			if (listeProjectile!=null) {
				listeProjectile.get(i).affichage(gc);
			}	
		}
	}
	
	/**
	 * met a jour la barre de vie si l'allie est touché
	 * @author Ducasse Quentin
	 */
	public void updateBarreVie() {
		// on update les degats infliges sur la barre de vie
		if (getHp()>0) {
			barreVie.setLongueurVie((barreVie.getLongueurBarre()*hpActuel)/this.getHp());
		}

	}
	/**
	 * meta jour les elements de alliés
	 * @author Ducasse Quentin
	 */
	public void update() {
		// update les projectiles
			for(int i=0;i<listeProjectile.size();i++) {
				listeProjectile.get(i).update();
				if (listeProjectile.get(i).sorsDeLaMap() || listeProjectile.get(i).rencontreEnnemi() || listeProjectile.get(i).rencontreObstacle()){
					listeProjectile.remove(i);
					
				}
			}
			if (etatMine){
				checkEnnemiSurCase();
				
			}
			
			updateBarreVie();
	}
	
	public boolean isMort() {
		return mort;
	}
	
	//Getter et Setter
	public void setMort(boolean val) {
		mort=val;
	}
	
	public int getHpActuel() {
		return hpActuel;
	}
	
	public void setHpActuel(int val) {
		hpActuel=val;
	}

	public int getLongueur() {
		return longueur;
	}
	
	public int getLargeur() {
		return largeur;
	}
	
	public int getPortee() {
		return portee;
	}
	
	public int getPrix() {
		return prix;
	}
	
	public Rectangle getHitBox() {
		return new Rectangle((int)x,(int)y,longueur,largeur);
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		color=c;
	}
}
