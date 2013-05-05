package it.cloudpanel.service.converter;

import it.cloudpanel.service.model.Domanda;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.appengine.api.datastore.Key;


@XmlRootElement(name = "domanda")
public class DomandaConverter {
	private Domanda entity = null;

	public DomandaConverter() {
		entity = new Domanda();
	}

	public DomandaConverter(Domanda entity) {
		this.entity = entity;
	} 

	@XmlElement
	public String getDescrizione() {
		return entity.getDomanda();
	}

	@XmlElement
	public String getId() {
		return entity.getId();
	}
	
	@XmlElement
	public Key getDatastoreId() {
		return entity.getDatastoreId();
	}

	public Domanda getDomanda() {
		return entity;
	}

	public void setDescrizione(String descrizione) {
		entity.setDomanda(descrizione);
	}

	public void setId(String id) {
		entity.setId(id);
	}
	
	public void setDatastoreId(Key dtstId) {
		entity.setDatastoreId(dtstId);
	}


}

