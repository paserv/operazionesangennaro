package it.pablo.fantagazzetta.utils;

import it.pablo.fantagazzetta.bean.Classifica;
import it.pablo.fantagazzetta.bean.LoginData;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SiteParser {

	private HttpURLConnection conn;
	private List<String> cookies;
	
	public boolean login(LoginData login) {
		if (login.getUsername()!= null && login.getPassword() != null) {
			String page;
			try {
				page = this.getPageContent(Configuration.FANTAGAZZETTA_URL);
				String postParams = getFormParams(page, login.getUsername(), login.getPassword());
				sendPost(Configuration.FANTAGAZZETTA_URL, postParams);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
		
	}
	
	public Classifica getClassifica() {
		Classifica result = new Classifica();
		String html;
		try {
			html = getPageContent(Configuration.CLASSIFICA_URL);
			Document doc = Jsoup.parse(html);
			for (Element table : doc.select("table[id=tbclassificaA]")) {
				for (Element row : table.select("tr")) {
					Elements tds = row.select("td");
					if (tds != null && tds.size() > 0) {
						result.setPosizione(Integer.valueOf(tds.get(0).text()));
						result.setNomeSquadra(tds.get(1).text());
						result.setPunti(Integer.valueOf(tds.get(2).text()));
						result.setGiocate(Integer.valueOf(tds.get(3).text()));
						result.setVinte(Integer.valueOf(tds.get(4).text()));
						result.setNulle(Integer.valueOf(tds.get(5).text()));
						result.setPerse(Integer.valueOf(tds.get(6).text()));
						result.setGolFatti(Integer.valueOf(tds.get(7).text()));
						result.setGolSubiti(Integer.valueOf(tds.get(8).text()));
						result.setDifferenzaReti(Integer.valueOf(tds.get(9).text()));
						result.setTotale(Float.valueOf(tds.get(10).text()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	
	private void sendPost(String url, String postParams) throws Exception {

		URL obj = new URL(url);
		conn = (HttpURLConnection) obj.openConnection();

		// Acts like a browser
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Host", "accounts.google.com");
		conn.setRequestProperty("User-Agent", Configuration.USER_AGENT);
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		for (String cookie : this.cookies) {
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		}
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Referer", "https://accounts.google.com/ServiceLoginAuth");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

		conn.setDoOutput(true);
		conn.setDoInput(true);

		// Send post request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + postParams);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = 
				new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		// System.out.println(response.toString());

	}

	private String getPageContent(String url) throws Exception {
		URL obj = new URL(url);
		conn = (HttpURLConnection) obj.openConnection();

		// default is GET
		conn.setRequestMethod("GET");

		conn.setUseCaches(false);

		// act like a browser
		conn.setRequestProperty("User-Agent", Configuration.USER_AGENT);
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if (cookies != null) {
			for (String cookie : this.cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = 
				new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// Get the response cookies
		setCookies(conn.getHeaderFields().get("Set-Cookie"));

		return response.toString();

	}


	public String getFormParams(String html, String username, String password)
			throws UnsupportedEncodingException {

		System.out.println("Extracting form's data...");

		Document doc = Jsoup.parse(html);

		// Google form id
		Element loginform = doc.getElementById("formMainMaster");
		Elements inputElements = loginform.getElementsByTag("input");
		List<String> paramList = new ArrayList<String>();
		for (Element inputElement : inputElements) {
			String key = inputElement.attr("name");
			String value = inputElement.attr("value");

			if (key.equals("Email"))
				value = username;
			else if (key.equals("Passwd"))
				value = password;
			paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
		}

		// build parameters list
		StringBuilder result = new StringBuilder();
		for (String param : paramList) {
			if (result.length() == 0) {
				result.append(param);
			} else {
				result.append("&" + param);
			}
		}
		return result.toString();
	}


	public List<String> getCookies() {
		return cookies;
	}

	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	}

	
	
}
