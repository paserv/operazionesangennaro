package it.osg.service.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.appengine.api.datastore.Key;

@XmlRootElement(name = "transmission")
public class TransmissionData {

	private Key datastoreId;

	private String id;
	private Date date;
	private long likeCount;
	private long talkingAboutCount;
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

	

	
	public long getLikeCount() {
		return likeCount;
	}


	public void setLikeCount(long likeCount) {
		this.likeCount = likeCount;
	}


	public long getTalkingAboutCount() {
		return talkingAboutCount;
	}


	public void setTalkingAboutCount(long talkingAboutCount) {
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
		return "Data [datastoreId=" + datastoreId + 
				", date=" + date + 
				", like_count=" + likeCount + 
				", talking_about=" + talkingAboutCount + 
				", timestamp=" + timestamp
				+ "]";
	}

}
