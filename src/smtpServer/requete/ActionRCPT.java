package smtpServer.requete;

import java.io.BufferedOutputStream;
import java.io.File;
import java.util.ArrayList;

import serverPop3.requete.Requete;
import smtpServer.Etat;
import util.StringContainer;

/**
 *
 * @author Corinne & Laura
 */
public class ActionRCPT extends ActionType {

	public ActionRCPT(BufferedOutputStream outDonnees) {
		super(outDonnees);
	}

	public Etat processingRcpt(ArrayList<StringContainer> destinataires,
			String params) {
		String param = params.trim().toUpperCase();

		if (existUser(param) && (params != null && !params.equals(""))) {
			destinataires.add(new StringContainer(param));
			if (super.sendMsg(super.reponseOk("250"))) {
				return Etat.TRANSAC_DEST;
			}
		}
		
		if (params == null || params.equals("")){
			super.sendMsg(super.reponseKo("553","Aucun desctinataire en parametre"));
		}else{
			super.sendMsg(super.reponseKo("553", "destinataire invalide"));
		}
		return Etat.TRANSAC_NO_DEST;
	}

	public static boolean existUser(String user) {
		File userFolder = new File(Requete.MAIL_PATH + user.toUpperCase());
		return userFolder.exists();
	}
}
