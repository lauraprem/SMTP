package smtpServer.requete;

import java.io.BufferedOutputStream;

import smtpServer.Etat;
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
		ehlo = new ActionEHLO(outDonnees);
		mail = new ActionMAIL(outDonnees);
		rctp = new ActionRCPT(outDonnees);
		data = new ActionDATA(outDonnees);
		quit = new ActionQUIT(outDonnees);
		fileMails = null;
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

	public FileMails getFileMails() {
		return fileMails;
	}

	public void setFileMails(FileMails fileMails) {
		this.fileMails = fileMails;
	}

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

	public boolean processingQuit() {
		return quit.PrecessingQuit();
	}

	public Etat PrecessingDefault(String[] users) {
		return data.PrecessingData(users);
	}
	
	public Etat PrecessingReceiveData(String dataExempl) {
		return data.PrecessingReceiveData(dataExempl);
	}

	// PrecessingReceiveData(String data)

	public Etat processingEhlo(String params) {
		return ehlo.ProcessingEhlo(params);
	}
}
