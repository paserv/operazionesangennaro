package it.osg.service.model;

import java.util.Date;

import com.google.appengine.api.datastore.Key;

public class BallaroData {

	private Key datastoreId;

	private String id;
	private Date date;
	private String likeCount;
	private String talkingAboutCount;
	private long timestamp;


	
	public Key getDatastoreId() {
		return datastoreId;
	}


	public void setDatastoreId(Key datastoreId) {
		this.datastoreId = datastoreId;
	}



	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	

	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	


	public String getLikeCount() {
		return likeCount;
	}


	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}


	public String getTalkingAboutCount() {
		return talkingAboutCount;
	}


	public void setTalkingAboutCount(String talkingAboutCount) {
		this.talkingAboutCount = talkingAboutCount;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	@Override
	public String toString() {
		return "Conf [datastoreId=" + datastoreId + 
				", date=" + date + 
				", like_count=" + likeCount + 
				", talking_about=" + talkingAboutCount + 
				", timestamp=" + timestamp
				+ "]";
	}

}
