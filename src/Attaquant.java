
public class Attaquant {
	protected int Id;
	protected double x;
	protected double y;
	protected int hp;
	protected int attaque;
	protected int vitesseAttaque;
	
	public Attaquant(int Id, double X, double Y, int HP, int Attaque, int VitesseAttaque) {
		this.Id = Id;
		this.x = X;
		this.y = Y;
		this.hp = HP;
		this.attaque = Attaque;
		this.vitesseAttaque = VitesseAttaque;
	}

	public double getX() {
		return(x);
	}
	
	public void setX(double X) {
		x = X;
	}
	
	public double getY() {
		return(y);
	}
	
	public int getId() {
		return(Id);
	}
	
	public void setY(double Y) {
		y = Y;
	}
	
	public int getHp(){
		return hp;
	}
	
	public void setHp(int a) {
		hp=a;
	}
	
	public int getAttaque(){
		return attaque;
	}
	
	public void setAttaque(int a) {
		attaque=a;
	}
	
	public int getVitesseAttaque(){
		return vitesseAttaque;
	}
		
	
}
