package it.cloudpanel.service.model;

import com.google.appengine.api.datastore.Key;

public class Domanda {
	
	private Key datastoreId;
	
	private String id;
	private boolean domandaDeploy;
	private boolean domandaDelivery;
	private boolean costs;
	private boolean timeToValueDeploy;
	private boolean regulatoryCompliance;
	private boolean control;
	private boolean governance;
	private boolean availability;
	private boolean customizability;
	private boolean timeToValueDelivery;
	private boolean migrationIntegration;
	private boolean contestoEconomico;
	private boolean contestoOrganizzativo;
	private boolean contestoTecnologico;
	private boolean contestoGiuridico;
	
	private String domanda; 
	private String risposta; 
	
	private int publicCloud;
	private int communityOnPremise;
	private int communityOffPremise;
	private int hybridCloud;
	private int privateOnPremise;
	private int privateOffPremise;
	private int noCloudFit;
	private int iaaS;
	private int paaS;
	private int saaS;
	private int bPaaS;
	

	public Domanda() {}

	

	public Domanda(Key datastoreId, String id, boolean domandaDeploy,
			boolean domandaDelivery, boolean costs, boolean timeToValueDeploy,
			boolean regulatoryCompliance, boolean control, boolean governance,
			boolean availability, boolean customizability,
			boolean timeToValueDelivery, boolean migrationIntegration,
			boolean contestoEconomico, boolean contestoOrganizzativo,
			boolean contestoTecnologico, boolean contestoGiuridico,
			String domanda, String risposta, int publicCloud,
			int communityOnPremise, int communityOffPremise, int hybridCloud,
			int privateOnPremise, int privateOffPremise, int noCloudFit,
			int iaaS, int paaS, int saaS, int bPaaS) {
		super();
		this.datastoreId = datastoreId;
		this.id = id;
		this.domandaDeploy = domandaDeploy;
		this.domandaDelivery = domandaDelivery;
		this.costs = costs;
		this.timeToValueDeploy = timeToValueDeploy;
		this.regulatoryCompliance = regulatoryCompliance;
		this.control = control;
		this.governance = governance;
		this.availability = availability;
		this.customizability = customizability;
		this.timeToValueDelivery = timeToValueDelivery;
		this.migrationIntegration = migrationIntegration;
		this.contestoEconomico = contestoEconomico;
		this.contestoOrganizzativo = contestoOrganizzativo;
		this.contestoTecnologico = contestoTecnologico;
		this.contestoGiuridico = contestoGiuridico;
		this.domanda = domanda;
		this.risposta = risposta;
		this.publicCloud = publicCloud;
		this.communityOnPremise = communityOnPremise;
		this.communityOffPremise = communityOffPremise;
		this.hybridCloud = hybridCloud;
		this.privateOnPremise = privateOnPremise;
		this.privateOffPremise = privateOffPremise;
		this.noCloudFit = noCloudFit;
		this.iaaS = iaaS;
		this.paaS = paaS;
		this.saaS = saaS;
		this.bPaaS = bPaaS;
	}



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



	public boolean isDomandaDeploy() {
		return domandaDeploy;
	}



	public void setDomandaDeploy(boolean domandaDeploy) {
		this.domandaDeploy = domandaDeploy;
	}



	public boolean isDomandaDelivery() {
		return domandaDelivery;
	}



	public void setDomandaDelivery(boolean domandaDelivery) {
		this.domandaDelivery = domandaDelivery;
	}



	public boolean isCosts() {
		return costs;
	}



	public void setCosts(boolean costs) {
		this.costs = costs;
	}



	public boolean isTimeToValueDeploy() {
		return timeToValueDeploy;
	}



	public void setTimeToValueDeploy(boolean timeToValueDeploy) {
		this.timeToValueDeploy = timeToValueDeploy;
	}



	public boolean isRegulatoryCompliance() {
		return regulatoryCompliance;
	}



	public void setRegulatoryCompliance(boolean regulatoryCompliance) {
		this.regulatoryCompliance = regulatoryCompliance;
	}



	public boolean isControl() {
		return control;
	}



	public void setControl(boolean control) {
		this.control = control;
	}



	public boolean isGovernance() {
		return governance;
	}



	public void setGovernance(boolean governance) {
		this.governance = governance;
	}



	public boolean isAvailability() {
		return availability;
	}



	public void setAvailability(boolean availability) {
		this.availability = availability;
	}



	public boolean isCustomizability() {
		return customizability;
	}



	public void setCustomizability(boolean customizability) {
		this.customizability = customizability;
	}



	public boolean isTimeToValueDelivery() {
		return timeToValueDelivery;
	}



	public void setTimeToValueDelivery(boolean timeToValueDelivery) {
		this.timeToValueDelivery = timeToValueDelivery;
	}



	public boolean isMigrationIntegration() {
		return migrationIntegration;
	}



	public void setMigrationIntegration(boolean migrationIntegration) {
		this.migrationIntegration = migrationIntegration;
	}



	public boolean isContestoEconomico() {
		return contestoEconomico;
	}



	public void setContestoEconomico(boolean contestoEconomico) {
		this.contestoEconomico = contestoEconomico;
	}



	public boolean isContestoOrganizzativo() {
		return contestoOrganizzativo;
	}



	public void setContestoOrganizzativo(boolean contestoOrganizzativo) {
		this.contestoOrganizzativo = contestoOrganizzativo;
	}



	public boolean isContestoTecnologico() {
		return contestoTecnologico;
	}



	public void setContestoTecnologico(boolean contestoTecnologico) {
		this.contestoTecnologico = contestoTecnologico;
	}



	public boolean isContestoGiuridico() {
		return contestoGiuridico;
	}



	public void setContestoGiuridico(boolean contestoGiuridico) {
		this.contestoGiuridico = contestoGiuridico;
	}



	public String getDomanda() {
		return domanda;
	}



	public void setDomanda(String domanda) {
		this.domanda = domanda;
	}



	public String getRisposta() {
		return risposta;
	}



	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}



	public int getPublicCloud() {
		return publicCloud;
	}



	public void setPublicCloud(int publicCloud) {
		this.publicCloud = publicCloud;
	}



	public int getCommunityOnPremise() {
		return communityOnPremise;
	}



	public void setCommunityOnPremise(int communityOnPremise) {
		this.communityOnPremise = communityOnPremise;
	}



	public int getCommunityOffPremise() {
		return communityOffPremise;
	}



	public void setCommunityOffPremise(int communityOffPremise) {
		this.communityOffPremise = communityOffPremise;
	}



	public int getHybridCloud() {
		return hybridCloud;
	}



	public void setHybridCloud(int hybridCloud) {
		this.hybridCloud = hybridCloud;
	}



	public int getPrivateOnPremise() {
		return privateOnPremise;
	}



	public void setPrivateOnPremise(int privateOnPremise) {
		this.privateOnPremise = privateOnPremise;
	}



	public int getPrivateOffPremise() {
		return privateOffPremise;
	}



	public void setPrivateOffPremise(int privateOffPremise) {
		this.privateOffPremise = privateOffPremise;
	}



	public int getNoCloudFit() {
		return noCloudFit;
	}



	public void setNoCloudFit(int noCloudFit) {
		this.noCloudFit = noCloudFit;
	}



	public int getIaaS() {
		return iaaS;
	}



	public void setIaaS(int iaaS) {
		this.iaaS = iaaS;
	}



	public int getPaaS() {
		return paaS;
	}



	public void setPaaS(int paaS) {
		this.paaS = paaS;
	}



	public int getSaaS() {
		return saaS;
	}



	public void setSaaS(int saaS) {
		this.saaS = saaS;
	}



	public int getBPaaS() {
		return bPaaS;
	}



	public void setBPaaS(int bPaaS) {
		this.bPaaS = bPaaS;
	}



	@Override
	public String toString() {
		return "Domanda [datastoreId=" + datastoreId + ", id=" + id
				+ ", domandaDeploy=" + domandaDeploy + ", domandaDelivery="
				+ domandaDelivery + ", costs=" + costs + ", timeToValueDeploy="
				+ timeToValueDeploy + ", regulatoryCompliance="
				+ regulatoryCompliance + ", control=" + control
				+ ", governance=" + governance + ", availability="
				+ availability + ", customizability=" + customizability
				+ ", timeToValueDelivery=" + timeToValueDelivery
				+ ", migrationIntegration=" + migrationIntegration
				+ ", contestoEconomico=" + contestoEconomico
				+ ", contestoOrganizzativo=" + contestoOrganizzativo
				+ ", contestoTecnologico=" + contestoTecnologico
				+ ", contestoGiuridico=" + contestoGiuridico + ", domanda="
				+ domanda + ", risposta=" + risposta + ", publicCloud="
				+ publicCloud + ", communityOnPremise=" + communityOnPremise
				+ ", communityOffPremise=" + communityOffPremise
				+ ", hybridCloud=" + hybridCloud + ", privateOnPremise="
				+ privateOnPremise + ", privateOffPremise=" + privateOffPremise
				+ ", noCloudFit=" + noCloudFit + ", iaaS=" + iaaS + ", paaS="
				+ paaS + ", saaS=" + saaS + ", bPaaS=" + bPaaS + "]";
	}



	

}

