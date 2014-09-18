package it.pablo.cards.activity;

import it.pablo.cards.task.CallRestService;
import it.pablo.cards.util.CardFragment;
import it.pablo.cards.util.Config;
import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final String ACTIVITY_STATUS_FRAGMENT = "activity_status";
	private CardFragment myFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FragmentManager fm = getFragmentManager();
		myFragment = (CardFragment) fm.findFragmentByTag(ACTIVITY_STATUS_FRAGMENT);
		if (myFragment == null) {
			myFragment = new CardFragment();
			fm.beginTransaction().add(myFragment, ACTIVITY_STATUS_FRAGMENT).commit();
		}

		if (myFragment.getPastImage() != null) {
			ImageView img = (ImageView) findViewById(R.id.passatoImage);
			img.setImageBitmap(myFragment.getPastImage());
		}
		if (myFragment.getPresImage() != null) {
			ImageView img = (ImageView) findViewById(R.id.presenteImage);
			img.setImageBitmap(myFragment.getPresImage());
		}
		if (myFragment.getFutuImage() != null) {
			ImageView img = (ImageView) findViewById(R.id.futuroImage);
			img.setImageBitmap(myFragment.getFutuImage());
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
	
	@Override
	public void onDestroy() {		
		ImageView imgPast = (ImageView) findViewById(R.id.passatoImage);
		ImageView imgPres = (ImageView) findViewById(R.id.presenteImage);
		ImageView imgFutu = (ImageView) findViewById(R.id.futuroImage);
		
		if (imgPast.getDrawable() != null) {
			Bitmap bitmapPast = ((BitmapDrawable) imgPast.getDrawable()).getBitmap();
			myFragment.setPastImage(bitmapPast);
		}
		
		if (imgPres.getDrawable() != null) {
			Bitmap bitmapPres = ((BitmapDrawable) imgPres.getDrawable()).getBitmap();
			myFragment.setPresImage(bitmapPres);
		}
		
		if (imgFutu.getDrawable() != null) {
			Bitmap bitmapFutu = ((BitmapDrawable) imgFutu.getDrawable()).getBitmap();
			myFragment.setFutuImage(bitmapFutu);
		}
		
		super.onDestroy();
	}
	

	public void passato (View v) {
		ImageButton pastBtn = (ImageButton) findViewById(R.id.passatoButton);
		ImageView pastImg = (ImageView) findViewById(R.id.passatoImage);
		new CallRestService(this, pastBtn, pastImg).execute(new String[]{Config.PAST_PARAM});
	}
	
	public void presente (View v) {
		ImageButton presBtn = (ImageButton) findViewById(R.id.presenteButton);
		ImageView presImg = (ImageView) findViewById(R.id.presenteImage);
		new CallRestService(this, presBtn, presImg).execute(new String[]{Config.PRES_PARAM});
	}
	
	public void futuro (View v) {
		ImageButton futuBtn = (ImageButton) findViewById(R.id.futuroButton);
		ImageView futuImg = (ImageView) findViewById(R.id.futuroImage);
		new CallRestService(this, futuBtn, futuImg).execute(new String[]{Config.FUTU_PARAM});
	}
	

}
