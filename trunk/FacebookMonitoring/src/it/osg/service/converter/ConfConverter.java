package it.osg.service.converter;

import it.osg.service.model.Conf;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.appengine.api.datastore.Key;


@XmlRootElement(name = "conf")
public class ConfConverter {
	private Conf entity = null;

	public ConfConverter() {
		entity = new Conf();
	}

	public ConfConverter(Conf entity) {
		this.entity = entity;
	} 

	@XmlElement
	public String getUrl() {
		return entity.getUrl();
	}

	@XmlElement
	public String getId() {
		return entity.getId();
	}
	
	@XmlElement
	public Key getDatastoreId() {
		return entity.getDatastoreId();
	}

	public Conf getDomanda() {
		return entity;
	}

	public void setUrl(String url) {
		entity.setUrl(url);
	}

	public void setId(String id) {
		entity.setId(id);
	}
	
	public void setDatastoreId(Key dtstId) {
		entity.setDatastoreId(dtstId);
	}


}

