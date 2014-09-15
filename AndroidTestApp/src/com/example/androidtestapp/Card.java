package com.example.androidtestapp;

import android.graphics.Bitmap;

public class Card {

	private String nomeImmagine;
	private String nomeCarta;
	private String descrizioneCarta;
	private String categoria;
	private String idImg;
	private Bitmap imageBmp;
	
	public String getNomeImmagine() {
		return nomeImmagine;
	}
	public String getNomeCarta() {
		return nomeCarta;
	}
	public String getDescrizioneCarta() {
		return descrizioneCarta;
	}
	public String getCategoria() {
		return categoria;
	}
	public String getIdImg() {
		return idImg;
	}
	public void setNomeImmagine(String nomeImmagine) {
		this.nomeImmagine = nomeImmagine;
	}
	public void setNomeCarta(String nomeCarta) {
		this.nomeCarta = nomeCarta;
	}
	public void setDescrizioneCarta(String descrizioneCarta) {
		this.descrizioneCarta = descrizioneCarta;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public void setIdImg(String idImg) {
		this.idImg = idImg;
	}
	public Bitmap getImageBmp() {
		return imageBmp;
	}
	public void setImageBmp(Bitmap imageBmp) {
		this.imageBmp = imageBmp;
	}
	
	
	
}
