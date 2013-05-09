package it.osg.service.converter;

import java.util.Date;

import it.osg.service.model.BallaroData;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.appengine.api.datastore.Key;


@XmlRootElement(name = "ballaro")
public class BallaroDataConverter {
	private BallaroData entity = null;

	public BallaroDataConverter() {
		entity = new BallaroData();
	}

	public BallaroDataConverter(BallaroData entity) {
		this.entity = entity;
	} 

	@XmlElement
	public String getId() {
		return entity.getId();
	}
	
	@XmlElement
	public Key getDatastoreId() {
		return entity.getDatastoreId();
	}
	
	@XmlElement
	public String getLikeCount() {
		return entity.getLikeCount();
	}
	
	@XmlElement
	public String getTalkingAboutCount() {
		return entity.getTalkingAboutCount();
	}
	
	@XmlElement
	public Date getDate() {
		return entity.getDate();
	}

	@XmlElement
	public long getTimestamp() {
		return entity.getTimestamp();
	}
	

}

