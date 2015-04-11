package serverPop3.requete;

import java.io.BufferedOutputStream;

import util.Lock.Lock;
import util.Lock.LockStates;

/**
 *
 * @author Corinne & Laura
 */
public class ActionQUIT extends ActionType {

	public ActionQUIT(BufferedOutputStream outDonnees) {
		super(outDonnees);
	}

	public boolean PrecessingDefault() {
		String msg;
		if (user == null || user == "" || !Lock.existUser(user)
				|| Lock.unlock(user) != LockStates.ERROR) {

			// Envoi du message au client
			msg = super.reponseOk("POP3 server signing off");
			if (sendMsg(msg)) {
				return true;
			}

			return false;
		} else {
			return false;
		}
	}
}
