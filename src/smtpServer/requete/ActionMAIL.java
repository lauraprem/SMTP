package smtpServer.requete;

import java.io.BufferedOutputStream;

import smtpServer.Etat;

/**
 *
 * @author Corinne & Laura
 */
public class ActionMAIL extends ActionType {

	public ActionMAIL(BufferedOutputStream outDonnees) {
		super(outDonnees);
	}

	public Etat processingDefault() {
		// TODO A enlever ou laisser si static (voir avec Laura)
		// Lib�ration du verrou s'il est � lib�rer
		String msg;
		// if (user == null || user == ""
		// ||!Lock.existUser(user)||Lock.unlock(user) != LockStates.ERROR) {

		// Envoi du message au client
		// msg = super.reponseOk("POP3 server signing off");
		// if (sendMsg(msg)) {
		// return true;
		// }
		//
		// return false;
		// } else {
		// // TODO msg error
		// // msg = super.reponseKo("Error lors du deverouillage");
		// System.out.println("erreur lors du d�verouillage");
		// return false;
		// }

		return null;
	}
}
