package com.example.androidtestapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CallRestService extends AsyncTask<String, Integer, Card> {

	private ImageButton btn;
	private ImageView img;
	Card card;

	private ProgressDialog pd;
	private Context context;


	public CallRestService(MainActivity mainActivity, ImageButton btn, ImageView img, Card currCard) {
		this.btn = btn;
		this.img = img;
		this.card = currCard;
		context = mainActivity;
	}


	protected void onPreExecute() {
		btn.setEnabled(false);
		pd = new ProgressDialog(context);
		pd.setTitle("Choosing card...");
		pd.setMessage("Please wait.");
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		pd.show();
	}

	@Override
	protected Card doInBackground(String... params) {
		card = getCardImageURL(params[0]);
		String imageURL = Configuration.IMAGE_ROOT_URL + card.getIdImg() + Configuration.IMAGE_URL_TAIL;
		Bitmap bmp = getBitmapFromURL(imageURL);
		card.setImageBmp(bmp);
		return card;
	}

	protected void onPostExecute(Card result) {
		img.setImageBitmap(result.getImageBmp());
		btn.setEnabled(true);
		if (pd!=null) {
			pd.dismiss();
		}
	}


	private Card getCardImageURL(String param) {

		String resultString = "";
//		Card extractedCard = new Card();
		DefaultHttpClient request = new DefaultHttpClient();
		HttpGet get = new HttpGet(Configuration.SERVICE_ROOT_URL + param);
		HttpResponse response;
		try {
			response = request.execute(get);
			InputStream is = response.getEntity().getContent();
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			String s = null;
			StringBuffer sb = new StringBuffer();

			while((s=r.readLine())!=null) {
				sb.append(s);
			}

			resultString = sb.toString();

			if (!resultString.equalsIgnoreCase("")) {
				try {
					JSONObject jsonObj = new JSONObject(resultString);
					String nomeImg = jsonObj.getString("nomeImg");
					String nomeCarta = jsonObj.getString("nomeCarta");
					String descrizCarta = jsonObj.getString("descrizCarta");
					String categoria = jsonObj.getString("categoria");
					String url = jsonObj.getString("url");
					card.setNomeImmagine(nomeImg);
					card.setNomeCarta(nomeCarta);
					card.setDescrizioneCarta(descrizCarta);
					card.setCategoria(categoria);
					card.setIdImg(url);
					return card;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return card;
	}

	private Bitmap getBitmapFromURL(String imageUrl) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(imageUrl);

		try {
			HttpResponse response = client.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream inputStream = null;
				try {
					// getting contents from the stream 
					inputStream = entity.getContent();

					// decoding stream data back into image Bitmap that android understands
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			getRequest.abort();
		} 
		return null;
	}

}
