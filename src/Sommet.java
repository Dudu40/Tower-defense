public class Sommet{
		private Case pere;
		private boolean check;
		private int poids;
		
		/**
		 * Constructeur sommet
		 */
		public Sommet() {
			this.pere=null;
			this.poids=Jeu.longueur*Jeu.largeur;
			this.check=false;
		}
		
		//Getter et Setter
		public Case getPere() {
			return pere;
		}
		public void setPere(Case pere) {
			this.pere = pere;
		}
		public boolean isCheck() {
			return check;
		}
		public void setCheck(boolean check) {
			this.check = check;
		}
		public int getPoids() {
			return poids;
		}
		public void setPoids(int poids) {
			this.poids = poids;
		}
	}