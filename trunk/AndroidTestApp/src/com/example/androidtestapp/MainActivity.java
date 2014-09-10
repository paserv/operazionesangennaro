package com.example.androidtestapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		new CallRestService(pastBtn, pastImg).execute(new String[]{Configuration.PAST_PARAM});
	}
	
	public void presente (View v) {
		ImageButton presBtn = (ImageButton) findViewById(R.id.presenteButton);
		ImageView presImg = (ImageView) findViewById(R.id.presenteImage);
		new CallRestService(presBtn, presImg).execute(new String[]{Configuration.PRES_PARAM});
	}
	
	public void futuro (View v) {
		ImageButton futuBtn = (ImageButton) findViewById(R.id.futuroButton);
		ImageView futuImg = (ImageView) findViewById(R.id.futuroImage);
		new CallRestService(futuBtn, futuImg).execute(new String[]{Configuration.FUTU_PARAM});
	}
	

}
