package smtpServer.requete;

import java.io.BufferedOutputStream;

import smtpServer.Etat;
import util.FileMails.Mail;

/**
 *
 * @author Corinne & Laura
 */
public class ActionDATA extends ActionType {
	
	private Mail mail;
	private boolean endLine;
	private String finRequete;
	private String [] users;

	public ActionDATA(BufferedOutputStream outDonnees) {
		super(outDonnees);
		mail = new Mail();
		endLine = false;
		finRequete = "\r\n";
		users = null;
	}

	public Etat PrecessingData(String[] users) {
		String msg = super.reponseOk("354");
		if (sendMsg(msg)) {
			mail = new Mail();
			endLine = false;
			this.users = users;
			return Etat.ECRI_MAIL;
		}
		
		return Etat.TRANSAC_DEST;
	}
	
	public Etat PrecessingReceiveData(String data) {
		if(data.equals(finRequete)){
			endLine = true;
		}else{
			if(endLine != true){
				mail.addHeader(data);
			}else{
				mail.addLine(data);
			}
		}
		
		if (data.contains(finRequete+"."+finRequete)) { // Fin du mail
			// Enregistrer dans les boites des users
			return Etat.MSG_ENVOYE;
		}else{
			return Etat.ECRI_MAIL;
		}
		
	}
}
