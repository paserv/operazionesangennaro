package it.pablo.cards.activity;

import com.example.androidtestapp.R;
import com.example.androidtestapp.R.id;
import com.example.androidtestapp.R.layout;
import com.example.androidtestapp.R.menu;

import it.pablo.cards.bean.Card;
import it.pablo.cards.task.CallRestService;
import it.pablo.cards.util.Config;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Card pastCard;
//	private Card presCard;
//	private Card futuCard;
	

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
	
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//	}

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
