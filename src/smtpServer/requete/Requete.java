package smtpServer.requete;

import java.io.BufferedOutputStream;
import java.util.ArrayList;

import smtpServer.Etat;
import util.StringContainer;
import util.FileMails.FileMails;

public class Requete {

	private ActionEHLO ehlo;
	private ActionMAIL mail;
	private ActionRCPT rctp;
	private ActionDATA data;
	private ActionQUIT quit;

	public Requete(BufferedOutputStream outDonnees) {
		super();
		ehlo = new ActionEHLO(outDonnees);
		mail = new ActionMAIL(outDonnees);
		rctp = new ActionRCPT(outDonnees);
		data = new ActionDATA(outDonnees);
		quit = new ActionQUIT(outDonnees);
	}

	// public void setUserRequete() {
	// String user = apop.getUser();
	// if (user != null) {
	// quit.setUser(user);
	// retr.setUser(user);
	//
	// fileMails = new FileMails(user, EXTENSION_MAIL, MAIL_PATH + user
	// + "/");
	// // TODO A enlever le save
	// // fileMails.saveMails();
	// fileMails.extractMails();
	// }
	// }

	// public Etat processingApop(String params) {
	// Etat etat = apop.Apop(params);
	// if (etat == Etat.TRANSACTION) {
	// setUserRequete();
	// }
	// return etat;
	// }

	public ActionEHLO getEhlo() {
		return ehlo;
	}

	public void setEhlo(ActionEHLO ehlo) {
		this.ehlo = ehlo;
	}

	public ActionMAIL getMail() {
		return mail;
	}

	public void setMail(ActionMAIL mail) {
		this.mail = mail;
	}

	public ActionRCPT getRctp() {
		return rctp;
	}

	public void setRctp(ActionRCPT rctp) {
		this.rctp = rctp;
	}

	public ActionDATA getData() {
		return data;
	}

	public void setData(ActionDATA data) {
		this.data = data;
	}

	public ActionQUIT getQuit() {
		return quit;
	}

	public void setQuit(ActionQUIT quit) {
		this.quit = quit;
	}

	public boolean processingQuit(StringContainer expediteur,
			ArrayList<StringContainer> destinataires, FileMails mails) {
		return quit.processingQuit(expediteur, destinataires, mails);
	}

	public Etat processingData(ArrayList<StringContainer> users,
			FileMails fileMails) {
		return data.processingData(users, fileMails);
	}

	public Etat processingReceiveData(String dataExempl) {
		return data.processingReceiveData(dataExempl);
	}

	public Etat processingEhlo(String params) {
		return ehlo.processingEhlo(params);
	}

	public Etat processingMail(StringContainer expediteur,
			ArrayList<StringContainer> destinataires, FileMails mails,
			String params) {
		return mail.processingMail(expediteur, destinataires, mails, params);
	}
}
