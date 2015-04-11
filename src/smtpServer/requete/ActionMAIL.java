package smtpServer.requete;

import java.io.BufferedOutputStream;
import java.util.ArrayList;

import smtpServer.Etat;
import util.StringContainer;
import util.FileMails.FileMails;

/**
 *
 * @author Corinne & Laura
 */
public class ActionMAIL extends ActionType {

	public ActionMAIL(BufferedOutputStream outDonnees) {
		super(outDonnees);
	}

	public Etat processingMail(StringContainer expediteur,
			ArrayList<StringContainer> destinataires, FileMails mails,
			String params) {
		if (params != null && !params.equals("")) {
			expediteur.setString(params);
			destinataires = new ArrayList<StringContainer>();
			mails.supressMail();
			super.sendMsg(super.reponseOk("250"));
			return Etat.TRANSAC_NO_DEST;
		}
		super.sendMsg(super.reponseKo("550", "Action demandée non effectuée "));
		return Etat.ETABL_TRANSAC;
	}

}
