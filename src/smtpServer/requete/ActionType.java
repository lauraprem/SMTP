package smtpServer.requete;

import java.io.BufferedOutputStream;
import java.io.IOException;

import smtpServer.Etat;
import util.MsgServer.MsgServer;

/**
 * Actions types réalisées par le serveur, classe mère des réactions au différentes requetes 
 * @author Corinne & Laura
 *
 */
public abstract class ActionType {

	private BufferedOutputStream outDonnees;
	protected String user;

	public ActionType(BufferedOutputStream outDonnees) {
		super();
		this.outDonnees = outDonnees;
		user = null;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public BufferedOutputStream getOutDonnees() {
		return outDonnees;
	}

	public void setOutDonnees(BufferedOutputStream outDonnees) {
		this.outDonnees = outDonnees;
	}

	protected String reponseOk(String msg){
		//TODO construire réponse ok
		return msg+" OK";
	}
	
	protected String reponseKo(String msg){
		return msg;
	}

	protected boolean sendMsg(String msg){
		return sendToClient(msg+"\r\n");
	}
	
	protected boolean sendToClient(String msg){
		
		try {
			outDonnees.write(msg.getBytes(), 0, (int) msg.getBytes().length);
			outDonnees.flush();
			MsgServer.msgInfo("Send", msg, user);
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
			MsgServer.msgError("IOException", e.getMessage(), user);
			return false;
		}
	}

	protected Etat PrecessingDefault() {
		return Etat.AUTHENTIFICATION;
	}

}
