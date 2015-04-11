package serverPop3.requete;

import java.io.BufferedOutputStream;
import java.util.ArrayList;

import util.FileMails.FileMails;
import util.FileMails.Mail;

/**
*
* @author Corinne & Laura
*/
public class ActionRETR extends ActionType {
	
	private Mail mail;
	
	public ActionRETR(BufferedOutputStream outDonnees) {
		super(outDonnees);
		
		// Permettra de sauvegarder le mail courrent
		mail = new Mail();
	}
	
	public boolean PrecessingDefault(String params, FileMails fileMails) {
		
		// Récupération du numéro du mail
		int num;
		try{
			num = Integer.parseInt(params.trim());
		}catch(NumberFormatException e){
			num = -1;
		}
				
		// Récupération du message s'il existe
		mail = fileMails.getMail(num);
		
		// Envoi de la réponse au client
		if(mail != null){
			
			// Envoi message : +OK {nb octets}
			sendMsg(super.reponseOk(mail.CalculationSizeMail()+""));
			
			// Envoi message : {contenu du message}
			sendToClient(mail.toString());
			
			return true;
		}else{
			
			// Envoi message : erreur
			sendMsg( super.reponseKo("Number of message is not valid"));
			
			return false;
		}
	}
}
