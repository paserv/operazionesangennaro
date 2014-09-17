package it.pablo.fantagazzetta.task;

import it.pablo.fantagazzetta.bean.Classifica;
import it.pablo.fantagazzetta.utils.SiteParser;
import android.os.AsyncTask;
import android.widget.TextView;


public class GetClassifica extends AsyncTask<String, Integer, String> {

	TextView text;
	
	public GetClassifica(TextView text) {
		this.text = text;
	}

	@Override
	protected String doInBackground(String... params) {
		SiteParser sp = new SiteParser();
		Classifica result = sp.getClassifica();
		return result.toString();
	}

	protected void onPostExecute(String result) {
		text.setText(result);
	}
	
}
