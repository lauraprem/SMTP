package smtpServer.requete;

import java.io.BufferedOutputStream;

import util.FileMails.FileMails;

public class Requete {

	public static final String EXTENSION_MAIL = ".txt";
	public static final String MAIL_PATH = "./StockMail/";

	private FileMails fileMails;

	private ActionEHLO ehlo;
	private ActionMAIL mail;
	private ActionRCPT rctp;
	private ActionDATA data;
	private ActionQUIT quit;

	public Requete(BufferedOutputStream outDonnees) {
		super();
		quit = new ActionQUIT(outDonnees);
		fileMails = null;
	}

	public ActionEHLO getEhlo() {
		return ehlo;
	}

	public void setApop(ActionEHLO ehlo) {
		this.ehlo = ehlo;
	}

	public ActionQUIT getQuit() {
		return quit;
	}

	public void setQuit(ActionQUIT quit) {
		this.quit = quit;
	}

//	public void setUserRequete() {
//		String user = apop.getUser();
//		if (user != null) {
//			quit.setUser(user);
//			retr.setUser(user);
//
//			fileMails = new FileMails(user, EXTENSION_MAIL, MAIL_PATH + user
//					+ "/");
//			// TODO A enlever le save
////			fileMails.saveMails();
//			fileMails.extractMails();
//		}
//	}

//	public Etat processingApop(String params) {
//		Etat etat = apop.Apop(params);
//		if (etat == Etat.TRANSACTION) {
//			setUserRequete();
//		}
//		return etat;
//	}

	public boolean processingQuit() {
		return quit.PrecessingDefault();
	}

}
