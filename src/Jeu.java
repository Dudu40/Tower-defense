
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Stack;
import javafx.scene.input.MouseEvent; 

public class Jeu extends Application implements EventHandler<ActionEvent>{
	// on dimensionne la fenetre
	private int longueurFenetre;
	private int largeurFenetre;
	public static Case[][] plateau;
	public static int TAILLE_BLOC=64;
	private int longueurMain;
	private int largeurMain;
	public static int largeur=12;
	public static int longueur=15;
	private String status="Menu";
	private MainJoueur mainJoueur;
	public static int money=50;
	private int score=0;
	private int  nbVague=0;
	private int timerItem=0;
	private int timerScore=0;
	private int timerSpawn=0;
	private int xPause=longueur*TAILLE_BLOC-TAILLE_BLOC-TAILLE_BLOC/2;
	private int yPause =-TAILLE_BLOC+TAILLE_BLOC/12;
	private Image pauseImg,menuImg;
	private Font fontDungeon = null;
	
	
	public static ArrayList<Ennemi> listeEnnemi = new ArrayList<Ennemi>();
	public static ArrayList<Allié> listeAllie=new ArrayList<Allié>();
	//Déclaration
	public static ArrayList<Item> listeItem = new ArrayList<Item>();
	
	AnimationTimer timerJeu;
	
	Stage fenetre ;
	// on cree le conteneur qui contiendra l'affichage du jeu
	 Canvas canvas;
	 Pane conteneur;
	 GraphicsContext gc;


		public Jeu() {
			this.largeurFenetre=largeur*TAILLE_BLOC;
			this.longueurFenetre=longueur*TAILLE_BLOC;
			
			this.longueurMain=longueurFenetre-longueurFenetre/3;
			this.largeurMain=2*TAILLE_BLOC;
			// init un nombre d'ennemi a un position x choisi
			plateau=initTab(largeur,longueur);
			mainJoueur= new MainJoueur(3*TAILLE_BLOC-TAILLE_BLOC/2,largeurFenetre+TAILLE_BLOC/4,longueurMain+2*TAILLE_BLOC,largeurMain);
			listeEnnemi.add(new Ennemi(1,longueur*TAILLE_BLOC,3*TAILLE_BLOC+TAILLE_BLOC/4,100,30,1,1,1,1));
			try {
				pauseImg=new Image(new FileInputStream("src/other/play.png"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * verifie si un chemin existe et que les murailles ne bloque pas tout passage
		 * @author Lacoux Quentin
		 * @param plateau, plateau de case a verifié
		 * @return vrai si le plateau et OK, faux si il est impossible
		 */
	public boolean verifTab(Case[][] plateau) {
//	on demarre en se disant que le plateau est OK
		boolean bool=true;
		boolean bool2;
//		pour chaque colonne
		for (int j=0;j<longueur-1;j++) {
//			on initialise un booleen vrai
			bool2=true;
//			pour chaque ligne
			for (int i=0;i<largeur;i++) {
//				on verifie si la case ou la case a coté est un mur
				bool2=bool2 & (plateau[i][j].isMur() | plateau[i][j+1].isMur());
			}
//			on verifie sur tout la ligne sion peut passer
			bool=bool & !bool2;
		}
		return bool;
	}
			 
	/**
	 * initialise le plateau du jeu avec des murailles aleatoire: algo de parours en profondeur
	 * @author Lacoux Quentin
	 * @param largeur,largeur du plateau
	 * @param longueur, longueur du plateau
	 * @return un tableau de case correspondant au plateau
	 */			 
	 public Case[][] initTab(int largeur,int longueur){
//		 on declare le tableau2d
		 Case[][] tab=new Case[largeur][longueur];
		 boolean estPossible = false;
//		 on initalse toute les case du tableau
		 	//y
			for (int i=0;i<largeur;i++) {
				for (int j=0;j<longueur;j++) {
					tab[i][j]=new Case(j,i);
				}
			}
//			on declare un ensemble de marquage et une pile
			HashSet<Case> ensMarquage = new HashSet<Case>();
			Stack<Case> stack = new Stack<Case>();
//			tant que le plateau ne convient pas:
			while (!estPossible) {
//			pour generer 5 mur
			for (int i=0;i<5;i++) {
//				on prend des coor au hasrd dans la zone 
				int rdmx=(int)(Math.random()*largeur);
				int rdmy=(int)(Math.random()*(longueur-6))+5;
//				on ajoute a la pile la case designee au hasard
				tab[rdmx][rdmy].setMur(true);
				stack.push(tab[rdmx][rdmy]);
				ensMarquage.add(tab[rdmx][rdmy]);
//				tant que la pile n'est pas vide
				while (!stack.empty()) {
//					on prend le premier element de la liste
					Case maCase=stack.peek();
//					on depile
					stack.pop();
//					un mur a 70% de chance d'etre generé a coté d'un autre mur
					if (Math.random()<0.5) {
//						si il est généré on actualise la case
						maCase.setMur(true);
//						on ajoute tout les "suivants" à la pile si il ne sont pas dansl'ensemble de marquage
						if (maCase.getY()+1<largeur) {
							Case next = tab[maCase.getY()+1][maCase.getX()];
							if (!ensMarquage.contains(next)) {
								stack.push(next);
								ensMarquage.add(next);
							}
						}
						if (maCase.getY()-1>-1) {
							Case next = tab[maCase.getY()-1][maCase.getX()];
							if (!ensMarquage.contains(next)) {
								stack.push(next);
								ensMarquage.add(next);
							}
						}
					}	
				}
			}
//			on verifie si le plateau est OK
			estPossible=verifTab(tab);
		}
		return tab;
	}
	 
	/**
	 * 
	 */

	
	 
/**
 * affichage du plateau avec les differentes types de blocs
 * @author Ducasse Quentin, Lagahe Hugo
 * @return affiche le plateau de jeu
 */	
	public void affichagePlateau(GraphicsContext gc) {
		for(int i=0;i<largeur;i++) {
			for(int j=0;j<longueur;j++) {
				if(plateau[i][j].isMur()) {
					try {
						//On charge l'image
						Image imageB = new Image(new FileInputStream("src/DungeonTileset/frames/wall_mid.png"));
						//On l'affiche
						gc.drawImage(imageB, (j*TAILLE_BLOC),(i*TAILLE_BLOC),(longueur*5),(largeur*5.5));
					} catch (FileNotFoundException e) {
						//On affiche un message d'erreur si l'image n'a pas été trouvée
						System.out.println("FileNotFoundException"+ e.getMessage());
					}
				} else {
					if (plateau[i][j].isSelect()) {
						try {
							//On charge l'image
							Image imageB = new Image(new FileInputStream("src/DungeonTileset/frames/floorSelect.png"));
							//On l'affiche
							gc.drawImage(imageB, (j*TAILLE_BLOC),(i*TAILLE_BLOC),(longueur*5),(largeur*5.5));
						} catch (FileNotFoundException e) {
							//On affiche un message d'erreur si l'image n'a pas été trouvée
							System.out.println("FileNotFoundException"+ e.getMessage());
						}
					} else {
						try {
							//On charge l'image
							Image imageB = new Image(new FileInputStream("src/DungeonTileset/frames/floor_1.png"));
							//On l'affiche
							gc.drawImage(imageB, (j*TAILLE_BLOC),(i*TAILLE_BLOC),(longueur*5),(largeur*5.5));
						} catch (FileNotFoundException e) {
							//On affiche un message d'erreur si l'image n'a pas été trouvée
							System.out.println("FileNotFoundException"+ e.getMessage());
						}
					}
				}
			}
		}
	}

   
	 
	/**
	* start de javaFx
	* @author Ducasse Quentin,Lacoux Quentin,Lagahe Hugo,Cuyala Tristan
	* lance le jeu et l'ajoute a la scene du stage
	* 
	*/	 
	@Override
    public void start(Stage stage) throws Exception {
//		final File file = new File("musique/musique1.mp3"); 
//	    final Media media = new Media(file.toURI().toString()); 
//	    final MediaPlayer mediaPlayer = new MediaPlayer(media); 
//	    final MediaView mediaView = new MediaView(mediaPlayer);
    	//Définit le titre de la fenêtre
    	stage.setTitle("Tower defense");
    	// on creer une zone pour dessiner de la taille de la fenetre
    	canvas= new Canvas(longueurFenetre+2*TAILLE_BLOC,largeurFenetre+largeurMain+2*TAILLE_BLOC);
    	
    	
    	Group root = new Group();
//    	root.getChildren().setAll(mediaView);
    	
    	// on creer un evenement mouseEntred qui detecte la souris lorsqu'on ne clique pas
    	  EventHandler<MouseEvent> mouseEntred=new EventHandler<MouseEvent>(){
    		  @Override
              public void handle(MouseEvent e){
    			  // on surligne en blanc les contours de la carte a la meme position que la souris
    			  for(int i=0;i<mainJoueur.getListeCarte().size();i++) {
    				  
    				  if(mainJoueur.getListeCarte().get(i).getHitBox().contains(e.getX(),e.getY()-TAILLE_BLOC) && (mainJoueur.getListeCarte().get(i).estEtatPret()) && (mainJoueur.getListeCarte().get(i).getPrix()<=money))  {
    					 
    						 mainJoueur.getListeCarte().get(i).setColor(Color.WHITE);		 
    				  }
    				  else {
 						 mainJoueur.getListeCarte().get(i).setColor(null);
 					 }
    					  
    			  }
    			  
    		  }
                 
 
          };
          
       // on creer un evenement mouseEntred qui detecte si la souris se deplace lorsque que le click est enfoncé
          EventHandler<MouseEvent> mouseDragged=new EventHandler<MouseEvent>(){
    		  @Override
              public void handle(MouseEvent e){
    			
    			  for(int i=0;i<mainJoueur.getListeCarte().size();i++) {
    				  // si la souris se deplace et que le click est enfoncé et que la carte est surligné en blanc
    				  if(mainJoueur.getListeCarte().get(i).getColor()!=null) {
    					  
    					  // on deplace les coordonnées de la carte sur le curseur
    					  int xCase= mainJoueur.getListeCarte().get(i).getCase().getX();
    					  int yCase= mainJoueur.getListeCarte().get(i).getCase().getY();
    					 mainJoueur.getListeCarte().get(i).setX((int)e.getX());
    					 mainJoueur.getListeCarte().get(i).setY((int)e.getY()-TAILLE_BLOC);
    					 
    					 // si le curseur passe au dessus d'une case du plateau
    					  if (xCase<longueur && xCase>=0 && yCase<largeur && yCase>=0) {
    						  
    						  // si la case est libre 
    	        			  if ( xCase==(int)e.getX()/TAILLE_BLOC && yCase==(int)e.getY()/TAILLE_BLOC-1 && plateau[yCase][xCase].isMur()==false)  {
    	        				 // la case est illuminée
    	        				  plateau[yCase][xCase].setSelect(true);
    	        			  }
    	        			  else {
    	        				  // la case reste sombre
    	        				  plateau[yCase][xCase].setSelect(false);
    	        			  }
    	    			  }
    				  }
    			  }
    			  
    			
                 
              }
          };
          
          // on creer un evenement mouseReleased qui detecte lorsque le click est relaché
          EventHandler<MouseEvent> mouseReleased=new EventHandler<MouseEvent>(){
    		  @Override
              public void handle(MouseEvent e){
    			  // case qui correspond a la position de la carte qui est en train d'etre joué
    		
    			  int xCase,yCase;
    			  // coordonnes de la main du joueur
    			  int x=mainJoueur.getX();
    			  int y=mainJoueur.getY();
    			 
    			  // x temporaire qui correpsond a la posiition x de la carte de la main
    			  int xTemp =x;
    			  // espace en hauteur entre les bords de la main et la carte
    			  int espaceY=mainJoueur.getEspaceY();
    			  // espace en longueur entre les bords de la main et la carte
    			  int espaceX=mainJoueur.getEspaceX();
    			  
    			  // on parcourt la liste de carte de la min
    			  for(int i=0;i<mainJoueur.getListeCarte().size();i++) {
    				  // si la carte est selecionne par le joueur
    				  if(mainJoueur.getListeCarte().get(i).getColor()!=null) {
    					  	// on sauvegarde la position de la case a cette position
    					  	xCase=(int)(e.getX()/Jeu.TAILLE_BLOC);
    					  	yCase=(int)((e.getY()-TAILLE_BLOC)/Jeu.TAILLE_BLOC);
    						// si la case est disponible et que l'o peut poser un allié
    					  	if ((mainJoueur.getListeCarte().get(i).getPrix()<=money) && mainJoueur.getListeCarte().get(i).estEtatPret()) {
    					  		
    					  		// si la carte est posable
    					  		if (mainJoueur.getListeCarte().get(i).estPosable(xCase,yCase)) {	
    					  			mainJoueur.getListeCarte().get(i).placerAllie(xCase,yCase);
        					  		money-=mainJoueur.getListeCarte().get(i).getPrix();
        					  		score+=25;
//        					  		le prix de l'allie augmente de 20%
        					  		mainJoueur.getListeCarte().get(i).setPrix((int)(mainJoueur.getListeCarte().get(i).getPrix()*1.2));
    					  		} else {
//    					  			pout tout les alliés
    					  			for (int j=0;j<listeAllie.size();j++) {
//    		    					  	si l'allie est dans la case et que il est du meme type que celui qu l'on pose
    		    					  	if(listeAllie.get(j).getCase().getX()==xCase & listeAllie.get(j).getCase().getY()==yCase & listeAllie.get(j).Id==mainJoueur.getListeCarte().get(i).Id) {
    		    					  		// on monte le level de l'allié
    		    					  		listeAllie.get(j).levelUp();	
    		    				  			money-=mainJoueur.getListeCarte().get(i).getPrix();
    		    				  			mainJoueur.getListeCarte().get(i).setPrix((int)(mainJoueur.getListeCarte().get(i).getPrix()*1.2));
    		    				  		}
    					  			}	   					 
    					  		}
    					  		
    					  	}
    					  	// apres avoir posé l'allié 
    					  	// on remet la case selectionnée sombre 
    					  	if ((xCase<Jeu.longueur) && (xCase>=0) && (yCase<Jeu.largeur) && (yCase>=0)) {
    					  		plateau[yCase][xCase].setSelect(false);
    					  	}
    						// on replace la carte a sa place dans la main
    					  	xTemp=i*TAILLE_BLOC+i*espaceX;
					  		mainJoueur.getListeCarte().get(i).setX(x+xTemp+espaceX);
					  		mainJoueur.getListeCarte().get(i).setY(y+espaceY);		  						  
    									
    				  	}
    			  }
              } 
          };
          // on creer un evenement mouseClicked lorsqu'on clique
          EventHandler<MouseEvent> mouseClicked=new EventHandler<MouseEvent>() {
        	  @Override
        	  public void handle(MouseEvent e) {
        		  // on sauvegarde les coordonnées du centre du bouton pause
        		  int xCentre=xPause+(TAILLE_BLOC-TAILLE_BLOC/8)/6;
        		  int yCentre=yPause+(TAILLE_BLOC-TAILLE_BLOC/8)/6;
        		  double dx=e.getX()-xCentre;
        		  double dy=e.getY()-TAILLE_BLOC-yCentre;
        		  double dist=Math.sqrt(dx * dx + dy * dy);
        		 
        		 // si la souris est dans le cercle du bouton pause
        		  if (dist<TAILLE_BLOC/2) {
        			  // le status du jeu passe en pause
        			  if (status=="Pause") {
        				  // affichage du bouton pause
        				  try {
        						pauseImg= new Image(new FileInputStream("src/other/play.png"));
        						gc.drawImage(pauseImg,xPause,yPause,TAILLE_BLOC-TAILLE_BLOC/6,TAILLE_BLOC-TAILLE_BLOC/6);
        					} catch (FileNotFoundException erreur) {
        						System.out.println("FileNotFoundException"+ erreur.getMessage());
        					}
        				  status="Jeu";
        			  }
        			  // sinon le status reste celui du jeu
        			  // on affiche le bouton play
        			  else if (status=="Jeu") {
        				  try {
        						pauseImg = new Image(new FileInputStream("src/other/pause.png"));
        						gc.drawImage(pauseImg,xPause,yPause,TAILLE_BLOC-TAILLE_BLOC/6,TAILLE_BLOC-TAILLE_BLOC/6);
        					} catch (FileNotFoundException erreur) {
        						System.out.println("FileNotFoundException"+ erreur.getMessage());
        					}
        				  status="Pause";
        			  }
        		  }
//        		  on recupere les coordonnee de case de la souris
        		  int casex=(int)(e.getX()/Jeu.TAILLE_BLOC);
        		  int casey=(int)((e.getY()-TAILLE_BLOC)/Jeu.TAILLE_BLOC);
//        		  on regarde dans tout les item si ils sont dans ces coordonnee
        		  for (int i=0;i<Jeu.listeItem.size();i++) {
        			  // si on clique sur un item
        			  if (Jeu.listeItem.get(i).getCase().equals(new Case(casex,casey))){
        				  // on augmente la money du joueur et on supprime l'item
        				  money=money+Jeu.listeItem.get(i).getValue();
        				  Jeu.listeItem.remove(i);
        			  }
        		  }	
        	  }
          };
          
         // on ajoute tous les events dans le root
         root.addEventHandler(MouseEvent.MOUSE_MOVED,mouseEntred);
         root.addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDragged);
         root.addEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleased);
         root.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseClicked);
         
        // on creer le contexte Graphique du canvas 
        gc = canvas.getGraphicsContext2D();
        
        // on deplace le 0,0 de un bloc vers le bas pour laisser place a la barre des scores
		gc.translate(0, TAILLE_BLOC);
		
		// on ajoute la canvas au root
		root.getChildren().add(canvas);
		
		//On introduit la police  du jeu
		fontDungeon = null;
		try {
			fontDungeon = Font.loadFont(new FileInputStream(new File("police/dungeon.TTF")), 20);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
       // on cree le bouton nouvelle partie et on l'ajoute au root
        Button btnN = new Button("Nouvelle partie");
        btnN.setFont(fontDungeon);
        btnN.setLayoutX(425);
        btnN.setLayoutY(250);
       
        // on dfefinis l'action du bouton qui est de passer le status du jeu a "Jeu" et cacher le bouton 
        btnN.setOnAction(event->{status="Jeu";	btnN.setVisible(false);});
        
        // on ajoute le bouton au root
        root.getChildren().addAll(btnN);
        
        // on cree la scene javaFx qui contine le root avec le canavs et les boutons
        Scene scene = new Scene(root,longueurFenetre,largeurFenetre+largeurMain+2*TAILLE_BLOC);
       
        // on ajoute la scene au stage javFx
        stage.setScene(scene);
        
        // on femre l'application lorsqu'on femre la fenetre
        Platform.setImplicitExit(true);
        stage.setOnCloseRequest((e) -> {
            Platform.exit();
            System.exit(0);
        });
        //Possibilité de redimensionner la fenêtre = true : oui
        stage.setResizable(false);
        //Affiche la fenêtre
        stage.show();
        //Centre la fenêtre
        stage.centerOnScreen();
//        mediaPlayer.play();
        // on lance le jeu
        lancerJeu();
    }


public void vagueLevel(int level) {
	int posY;
	// nombre d'ennemi de type 1
	int r=0;
	Ennemi e=null;

	
	for(int i=0;i<level;i++) {
		// on genere un nombre d'ennemi definit pas le level
		posY=(int)(Math.random() * ((largeur - 1) + 1));
		// on prend un random entre 1 et 3 pour choisir le type d'ennemi généré
		r=1 + (int)(Math.random() * ((2) + 1));
		if (r==1) {
			e=new Ennemi(1,longueur*TAILLE_BLOC,posY*TAILLE_BLOC+TAILLE_BLOC/4,100+20*(level-1),30+10*(level-1),1,1,1,level);
		}
		else if (r==2) {
			e=new Ennemi(2,longueur*TAILLE_BLOC,posY*TAILLE_BLOC+TAILLE_BLOC/4,100+20*(level-1),20+10*(level-1),2,1,1.5,level);
		}
		else {
			e=new Ennemi(3,longueur*TAILLE_BLOC,posY*TAILLE_BLOC+TAILLE_BLOC/4,80+20*(level-1),10+20*(level-1),3,1,2,level);
		}
		
		listeEnnemi.add(e);			
		
	}
}
	
	/**
	* met a jours tous les alliés
	* @author Ducasse Quentin
	* 
	*/
	    public void updateListeAllies() {
	    	if (listeAllie!=null) {
	    		for(int i=0;i<listeAllie.size();i++) {
	    			listeAllie.get(i).update();
	    			if (listeAllie.get(i).isMort()) {
	    				plateau[listeAllie.get(i).getCase().getY()][listeAllie.get(i).getCase().getX()].setPosAllie(-1);
	    				listeAllie.remove(i);
	  				}
	    		}
	    	}
	    }
	    
	    /**
	    * met a jours tous les ennemis
	    * @author Ducasse Quentin
	    * 
	    */
	    
		 public void updateListeEnnemis() {
			 for(int i=0;i<listeEnnemi.size();i++) {
				 if (listeEnnemi!=null){
					 listeEnnemi.get(i).update();
					 // si un ennemi sors de la map on arrete le jeu
					 if (listeEnnemi.get(i).sorsDeLaMap()) {
						 status="Fin";
					 }
					 // si un ennemi est mort on le supprime
					 if (listeEnnemi.get(i).isMort()) {
						 listeEnnemi.remove(i);
						 score+=50;
					 }
				 }
			 }
		}
		
		 /**
		    * met a jours tous les elements du jeu et genere les vagues
		    * @author Ducasse Quentin
		    * 
		  */
		 
    public void update(int timerSpawn,int timerScore,int timerItem) {
    	// met a jour des alliés
    	updateListeAllies();
    	// met a jour des ennemis
    	updateListeEnnemis();
    	//vagues a 10 secondes d'ecart
    	
    	if (timerSpawn%300==0) {
    		timerSpawn=0;
    		if (nbVague<=3) {
    			// 1 zombie de type 1 toute les 10 secondes
    			vagueLevel(1);
    		}
    		// apres 30 secondes de jeu
    		else if ((nbVague>3) && (nbVague<=6)) {
    			// vague level 2 * 3
    			// 2 zombie de type 1 ou 
    			//1 zombie de type 2 
    			vagueLevel(2);
    		}
    		// apres 1 min de jeu
    		else if ((nbVague >6) && (nbVague<=12)) {
    			// vague level 3 * 6
    			// 3 zombie de type 1 ou 
    			//1 zombie de type 2 et 2 zombie de type 1 ou
    			//1 zombie de type 3 
    			vagueLevel(3);
    		}
    		// apres 2 min de jeu
    		else if ((nbVague>12) && (nbVague<=20)){
    			vagueLevel(4);
    			// avgue level 4 *8
    			// 5 zombie de type 1 ou
    			// 4 zombies de type 1 ou 
    			//3 zombies de type 1 et 1  zombie de type 2 ou
    			// 2 zombie de type 1 et 2 zombie de type 2
    			// ou 1 zombie de type 1 et 3 zombies de type 2 ou
    			// 2 zombies de type 3 et 1 zombie de type 2 ou
    			// 2 zombies de type 3 et 2 zombies de type 1
    		}
    		else {
    			vagueLevel(5);
    		}
    		nbVague++;
    	} 
    	
    	// on genere des items toutes les 13 secondes
    	// 
    	if (timerItem%500==0) {
    		timerItem=0;
    		int posY = (int)(Math.random() * ((largeur - 1) + 1));
    		int posX = (int)(Math.random() * ((longueur - 1) + 1));
    		listeItem.add(new Item(1,20,posX*TAILLE_BLOC,posY*TAILLE_BLOC));
    	}
    	
    	// on incremente le score de 1 toute les demi seconde
    	if (timerScore%50==0) {
    		timerScore=0;
    		score+=1;
    	}	
   }
    
    /**
	* affichage de tous les ennemis
	* @author Ducasse Quentin
	* 
	*/
	public void affichageListeEnnemis(GraphicsContext gc){
		if (listeEnnemi!=null) {
				for(int i=0;i<listeEnnemi.size();i++) {
				listeEnnemi.get(i).afficher(gc);
			}
		}
	}   
    
	/**
	* affichage de tous les alliés
	* @author Ducasse Quentin
	* 
	*/
	
	public void affichageListeAllies(GraphicsContext gc){
		if (listeAllie!=null) {
			for(int i=0;i<listeAllie.size();i++) {
				listeAllie.get(i).affichage(gc);
					listeAllie.get(i).affichage(gc);
			}
		}
	}
	
	/**
	* affichage de toutes les pieces
	* @author Lagahe Hugo
	* 
	*/
	public void affichageListeItems(GraphicsContext gc){
		if (listeItem!=null) {
			for(int j=0;j<listeItem.size();j++) {
				listeItem.get(j).afficherI(gc);
			}
		}	
	}
	
	/**
	* affichage de fin de partie "Game Over"
	* @author Ducasse Quentin
	* 
	*/
	public void affichageFinPartie() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0,longueurFenetre,largeurFenetre+largeurMain);
		gc.setFont(new Font("Arial", 52));
		gc.setFill(Color.GOLD);
		gc.fillText("    GAME OVER",longueurFenetre/4,largeurFenetre/2);
	}
	
	/**
	* affichage du menu du Jeu
	* @author Lagahe Hugo
	* 
	*/
	public void affichageMenu() {
		 try {
				menuImg= new Image(new FileInputStream("wallpaperMenu/dungeon.jpg"));
				gc.drawImage(menuImg,0,-TAILLE_BLOC,longueurFenetre,largeurFenetre+largeurMain+35+2*TAILLE_BLOC);
			} catch (FileNotFoundException erreur) {
				System.out.println("FileNotFoundException"+ erreur.getMessage());
			}
	}
	
	/**
	* affichage du Jeu entier
	* @author Ducasse Quentin, Lagahe Hugo
	* 
	*/
    public void affichageJeu(){
    	gc.setFont(fontDungeon);
		affichagePlateau(gc);
        affichageListeEnnemis(gc);
        affichageListeAllies(gc);
        affichageListeItems(gc);
        // affichage barre du haut
        gc.setFill(Color.DIMGREY);
        gc.fillRect(0,-TAILLE_BLOC,longueur*TAILLE_BLOC,TAILLE_BLOC);
        mainJoueur.affichage(gc);       		
		//La couleur de l'argent qu'il possède
	    gc.setFill(Color.GOLD);	        		
	    gc.fillText("Argent :   "+Integer.toString(money),longueurFenetre-14*TAILLE_BLOC,-TAILLE_BLOC/6);
	    //On affiche le score du joueur
       	//La couleur du score
        gc.drawImage(pauseImg, xPause,yPause,TAILLE_BLOC-TAILLE_BLOC/6,TAILLE_BLOC-TAILLE_BLOC/6);
    	gc.setFill(Color.SPRINGGREEN);
        gc.fillText("Score :   "+Integer.toString(score),longueurFenetre-4*TAILLE_BLOC-TAILLE_BLOC/8,-TAILLE_BLOC/6);
        gc.setFont(fontDungeon);
    }
    /**
	* affichage du menu Pause
	* @author Ducasse Quentin
	* 
	*/
    private void afficherMenuPause() {
    	gc.setFont(new Font("Arial", 52));
    	gc.setFill(Color.GOLD);
    	gc.fillText("         PAUSE ",longueurFenetre/4,largeurFenetre/2);
    	gc.setFont(fontDungeon);
	}

    /**
  	* lance le timer du jeu et donc le jeu
  	* @author Ducasse Quentin
  	* 
  	*/
    public void lancerJeu() {
    	timerJeu = new AnimationTimer() {
	    	@Override
	    	public void handle(long now) {
	    		if (status=="Fin") {
	    			affichageFinPartie();
	    			timerJeu.stop();
	    		} else if (status=="Menu") {
	    				affichageMenu();
	    		} else if (status=="Pause") {
	    			afficherMenuPause();
	    		} else if (status=="Jeu"){
	    			// status du jeu en game
	    			timerSpawn++;
	        		timerScore++;
	        		timerItem++;
	        		update(timerSpawn,timerScore,timerItem);
	        		affichageJeu();
	    		}  
	    	}        	
        };
        timerJeu.start();
    }
    
    /**
     * Lance l'application
     * @param args
     */
	public static void main(String[] args) {
			Application.launch(args);
	}
	
	// methode inimplémenté de Jeu (inutile mais demandée) 
	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}