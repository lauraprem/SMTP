package smtpServer.requete;

import java.io.BufferedOutputStream;

import smtpServer.Etat;

/**
 *
 * @author Corinne & Laura
 */
public class ActionEHLO extends ActionType {

	public ActionEHLO(BufferedOutputStream outDonnees) {
		super(outDonnees);
	}

	public Etat PrecessingDefault() {

		String msg;

		// Envoi du message au client
		// msg = super.reponseOk("POP3 server signing off");
		// if (sendMsg(msg)) {
		// return true;
		// }
		//
		// return false;
		return null;
	}
}
