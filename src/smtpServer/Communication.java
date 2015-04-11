package smtpServer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import smtpServer.requete.Requete;
import util.StringContainer;
import util.FileMails.FileMails;
import util.MsgServer.MsgServer;

/**
 * @author Corinne & Laura
 */
public class Communication extends Thread {

	private Socket socket;
	private int SO_TIMEOUT;
	private BufferedReader in;
	private PrintWriter out;
	private BufferedOutputStream outDonnees;

	private Requete requete;
	private String finRequete;
	private Etat etatCourant;
	private StringContainer expediteur;
	private ArrayList<StringContainer> destinataires;
	private FileMails data;

	public static final String EXTENSION_MAIL = ".txt";
	public static final String MAIL_PATH = "./StockMail/";

	private static final int uneMinute = 60000;

	public Communication(Socket connexion) {
		SO_TIMEOUT = 10 * uneMinute;
		socket = connexion;
		expediteur = new StringContainer();
		expediteur.setString(socket.toString());
		destinataires = new ArrayList<StringContainer>();
		try {
			socket.setSoTimeout(SO_TIMEOUT);
		} catch (SocketException ex) {
			MsgServer.msgError("socket time-out", ex.getMessage(),
					expediteur.getString());
			this.sendMsg(this.reponseKo("550", "socket time-out"));
		}

		// flux
		in = null;
		out = null;
		outDonnees = null;

		// Autre
		requete = null;
		data = new FileMails(EXTENSION_MAIL, MAIL_PATH);
		finRequete = "\r\n";
		etatCourant = Etat.AUTHENTIFICATION;
	}

	@Override
	public void run() {

		// Console connexion TCP correct
		MsgServer.msgConnect(true, expediteur.getString());

		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			outDonnees = new BufferedOutputStream(socket.getOutputStream());

			requete = new Requete(outDonnees);

			// Envoi Message de bienvenue
			String msg = "220 lauco.com Simple Mail Transfer" + finRequete;
			outDonnees.write(msg.getBytes(), 0, (int) msg.getBytes().length);
			outDonnees.flush();
			MsgServer.msgInfo("Send", msg, expediteur.getString());

			// Permet de savoir si la connexion est clôturer

			boolean isQuit = false;
			while (!isQuit) {

				// recupere la premiere ligne de la requete du client
				String ligne = readLine();
				MsgServer.msgInfo("Request receive", ligne,
						expediteur.getString());

				if (ligne != null) {
					isQuit = processingRequest(ligne);
				} else {
					isQuit = true;
				}
			}

		} catch (SocketTimeoutException e) {
			System.out.println(expediteur.getString() + " time_out dépassé : "
					+ e.getMessage());
			// TODO gestion erreur
			// erreur(408);
		} catch (IOException ex) {
			System.out.println(expediteur.getString() + " Error : "
					+ ex.getMessage());
			// erreur(500);
		} finally {
			// if (MsgServer.isUserFormat(user) && Lock.existUser(user)) {
			// Lock.unlock(user);
			// }
			close(in);
			close(out);
			close(outDonnees);
			close(socket);
		}
		MsgServer.msgConnect(false, expediteur.getString());
	}

	/**
	 * Traite la demande du client
	 * 
	 * @param requete
	 *            du client
	 */
	public boolean processingRequest(String receive) {

		// Permet de savoir si la connexion est � cl�turer
		boolean isQuit = false;

		// R�cup�ration et validation de la commande en fonction de l'�tat
		// courrent
		// if (receive.length() >= 4) { // si fin \r\n
		String params = "";
		String command = "";
		if (receive.length() >= 4) {
			command = receive.substring(0, 4);
			params = receive.substring(4);

			// Mise à jour des params
			ArrayList<String> commandParams = receiveCommandMailFrom(command,
					params);
			command = commandParams.get(0);
			params = commandParams.get(1);
			commandParams = receiveCommandRcptTo(command, params);
			command = commandParams.get(0);
			params = commandParams.get(1);
		} else {
			command = receive;
		}

		MsgServer.msgInfo("Command receive", command, expediteur.getString());
		MsgServer.msgInfo("Params receive", params, expediteur.getString());

		switch (etatCourant) {
		case AUTHENTIFICATION:
			switch (command) {
			case "EHLO":
				MsgServer.msgInfo("processing", "EHLO ...",
						expediteur.getString());
				etatCourant = requete.processingEhlo(params);
				if (etatCourant.equals(Etat.ETABL_TRANSAC)) {
					expediteur.setString(params.trim());
				}
				break;
			case "QUIT":
				MsgServer.msgInfo("processing", "QUIT ...",
						expediteur.getString());
				requete.getQuit().setUser(expediteur.getString());
				isQuit = requete.processingQuit(expediteur, destinataires,
						this.data);
				break;
			default:
				MsgServer.msgWarnning("Unidentified command", command,
						expediteur.getString());
				this.sendMsg(this.reponseKo("550", "Unidentified command"));
				break;
			}
			break;

		case ETABL_TRANSAC:
			switch (command) {
			case "MAIL FROM":
				MsgServer.msgInfo("processing", "MAIL FROM ...",
						expediteur.getString());
				etatCourant = requete.processingMail(expediteur, destinataires,
						data, params);
				break;
			case "QUIT":
				MsgServer.msgInfo("processing", "QUIT ...",
						expediteur.getString());
				isQuit = requete.processingQuit(expediteur, destinataires,
						this.data);
				break;
			default:
				MsgServer.msgWarnning("Unidentified command", command,
						expediteur.getString());
				this.sendMsg(this.reponseKo("550", "Unidentified command"));
				break;
			}
			break;
		case TRANSAC_NO_DEST:
			switch (command) {
			case "RCPT TO":
				MsgServer.msgInfo("processing", "RCPT TO ...",
						expediteur.getString());
				etatCourant = requete.processingRcpt(destinataires, params);
				// TRANSAC_DEST
				break;
			case "QUIT":
				MsgServer.msgInfo("processing", "QUIT ...",
						expediteur.getString());
				isQuit = requete.processingQuit(expediteur, destinataires,
						this.data);
				break;
			default:
				MsgServer.msgWarnning("Unidentified command", command,
						expediteur.getString());
				this.sendMsg(this.reponseKo("550", "Unidentified command"));
				break;
			}
			break;
		case TRANSAC_DEST:
			switch (command) {
			case "RCPT TO":
				MsgServer.msgInfo("processing", "RCPT TO ...",
						expediteur.getString());
				etatCourant = requete.processingRcpt(destinataires, params);
				// TRANSAC_DEST
				break;
			case "DATA":
				MsgServer.msgInfo("processing", "DATA ...",
						expediteur.getString());
				etatCourant = requete.processingData(destinataires, data);
				break;
			case "QUIT":
				MsgServer.msgInfo("processing", "QUIT ...",
						expediteur.getString());
				isQuit = requete.processingQuit(expediteur, destinataires,
						this.data);
				break;
			default:
				MsgServer.msgWarnning("Unidentified command", command,
						expediteur.getString());
				this.sendMsg(this.reponseKo("550", "Unidentified command"));
				break;
			}
			break;
		case ECRI_MAIL:
			String data = command + params;
			etatCourant = requete.processingReceiveData(data);

			break;
		case MSG_ENVOYE:
			switch (command) {
			case "MAIL FROM":
				MsgServer.msgInfo("processing", "MAIL FROM ...",
						expediteur.getString());
				etatCourant = requete.processingMail(expediteur, destinataires,
						this.data, params);
				break;
			case "QUIT":
				MsgServer.msgInfo("processing", "QUIT ...",
						expediteur.getString());
				isQuit = requete.processingQuit(expediteur, destinataires,
						this.data);
				break;
			default:
				MsgServer.msgWarnning("Unidentified command", command,
						expediteur.getString());
				this.sendMsg(this.reponseKo("550", "Unidentified command"));
				break;
			}
			break;

		default:
			MsgServer.msgWarnning("Unidentified etat", etatCourant.toString(),
					expediteur.getString());
			break;
		}

		// } else {
		// MsgServer.msgWarnning("Invalid request form", null,
		// expediteur.getString());
		// this.sendMsg(this.reponseKo("Invalid request form"));
		// }

		return isQuit;
	}

	/**
	 * Ferme les flux.
	 *
	 * @param stream
	 *            flux qui va �tre ferm�
	 */
	public void close(Object stream) {
		if (stream == null) {
			return;
		}
		try {
			if (stream instanceof Reader) {
				((Reader) stream).close();
			} else if (stream instanceof Writer) {
				((Writer) stream).close();
			} else if (stream instanceof InputStream) {
				((InputStream) stream).close();
			} else if (stream instanceof OutputStream) {
				((OutputStream) stream).close();
			} else if (stream instanceof Socket) {
				((Socket) stream).close();
			} else {
				// TODO gestion erreur
				System.err.println("Unable to close object: " + stream);
			}
		} catch (Exception e) {
			System.err.println("Error closing stream: " + e);
		}
	}

	protected String reponseOk(String num) {
		return num + " OK";
	}

	protected String reponseKo(String num, String msg) {
		return num + " " + msg;
	}

	private boolean sendMsg(String msg) {
		return sendToClient(msg + "\r\n");
	}

	private boolean sendToClient(String msg) {

		try {
			outDonnees.write(msg.getBytes(), 0, (int) msg.getBytes().length);
			outDonnees.flush();
			MsgServer.msgInfo("Send", msg, expediteur.getString());
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
			MsgServer.msgError("IOException", e.getMessage(),
					expediteur.getString());
			return false;
		}
	}

	private String readLine() throws IOException {
		InputStream ligneByte = socket.getInputStream();
		String ligne = "";
		boolean isEndLine = false;
		while (!isEndLine) {
			int reading = ligneByte.read();

			if (reading == -1) {
				return null;
			}

			ligne = ligne + (char) reading;
			isEndLine = ligne.contains(finRequete);
		}

		String[] requeteString = ligne.split(finRequete);
		if (requeteString.length > 0) {
			return requeteString[0];
		}
		return finRequete;
	}

	/**
	 * Récupération du from s'il existe
	 * @return command MAIL FROM et le params sinon MAIL et params
	 */
	private ArrayList<String> receiveCommandMailFrom(String command,
			String params) {
		ArrayList<String> commandParams = new ArrayList<String>();
		if (command.equals("MAIL")) {
			if (params.startsWith(" FROM") == true) {
				command = command + " FROM";
				params = params.replaceFirst(" FROM", "");
			}
		}
		commandParams.add(command);
		commandParams.add(params);

		return commandParams;
	}

	/**
	 * Récupération du from s'il existe
	 * @return command RCPT TO et le params sinon RCPT et params
	 */
	private ArrayList<String> receiveCommandRcptTo(String command, String params) {
		ArrayList<String> commandParams = new ArrayList<String>();
		if (command.equals("RCPT")) {
			if (params.startsWith(" TO") == true) {
				command = command + " TO";
				params = params.replaceFirst(" TO", "");
			}
		}
		commandParams.add(command);
		commandParams.add(params);

		return commandParams;
	}

}
