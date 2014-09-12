package com.example.androidtestapp;

import android.app.Fragment;
import android.os.Bundle;

public class CardFragment extends Fragment {

	private Card pastCard;
	private Card presCard;
	private Card futuCard;
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }



	public Card getPastCard() {
		return pastCard;
	}



	public Card getPresCard() {
		return presCard;
	}



	public Card getFutuCard() {
		return futuCard;
	}



	public void setPastCard(Card pastCard) {
		this.pastCard = pastCard;
	}



	public void setPresCard(Card presCard) {
		this.presCard = presCard;
	}



	public void setFutuCard(Card futuCard) {
		this.futuCard = futuCard;
	}

	
	
}
