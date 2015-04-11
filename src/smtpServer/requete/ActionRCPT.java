package smtpServer.requete;

import java.io.BufferedOutputStream;
import java.util.ArrayList;

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
		return null;
	}
}
