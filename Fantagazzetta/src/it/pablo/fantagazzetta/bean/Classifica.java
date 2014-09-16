package it.pablo.fantagazzetta.bean;

public class Classifica {

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
	
	public int getPosizione() {
		return posizione;
	}
	public void setPosizione(int posizione) {
		this.posizione = posizione;
	}
	public String getNomeSquadra() {
		return nomeSquadra;
	}
	public void setNomeSquadra(String nomeSquadra) {
		this.nomeSquadra = nomeSquadra;
	}
	public int getPunti() {
		return punti;
	}
	public void setPunti(int punti) {
		this.punti = punti;
	}
	public int getGiocate() {
		return giocate;
	}
	public void setGiocate(int giocate) {
		this.giocate = giocate;
	}
	public int getVinte() {
		return vinte;
	}
	public void setVinte(int vinte) {
		this.vinte = vinte;
	}
	public int getNulle() {
		return nulle;
	}
	public void setNulle(int nulle) {
		this.nulle = nulle;
	}
	public int getPerse() {
		return perse;
	}
	public void setPerse(int perse) {
		this.perse = perse;
	}
	public int getGolFatti() {
		return golFatti;
	}
	public void setGolFatti(int golFatti) {
		this.golFatti = golFatti;
	}
	public int getGolSubiti() {
		return golSubiti;
	}
	public void setGolSubiti(int golSubiti) {
		this.golSubiti = golSubiti;
	}
	public int getDifferenzaReti() {
		return differenzaReti;
	}
	public void setDifferenzaReti(int differenzaReti) {
		this.differenzaReti = differenzaReti;
	}
	public float getTotale() {
		return totale;
	}
	public void setTotale(float totale) {
		this.totale = totale;
	}
	
	@Override
	public String toString() {
		return this.nomeSquadra + " " + this.posizione;
	}
	
}
