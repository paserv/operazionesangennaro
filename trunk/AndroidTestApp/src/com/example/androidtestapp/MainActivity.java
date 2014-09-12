package com.example.androidtestapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private CardFragment fragment;
	
	private Card pastCard;
	private Card presCard;
	private Card futuCard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fm = getFragmentManager();
		fragment = (CardFragment) fm.findFragmentByTag("appstate");
		
		ImageView pastImg = (ImageView) findViewById(R.id.passatoImage);
		ImageView presImg = (ImageView) findViewById(R.id.presenteImage);
		ImageView futuImg = (ImageView) findViewById(R.id.futuroImage);
		
		if (fragment != null) {
			
			if (fragment.getPastCard() != null) {
				pastCard = fragment.getPastCard();
				pastImg.setImageBitmap(pastCard.getImageBmp());
			}
			
			if (fragment.getPresCard() != null) {
				presCard = fragment.getPresCard();
				presImg.setImageBitmap(presCard.getImageBmp());
			}
			
			if (fragment.getFutuCard() != null) {
				futuCard = fragment.getFutuCard();
				futuImg.setImageBitmap(futuCard.getImageBmp());
			}
			
		} else {
			fragment = new CardFragment();
			fm.beginTransaction().add(fragment, "appstate").commit();
			pastCard = new Card();
			presCard = new Card();
			futuCard = new Card();
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (pastCard != null) {
			fragment.setPastCard(pastCard);
		}
		if (presCard != null) {
			fragment.setPresCard(presCard);
		}
		if (futuCard != null) {
			fragment.setFutuCard(futuCard);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void passato (View v) {
		ImageButton pastBtn = (ImageButton) findViewById(R.id.passatoButton);
		ImageView pastImg = (ImageView) findViewById(R.id.passatoImage);
		new CallRestService(this, pastBtn, pastImg, pastCard).execute(new String[]{Configuration.PAST_PARAM});
	}
	
	public void presente (View v) {
		ImageButton presBtn = (ImageButton) findViewById(R.id.presenteButton);
		ImageView presImg = (ImageView) findViewById(R.id.presenteImage);
		new CallRestService(this, presBtn, presImg, presCard).execute(new String[]{Configuration.PRES_PARAM});
	}
	
	public void futuro (View v) {
		ImageButton futuBtn = (ImageButton) findViewById(R.id.futuroButton);
		ImageView futuImg = (ImageView) findViewById(R.id.futuroImage);
		new CallRestService(this, futuBtn, futuImg, futuCard).execute(new String[]{Configuration.FUTU_PARAM});
	}
	

}
