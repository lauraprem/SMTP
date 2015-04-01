package smtpServer.requete;

import java.io.BufferedOutputStream;

/**
 *
 * @author Corinne & Laura
 */
public class ActionQUIT extends ActionType {

	public ActionQUIT(BufferedOutputStream outDonnees) {
		super(outDonnees);
	}

	public boolean PrecessingDefault() {
		// TODO A enlever ou laisser si static (voir avec Laura)
		// Libération du verrou s'il est à libérer
		String msg;
//		if (user == null || user == "" ||!Lock.existUser(user)||Lock.unlock(user) != LockStates.ERROR) {

			// Envoi du message au client
			msg = super.reponseOk("POP3 server signing off");
			if (sendMsg(msg)) {
				return true;
			}

			return false;
//		} else {
//			// TODO msg error
//			// msg = super.reponseKo("Error lors du deverouillage");
//			System.out.println("erreur lors du déverouillage");
//			return false;
//		}
	}
}
