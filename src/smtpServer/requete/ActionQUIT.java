package smtpServer.requete;

import java.io.BufferedOutputStream;
import java.util.ArrayList;

import util.StringContainer;
import util.FileMails.FileMails;

/**
 *
 * @author Corinne & Laura
 */
public class ActionQUIT extends ActionType {

	public ActionQUIT(BufferedOutputStream outDonnees) {
		super(outDonnees);
	}

	public boolean processingQuit(StringContainer expediteur,
			ArrayList<StringContainer> destinataires, FileMails mails) {
		expediteur.setString("");
		destinataires = new ArrayList<StringContainer>();
		mails.supressMail();
		String msg = super.reponseOk("221");
		if (sendMsg(msg)) {
			return true;
		}

		return false;
	}
}
