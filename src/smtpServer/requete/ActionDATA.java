package smtpServer.requete;

import java.io.BufferedOutputStream;
import java.util.ArrayList;

import smtpServer.Etat;
import util.StringContainer;
import util.FileMails.FileMails;
import util.FileMails.Mail;

/**
 *
 * @author Corinne & Laura
 */
public class ActionDATA extends ActionType {

	private Mail mail;
	private boolean endLine;
	private String finRequete;
	private ArrayList<StringContainer> users;
	private FileMails fileMails;

	public ActionDATA(BufferedOutputStream outDonnees) {
		super(outDonnees);
		mail = new Mail();
		endLine = false;
		finRequete = "\r\n";
		users = null;
	}

	public Etat processingData(ArrayList<StringContainer> users,FileMails fileMails) {
		String msg = super.reponseOk("354");
		if (sendMsg(msg)) {
			mail = new Mail();
			endLine = false;
			this.users = users;
			this.fileMails = fileMails;
			fileMails.addMail(mail);
			return Etat.ECRI_MAIL;
		}

		return Etat.TRANSAC_DEST;
	}

	public Etat processingReceiveData(String data) {
		if (data.equals(finRequete)) {
			endLine = true;
		} else {
			if (endLine != true) {
				mail.addHeader(data);
			} else {
				mail.addLine(data);
			}
		}

		if (data.equals(".")) {
			
			// Enregistrer dans les boîtes des users
			for (int i = 0; i < users.size(); i++) {
				fileMails.setName(users.get(i).getString());
				fileMails.setLocation(fileMails.getLocation()+users.get(i).getString()+'/');
				fileMails.extractMails();
				mail.setId(fileMails.getListMail().size());
				fileMails.addMail(mail);
				fileMails.CreateSaveMails();
				fileMails.saveMails();
			}
			
			return Etat.MSG_ENVOYE;
		} else {
			return Etat.ECRI_MAIL;
		}

	}
}
