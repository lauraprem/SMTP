package smtpServer.requete;

import java.io.BufferedOutputStream;

import smtpServer.Etat;

/**
 *
 * @author Corinne & Laura
 */
public class ActionDATA extends ActionType {
	
	private String stringMail;

	public ActionDATA(BufferedOutputStream outDonnees) {
		super(outDonnees);
	}

	public Etat PrecessingData(String[] users) {
		String msg;
		
		msg = super.reponseOk("354");
		if (sendMsg(msg)) {
			stringMail = "";
			return Etat.ECRI_MAIL;
		}
		return Etat.TRANSAC_DEST;
	}
	
	public Etat PrecessingReceiveData(String data) {
		if (data.contains("\r\n"+"."+"\r\n")) {
			stringMail = stringMail + data;
			return Etat.ECRI_MAIL;
		}
		return Etat.MSG_ENVOYE;
	}
}
