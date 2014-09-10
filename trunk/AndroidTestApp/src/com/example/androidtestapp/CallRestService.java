package com.example.androidtestapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CallRestService extends AsyncTask<String, Integer, Bitmap> {

	private ImageButton btn;
	private ImageView img;
	
	
	
	public CallRestService(ImageButton btn, ImageView img) {
		this.btn = btn;
		this.img = img;
	}

	protected void onPreExecute() {
		btn.setEnabled(false);
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
				
		String extractedImage = getCardImageURL(params[0]);
				
		String imageURL = Configuration.IMAGES_ROOT_URL + extractedImage;
		
		return getBitmapFromURL(imageURL);
	}
	
	protected void onPostExecute(Bitmap result) {
		img.setImageBitmap(result);
		btn.setEnabled(true);
	  }
	
	
	private String getCardImageURL(String param) {
		
		String resultString = "error.png";
		
//		DefaultHttpClient request = new DefaultHttpClient();
//		HttpGet get = new HttpGet(Configuration.SERVICE_ROOT_URL + param);
//		HttpResponse response;
//		try {
//			response = request.execute(get);
//			InputStream is = response.getEntity().getContent();
//			BufferedReader r = new BufferedReader(new InputStreamReader(is));
//			String s = null;
//			StringBuffer sb = new StringBuffer();
//			
//			while((s=r.readLine())!=null) {
//			    sb.append(s);
//			}
//			
//			resultString = sb.toString();
//			
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return resultString;
		
		Random rnd = new Random(System.currentTimeMillis());
		int intero = rnd.nextInt(2);
		
		if (intero == 0) {
			return "le_diable.png";
		}
		
		return "la_maison_diev.png";
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
