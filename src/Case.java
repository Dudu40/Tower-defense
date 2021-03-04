
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Case {
	private int x;
	private int y;
	private int posAllie,posEnnemi;
	private boolean isMur;
	private boolean select=false;
	
	public Case(int x,int y) {
		this.x=x;
		this.y=y;
		this.isMur=false;
		this.posAllie=-1;

	}

	
	
	@Override
	public boolean equals(Object o) {
		return (this.x==((Case)o).getX() & this.y==((Case)o).getY());
	}
	
	@Override
	public String toString() {
		String mur="";
		if (this.isMur) {
			mur="Mur";
		} else {
			mur="Vide";
		}
		return mur+","+this.x+","+this.y;
	}
	
	public ArrayList<Case> getVoisins() {
		ArrayList<Case> voisins = new ArrayList<Case>();
//		si la voisine au dessus n'est pas hors des limites
		if (this.getY()-1>-1) {
//			si ce n'est pas un mur non plus
			if (!Jeu.plateau[(int)this.getY()-1][(int)this.getX()].isMur) {
//				on l'ajoute a la liste des voisins
				voisins.add(Jeu.plateau[(int)this.getY()-1][(int)this.getX()]);
			}
		}
//		si la voisine en a droite n'est pas hors des limites
		if (this.getX()+1<Jeu.longueur) {
//			si ce n'est pas un mur non plus
			if (!Jeu.plateau[(int)this.getY()][(int)this.getX()+1].isMur) {
//				on l'ajoute a la liste des voisins
				voisins.add(Jeu.plateau[(int)this.getY()][(int)this.getX()+1]);
			}
		}
//		si la voisine a gauche n'est pas hors des limites
		if (this.getX()-1>-1) {
//			si ce n'est pas un mur non plus
			if (!Jeu.plateau[(int)this.getY()][(int)this.getX()-1].isMur) {
//				on l'ajoute a la liste des voisins
				voisins.add(Jeu.plateau[(int)this.getY()][(int)this.getX()-1]);
			}
		}
//		si la voisine en dessous n'est pas hors des limites
		if (this.getY()+1<Jeu.largeur) {
//			si ce n'est pas un mur non plus
			if (!Jeu.plateau[(int)this.getY()+1][(int)this.getX()].isMur) {
//				on l'ajoute a la liste des voisins
				voisins.add(Jeu.plateau[(int)this.getY()+1][(int)this.getX()]);
			}
		}
		return voisins;
	}
	
	//Getter et Setter 
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setSelect(boolean c) {
		select=c;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getPosAllie() {
		return posAllie;
	}
	public void setMur(boolean isMur) {
		this.isMur = isMur;
	}
	public void setPosAllie(int val) {
		this.posAllie=val;
	}
	public boolean isMur() {
		return isMur;
	}

	public boolean isSelect() {
		return select;
	}
	public void isAllie(boolean isAllie) {
		
	}
}
