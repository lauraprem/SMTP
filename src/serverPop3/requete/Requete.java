package serverPop3.requete;

import java.io.BufferedOutputStream;

import serverPop3.Etat;
import util.FileMails.FileMails;

public class Requete {

	public static final String EXTENSION_MAIL = ".txt";
	public static final String MAIL_PATH = "./StockMail/";

	private FileMails fileMails;

	private ActionAPOP apop;
	private ActionQUIT quit;
	private ActionRETR retr;

	public Requete(BufferedOutputStream outDonnees) {
		super();
		apop = new ActionAPOP(outDonnees);
		quit = new ActionQUIT(outDonnees);
		retr = new ActionRETR(outDonnees);
		fileMails = null;
	}

	public ActionAPOP getApop() {
		return apop;
	}

	public void setApop(ActionAPOP apop) {
		this.apop = apop;
	}

	public ActionQUIT getQuit() {
		return quit;
	}

	public void setQuit(ActionQUIT quit) {
		this.quit = quit;
	}

	public ActionRETR getRetr() {
		return retr;
	}

	public void setRetr(ActionRETR retr) {
		this.retr = retr;
	}

	public void setUserRequete() {
		String user = apop.getUser();
		if (user != null) {
			quit.setUser(user);
			retr.setUser(user);

			fileMails = new FileMails(user, EXTENSION_MAIL, MAIL_PATH + user
					+ "/");
			fileMails.extractMails();
		}
	}

	public Etat processingApop(String params) {
		Etat etat = apop.Apop(params);
		if (etat == Etat.TRANSACTION) {
			setUserRequete();
		}
		return etat;
	}

	public boolean processingQuit() {
		return quit.PrecessingDefault();
	}

	public boolean processingRetr(String params) {
		return retr.PrecessingDefault(params, fileMails);
	}

}
