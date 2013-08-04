package test;

import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;

public class BaseInfoStandalone {

	public static void main(String[] args) {
		
		System.out.println("ID,Nome,Fan,TalkAbout,FirstPost,LastPost");
		ArrayList<String> input = new ArrayList<String>();
		input.add("63360159216");
				input.add("390316297742803");
				input.add("112352038802143");
				input.add("68340403104");
				input.add("174668952677211");
				input.add("489219834450153");
				input.add("434003210024874");
				input.add("206379842757683");
				input.add("120577671459080");
				input.add("354028671345304");
				input.add("56221866936");
				input.add("192356270892210");
				input.add("364947573545455");
				input.add("133810866691494");
				input.add("61657367059");
				input.add("175061545874587");
				input.add("53388213256");
				input.add("243230429056389");
				input.add("118830804861699");
				input.add("173780875979272");
				input.add("161427010581901");
				input.add("258372870902870");
				input.add("160694770110");
				input.add("313671835390305");
				input.add("128729313852232");
				input.add("53505098083");
				input.add("376095029171963");
				input.add("36630618044");
				input.add("120435464635776");
				input.add("250216921658628");
				input.add("534334929944459");
				input.add("164305146951493");
				input.add("278376212226141");
				input.add("52932694036");
				input.add("118672271588789");
				input.add("70036951368");
				input.add("31770497795");
				input.add("354956144525858");
				input.add("129919073737485");
				input.add("49423676393");
				input.add("343365372377122");
				input.add("149864881742810");
				input.add("110407065829834");
				input.add("422625477753021");
				input.add("526504600745221");
				input.add("49957446932");
				input.add("469857859741998");
				input.add("109633709120808");
				input.add("207112332753505");
				input.add("234736036582930");
				input.add("486046941469232");
				input.add("529405893749379");
				input.add("113335124914");
				input.add("270956182996934");
				input.add("457562410985448");
				input.add("235766926498784");
				input.add("327284627283068");
				input.add("121112761293626");
				input.add("200303126695453");
				input.add("180764825332055");
				input.add("598261373517812");
				input.add("352604201451113");
				input.add("420895527988113");
				input.add("478212418874107");
				input.add("32377918793");
				input.add("553261914706917");
				
				Iterator<String> iter = input.iterator();
				while (iter.hasNext()) {
					String curr = iter.next();
					
					Date startDate = null;
					Date endDate = null;
					Date f = null;
					Date t = null;

					Date startFB = null;
					try {
						startFB = DateUtils.parseDateAndTime("01-02-2004 00:00:00");
					} catch (ParseException e) {
						e.printStackTrace();
					}
					

					
					//FIRST POST
					f = startFB;
					t = DateUtils.addMonthToDate(f, 1);
					try {
						while (true) {
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							ResponseList<Post> facResults = FacebookUtils.getFB().getFeed(curr, new Reading().since(f).until(t).fields("created_time").limit(1));
							if (facResults != null && facResults.size() != 0) {
								Post currPost = facResults.get(0);
								startDate = currPost.getCreatedTime();
								break;
							} else if (DateUtils.diffInDay(t, DateUtils.getNowDate()) > 0) {
								f = t;
								t = DateUtils.addMonthToDate(f, 1);
							} else if (DateUtils.diffInDay(t, DateUtils.getNowDate()) < 0) {
								try {
									startDate =  DateUtils.parseDate("01-01-1970");
								} catch (ParseException e) {
									e.printStackTrace();
								};
								break;
							}
						}


					} catch (FacebookException e) {
						e.printStackTrace();
					}

					
					
					
					//LAST POST
					t = DateUtils.getNowDate();
					f = DateUtils.addMonthToDate(t, -1);
					
					while (true) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						ResponseList<Post> facResults2;
						try {
							facResults2 = FacebookUtils.getFB().getFeed(curr, new Reading().since(f).until(t).fields("created_time").limit(1));
							if (facResults2 != null && facResults2.size() != 0) {
								Post currPost = facResults2.get(0);
								endDate = currPost.getCreatedTime();
								break;
							} else if (DateUtils.diffInDay(startFB, f) > 0) {
								t = f;
								f = DateUtils.addMonthToDate(t, -1);
							} else if (DateUtils.diffInDay(startFB, f) < 0) {
								try {
									endDate =  DateUtils.parseDate("01-01-1970");
								} catch (ParseException e) {
									e.printStackTrace();
								}
								break;
							}
						} catch (FacebookException e) {
							e.printStackTrace();
						}
						
					}
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					Hashtable<String, Object> bi = FacebookUtils.getBaseInfo(curr);

					System.out.println(curr + "," + bi.get("name") + "," + bi.get("likes") + "," + bi.get("talking_about_count") + "," + DateUtils.formatDate(startDate) + "," + DateUtils.formatDate(endDate));
					
				}
		
	}
	
}
