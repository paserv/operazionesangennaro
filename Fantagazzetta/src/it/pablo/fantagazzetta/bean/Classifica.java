package it.pablo.fantagazzetta.bean;

import java.util.ArrayList;
import java.util.List;

public class Classifica {

	private List<ClassificaItem> items;

	public Classifica() {
		items = new ArrayList<Classifica.ClassificaItem>();
	}
	
	public class ClassificaItem {
		int posizione;
		String nomeSquadra;
		int punti;
		int giocate;
		int vinte;
		int nulle;
		int perse;
		int golFatti;
		int golSubiti;
		int differenzaReti;
		float totale;

		public ClassificaItem() {
			
		}
		
		public ClassificaItem(int posizione, String nomeSquadra, int punti,
				int giocate, int vinte, int nulle, int perse, int golFatti,
				int golSubiti, int differenzaReti, float totale) {
			super();
			this.posizione = posizione;
			this.nomeSquadra = nomeSquadra;
			this.punti = punti;
			this.giocate = giocate;
			this.vinte = vinte;
			this.nulle = nulle;
			this.perse = perse;
			this.golFatti = golFatti;
			this.golSubiti = golSubiti;
			this.differenzaReti = differenzaReti;
			this.totale = totale;
		}

		
		@Override
		public String toString() {
			return this.nomeSquadra + " " + this.posizione;
		}

		public int getPosizione() {
			return posizione;
		}

		public String getNomeSquadra() {
			return nomeSquadra;
		}

		public int getPunti() {
			return punti;
		}

		public int getGiocate() {
			return giocate;
		}

		public int getVinte() {
			return vinte;
		}

		public int getNulle() {
			return nulle;
		}

		public int getPerse() {
			return perse;
		}

		public int getGolFatti() {
			return golFatti;
		}

		public int getGolSubiti() {
			return golSubiti;
		}

		public int getDifferenzaReti() {
			return differenzaReti;
		}

		public float getTotale() {
			return totale;
		}

		public void setPosizione(int posizione) {
			this.posizione = posizione;
		}

		public void setNomeSquadra(String nomeSquadra) {
			this.nomeSquadra = nomeSquadra;
		}

		public void setPunti(int punti) {
			this.punti = punti;
		}

		public void setGiocate(int giocate) {
			this.giocate = giocate;
		}

		public void setVinte(int vinte) {
			this.vinte = vinte;
		}

		public void setNulle(int nulle) {
			this.nulle = nulle;
		}

		public void setPerse(int perse) {
			this.perse = perse;
		}

		public void setGolFatti(int golFatti) {
			this.golFatti = golFatti;
		}

		public void setGolSubiti(int golSubiti) {
			this.golSubiti = golSubiti;
		}

		public void setDifferenzaReti(int differenzaReti) {
			this.differenzaReti = differenzaReti;
		}

		public void setTotale(float totale) {
			this.totale = totale;
		}

	}


	public List<ClassificaItem> getItems() {
		return items;
	}


	public void setItems(List<ClassificaItem> items) {
		this.items = items;
	}

	public void addItem(ClassificaItem item) {
		if (this.items != null) {
			items.add(item);
		}
	}
	
	@Override
	public String toString() {
		String result = "";
		for (ClassificaItem curr : items) {
			result = result + curr.toString() + "\n";
		}
		return result;
	}

}
