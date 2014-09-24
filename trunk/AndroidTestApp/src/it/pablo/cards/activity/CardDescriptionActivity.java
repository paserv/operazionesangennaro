package it.pablo.cards.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

public class CardDescriptionActivity extends Activity {

	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_card);
	        
	        Intent intent = getIntent();
//	        Bitmap image = intent.getParcelableExtra(MainActivity.PASS_IMAGE);
	        
	        ImageView imgView = (ImageView) findViewById(R.id.image);
//	        imgView.setImageBitmap(image);

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

	
	
}
