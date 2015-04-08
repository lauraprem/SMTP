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

	public Etat ProcessingEhlo(String params) {
		String msg = super.reponseOkEhlo("250", "lauco greets" + params);
		super.sendMsg(msg);
		return Etat.ETABL_TRANSAC;
	}

}
