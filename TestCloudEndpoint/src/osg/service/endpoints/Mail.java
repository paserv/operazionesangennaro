package osg.service.endpoints;

public class Mail {

	private String destinatario;
	private String oggetto;
	private String text;
	private String mittente;
	private String mailMittente;
	

	public Mail() {}


	public String getDestinatario() {
		return destinatario;
	}


	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}


	public String getOggetto() {
		return oggetto;
	}


	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getMittente() {
		return mittente;
	}


	public void setMittente(String mittente) {
		this.mittente = mittente;
	}


	public String getMailMittente() {
		return mailMittente;
	}


	public void setMailMittente(String mailMittente) {
		this.mailMittente = mailMittente;
	};
	
	

}