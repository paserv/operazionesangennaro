package it.multithread.spider.job;

import it.multithread.spider.bean.LinksMap;
import it.queue.RunnableQueue;
import it.spark.spider.utils.NTLMPageDownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SimpleSpiderJobMT_NTLMAuth extends RunnableQueue {

	private String ARTICLE_TAG = "article";
	private String POST_RATINGS_CLASS = "post-ratings";
	private String A_TAG = "a";
	
	private String url;
	private String username;
	private String password;
	private LinksMap links;
	
	public SimpleSpiderJobMT_NTLMAuth(String url, String username, String password, LinksMap links) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.links = links;
	}

	@Override
	public void executeJob() {
		System.out.println("EXPLORING URL -----> " + this.url);

		links.addExploredLink(this.url);
		
		NTLMPageDownloader pd = new NTLMPageDownloader(this.username, this.password);
		
		String pageHtml;
		
		try {
			pageHtml = pd.downloadPage(this.url, "https://intranet.postepernoi.poste/note-legali/");
			Document doc = Jsoup.parse(pageHtml);
			
			/**
			 * SE E' UNA PAGINA CHE CONTIENE LINKS A POST
			 */
			if (doc != null) {
				Elements ratings = doc.getElementsByClass(POST_RATINGS_CLASS);
				if (ratings != null && ratings.size() != 0) {
					System.out.println("PAGE WITH POSTS ---> " + this.url);
					/**
					 * AGGIUNGI TUTTI I LINK AI POST IN ContentLink
					 */
					Elements postLinks = doc.getElementsByTag(ARTICLE_TAG);//TODO Probabilmente basta questa come condizione o altrimenti la presenza di un div con ID "content-1" (Tranne la homepage)
					if (postLinks != null && postLinks.size() != 0) {
						for (Element currPostLink : postLinks) {
							Elements currentATags = currPostLink.getElementsByTag(A_TAG);
							for (Element aTags : currentATags) {
								String currHref = aTags.attr("href");
								if (currHref != null) {
									String cleanedUrl = cleanUrl(currHref);
									if (isAllowedUrl(cleanedUrl)) {
										System.out.println("AGGIUNTO POST LINK: " + cleanedUrl);
										links.addPostLink(cleanedUrl);
									}
								}
							}
						}
					}
					/**
					 * INSERISCI POI NELLE PAGINE DA ESPLORARE LA NEXT PAGE DI POST (SE C'E')
					 */
					Elements pagination = doc.getElementsByClass("pagination");
					if (pagination != null && pagination.size() != 0) {
						Elements pages = pagination.get(0).getAllElements();
						for (int i = 0; i < pages.size(); i++) {
							Element link = pages.get(i);
							String classNamePaginationLink = link.className();
							if (classNamePaginationLink.equalsIgnoreCase("current")) {
								if (i < pages.size() - 1) {
									String nextLinkToExplore = pages.get(i + 1).attr("href");
									if (!links.isAlreadyExplored(nextLinkToExplore) && isAllowedUrl(nextLinkToExplore)) {
										links.addExploredLink(nextLinkToExplore);
										SimpleSpiderJobMT_NTLMAuth newSpider = new SimpleSpiderJobMT_NTLMAuth(nextLinkToExplore, username, password, links);
										newSpider.setName(nextLinkToExplore);
										this.getQueue().addThread(newSpider);
										System.out.println("FOUND NEXT CONTENT LINK ---> " + nextLinkToExplore);
									}
								}
							}
						}
					}
				}

				/**
				 * CERCO POI EVENTUALI ALTRI LINK DA ESPLORARE
				 */
				Elements foundLinks = doc.getElementsByTag(A_TAG);
				for (Element link : foundLinks) {
					String linkHref = link.attr("href");
					String cleanedUrl = cleanUrl(linkHref);
					/*
					 * ASSICURANDOMI CHE SIANO URL CONSENTITI E NON SIANO URL DI POST PURI
					 */
					if (!links.isAlreadyExplored(cleanedUrl) && isAllowedUrl(cleanedUrl) && !links.isPostLink(cleanedUrl)) {
						System.out.println("FOUND LINK TO EXPLORE ---> " + cleanedUrl);
						links.addExploredLink(cleanedUrl);
						SimpleSpiderJobMT_NTLMAuth newSpider = new SimpleSpiderJobMT_NTLMAuth(cleanedUrl, username, password, links);
						newSpider.setName(cleanedUrl);
						this.getQueue().addThread(newSpider);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}

	private String cleanUrl(String currHref) {
		currHref = currHref.trim();
		if (!currHref.endsWith("/")) {
			currHref = currHref + "/";
		}
		String[] oneArray = currHref.split("#");
		if (oneArray.length > 0) {
			currHref = oneArray[0];
		}
		String[] twoArray = currHref.split("\\?");
		if (twoArray.length > 0) {
			currHref = twoArray[0];
		}
		return currHref;
	}

	private boolean isAllowedUrl(String linkHref) {
		return (linkHref.startsWith("https://intranet.postepernoi.poste") && 
				!linkHref.contains("@") && 
				!linkHref.endsWith(".jpg/") && 
				!linkHref.endsWith(".aspx/") && 
				!linkHref.endsWith(".xls/") && 
				!linkHref.endsWith(".png/") && 
				!linkHref.endsWith(".action/") && 
				!linkHref.endsWith(".pdf/") && 
				!linkHref.startsWith("mailto/") && 
				!linkHref.endsWith(".wmv/") && 
				!linkHref.endsWith(".mp4/") && 
				!linkHref.endsWith(".asf/") &&
				!linkHref.endsWith(".bmp/") &&
				!linkHref.endsWith(".zip/") &&
				!linkHref.endsWith(".doc/"));
	}

	
	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}

	

}
