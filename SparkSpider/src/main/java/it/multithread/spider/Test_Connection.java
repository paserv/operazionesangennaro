package it.multithread.spider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

public class Test_Connection {

	private static HttpClient httpClient;
	private static String PROXY_HOST = "smwauthpi.postepernoi.poste";
	private static int PROXY_PORT = 443;
	private static String PROXY_USERNAME = "servill7";
	private static String PROXY_PASSWORD = "Paolos20";
	private static String PROXY_DOMAIN = "rete";
	private static HttpHost PROXY = new HttpHost(PROXY_HOST, PROXY_PORT);
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
	private static final PrintWriter pw = new PrintWriter(System.out, true);
	private static final String HOST = "intranet.postepernoi.poste";

	public static void main(String[] args) throws Exception {
		httpClient = HttpClientBuilder
				.create()
				.setRedirectStrategy(new LaxRedirectStrategy())
				.setDefaultCookieStore(new BasicCookieStore())
				.setSSLSocketFactory(builConnectionSocketFactory())
				.setDefaultCredentialsProvider(getProxyAuthCredentialsProvider())
				.build();
		String pageContent = doGetAsString("https://intranet.postepernoi.poste");
		System.out.println(pageContent);
		
		pageContent = doGetAsString("https://intranet.postepernoi.poste/aziendanews-numero-31-dicembre-2014/");
		System.out.println(pageContent);
		
		pageContent = doGetAsString("https://intranet.postepernoi.poste/iniziative-per-noi/");
		System.out.println(pageContent);
		
	}

	private static CredentialsProvider getProxyAuthCredentialsProvider()
			throws UnknownHostException {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(
				new AuthScope(PROXY_HOST , PROXY_PORT , AuthScope.ANY_REALM, "ntlm"),
				new NTCredentials(PROXY_USERNAME , PROXY_PASSWORD ,
						InetAddress.getLocalHost().getHostName(), PROXY_DOMAIN ));
		return credentialsProvider;
	}

	private static SSLConnectionSocketFactory builConnectionSocketFactory()
			throws Exception {
		SSLContext sslcontext = SSLContexts.custom()
				.loadTrustMaterial(null, new TrustStrategy() {
					@Override
					public boolean isTrusted(final X509Certificate[] chain,
							final String authType) throws CertificateException {
						return true;
					}
				}).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return sslsf;
	}
	
	
	private static void setProxyHost(HttpRequestBase request) {
		RequestConfig config = RequestConfig.custom().build();
		request.setConfig(config);
	}
	
	public static String doGetAsString(String url) throws Exception {
		pw.printf("Sending 'GET' request to URL : %s\n", url);

		HttpGet request = new HttpGet(url);
		setProxyHost(request);
		request.setHeader("User-Agent", USER_AGENT);
		request.setHeader("Accept-Encoding", "gzip, deflate");
		request.setHeader("Connection", "keep-alive");
		request.setHeader("DNT", "1");
		request.setHeader("Host", HOST);
		request.setHeader("Accept",
"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		request.setHeader("Accept-Language", "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3");

		HttpResponse response = httpClient.execute(request);
		int responseCode = response.getStatusLine().getStatusCode();

		pw.printf("Response Code : %d\n", responseCode);

		long readStart = System.currentTimeMillis();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null)
			result.append(String.format("%s\n", line));

		long readEnd = System.currentTimeMillis();

		pw.printf("\n%d ms reading from the InterNet...\n", readEnd - readStart);

		return result.toString();
	}

}
