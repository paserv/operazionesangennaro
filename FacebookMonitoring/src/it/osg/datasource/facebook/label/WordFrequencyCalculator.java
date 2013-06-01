package it.osg.datasource.facebook.label;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PagableList;
import facebook4j.Paging;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import it.osg.datasource.SourceGenerator;
import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;
import it.osg.utils.OrderComparator;
import it.pipe.core.PipeBlock;
import it.pipe.core.PipelineEngine;
import it.pipe.filters.RemoveRegex;
import it.pipe.filters.RemoveWordList;
import it.pipe.transformers.DummyTransformer;
import it.pipe.transformers.FrequencyTransformer;
import it.pipe.transformers.Tokenizer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class WordFrequencyCalculator extends SourceGenerator {

	@Override
	public ArrayList<Graph> getGraphData(Object[] objects) {

		ArrayList<Graph> result = new ArrayList<Graph>();

		//GET INPUT PARAMETERS
		String transmissionName = (String) objects[0];
		int limit = Integer.valueOf((String) objects[3]);
		Date f = null;
		Date t = null;
		try {
			if (objects[1] != null && objects[2] != null) {
				f = DateUtils.parseDateAndTime((String) objects[1]);
				t = DateUtils.parseDateAndTime((String)objects[2]);
			}


			//Istanzio e configuro il pipeline engine
			PipelineEngine eng = new PipelineEngine();
			ArrayList<PipeBlock> blocks = new ArrayList<PipeBlock>();
			Tokenizer block1 = new Tokenizer("Tokenizer", null);
			blocks.add(block1);
			RemoveWordList block2 = new RemoveWordList("RemoveWordList", null);
			block2.addConfiguration("wordListPath", "stopwords_it.csv");
			blocks.add(block2);
			RemoveRegex block3 = new RemoveRegex("RemoveRegex", null);
			block3.addConfiguration("regex1", "^[0-9]+");
			block3.addConfiguration("regex2", "(\\d+)(\\.)(\\d+)");
			blocks.add(block3);
			FrequencyTransformer block4 = new FrequencyTransformer("FrequencyTransformer", null);
			blocks.add(block4);

			ArrayList<String> input = getFacebookCommentsAndPost(transmissionName, f, t);
			//prendo il risultato della pipe
			ArrayList<String> pipeResult = eng.run(blocks, input);

			//Costruisco il grafo a partire dal risultato della pipe
			for(int i = 0; i < pipeResult.size(); i++) {
				String currOccurrence = pipeResult.get(i);
				String[] splitted = currOccurrence.split(",");
				Graph currGraph = new Graph(splitted[0], Long.valueOf(splitted[1]));
				result.add(currGraph);
			}

			Collections.sort(result,new OrderComparator());

		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (result.size() > limit) {
			ArrayList<Graph> subList = new ArrayList<Graph>();
			for (int i = 0; i < limit; i++) {
				Graph currGraph = result.get(i);
				subList.add(currGraph);
			}
			return subList;
		}

		return result;

	}

	private ArrayList<String> getFacebookCommentsAndPost(String transmissionName, Date f, Date t) {

		ArrayList<String> result = new ArrayList<String>();

		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId("156346967866710", "e0f880cc248e811c98952d9a44a27ce4");
		//facebook.setOAuthPermissions(commaSeparetedPermissions);
		facebook.setOAuthAccessToken(new AccessToken("156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk", null));

		try {
			ResponseList<Post> facResults = facebook.getFeed(transmissionName, new Reading().since(f).until(t));
			Iterator<Post> iter = facResults.iterator();
			//Salvo i Post e relativi commenti
			while (iter.hasNext()){
				result.addAll(downloadPostWithComments(iter.next(), facebook, f, t));		
			}
			//Itero su tutti i post
			Paging<Post> pagingPost = facResults.getPaging();
			while (true) {
				if (pagingPost != null) {
					ResponseList<Post> nextPosts = facebook.fetchNext(pagingPost);
					if (nextPosts != null) {
						Post firstPost = nextPosts.get(0);
						if (firstPost.getCreatedTime().after(t) || firstPost.getCreatedTime().before(f)) {
							break;
						}
						Iterator<Post> itr = nextPosts.iterator();
						while (itr.hasNext()) {
							result.addAll(downloadPostWithComments(itr.next(), facebook, f, t));
						}
					} else {
						break;
					}
					pagingPost = nextPosts.getPaging();
				} else {
					break;
				}
			}

		} catch (FacebookException e) {
			e.printStackTrace();
		}

		return result;
	}

	private ArrayList<String> downloadPostWithComments(Post curr, Facebook facebook, Date f, Date t) {

		ArrayList<String> result = new ArrayList<String>();

		if (curr.getCreatedTime().after(f) && curr.getCreatedTime().before(t)) {
			String currText = curr.getMessage();
			if (currText != null) {
				result.add(currText);
				System.out.println(currText);
				System.out.println(curr.getId());
				System.out.println(curr.getCreatedTime());
			}

			PagableList<Comment> comments = curr.getComments();
			if (comments != null) {
				Iterator<Comment> iterComment = comments.iterator();
				//Salvo i Commenti
				while (iterComment.hasNext()) {
					Comment currComment = iterComment.next();
					String currCommentMessage = currComment.getMessage();
					if (currCommentMessage != null) {
						result.add(currCommentMessage);
					}
					//TODO salvare le risposte ai commenti

				}
				//Itero sulla paginazione per salvare tutti i commenti
				Paging<Comment> paging = comments.getPaging();
				while (true) {
					if (paging != null) {
						ResponseList<Comment> nextPage;
						try {
							nextPage = facebook.fetchNext(paging);
							if (nextPage != null) {
								Iterator<Comment> itr = nextPage.iterator();
								while (itr.hasNext()) {
									Comment cmt = itr.next();
									String curCmt = cmt.getMessage();
									if (curCmt != null) {
										result.add(curCmt);
									}
								}
							} else {
								break;
							}
							paging = nextPage.getPaging();
						} catch (FacebookException e) {
							e.printStackTrace();
						}
					} else {
						break;
					}

				}
			}

		}
		
		
		return result;



	}


}
