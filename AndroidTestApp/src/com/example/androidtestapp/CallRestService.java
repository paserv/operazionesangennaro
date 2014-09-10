package com.example.androidtestapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import org.apache.commons.io.IOUtils;
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
				
//		String imageURL = Configuration.IMAGES_ROOT_URL + extractedImage;
		String imageURL = "https://drive.google.com/file/d/0B5FZC7m0E5ixNUtGZzFhOER3a3M/edit?usp=sharing";
		
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
		
//		System.getProperties().put("http.proxyHost", Configuration.HTTP_PROXY_HOST);
//		System.getProperties().put("http.proxyPort", Configuration.HTTP_PROXY_PORT);
//		System.getProperties().put("http.proxyUser", Configuration.DOMAIN + "\\" + Configuration.HTTP_PROXY_USER);
//		System.getProperties().put("http.proxyPassword", Configuration.HTTP_PROXY_PASSWORD);
		
//		Integer proxyport = Integer.valueOf(Configuration.HTTP_PROXY_PORT);
//		InetSocketAddress inetSocketAddress = new InetSocketAddress(
//				Configuration.HTTP_PROXY_HOST, proxyport);
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, inetSocketAddress);
//		Authenticator authenticator = new Authenticator() {
//			public PasswordAuthentication getPasswordAuthentication() {
//				char[] proxypasswd = Configuration.HTTP_PROXY_PASSWORD
//						.toCharArray();
//				String doman_user = Configuration.DOMAIN + "\\"
//						+ Configuration.HTTP_PROXY_USER;
//				PasswordAuthentication passwordAuthentication = new PasswordAuthentication(
//						doman_user, proxypasswd);
//				return passwordAuthentication;
//			}
//		};
//		Authenticator.setDefault(authenticator);
//		
//		URLConnection conn;
//		try {
//			conn = new URL(imageUrl).openConnection(proxy);
//			conn.connect();
//			InputStream in = conn.getInputStream();
//
//			StringWriter writer = new StringWriter();
//			IOUtils.copy(in, writer);
//			String resultString = writer.toString();
//			
//			System.out.println(resultString);
//			System.out.println("URL: " + conn.getURL());
//		} catch (MalformedURLException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		} catch (IOException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		
		
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
