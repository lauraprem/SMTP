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
		String param = params.trim();
		if (param != null && !param.equals("")) {
			expediteur.setString(param);
			destinataires = new ArrayList<StringContainer>();
			mails.supressMail();
			super.sendMsg(super.reponseOk("250"));
			return Etat.TRANSAC_NO_DEST;
		} else if (param == null || param.equals("")) {
			super.sendMsg(super.reponseKo("553",
					"Aucun expediteur en parametre"));
			return Etat.ETABL_TRANSAC;
		}
		super.sendMsg(super.reponseKo("550", "Action demandee non effectuee"));
		return Etat.ETABL_TRANSAC;
	}

}
