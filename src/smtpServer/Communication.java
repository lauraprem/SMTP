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

import smtpServer.requete.Requete;
import util.FileMails.Mail;
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
	private String expediteur;
	private String[] destinaires;
	private Mail data;

	private static final int uneMinute = 60000;

	public Communication(Socket connexion) {
		SO_TIMEOUT = 10 * uneMinute;
		socket = connexion;
		try {
			socket.setSoTimeout(SO_TIMEOUT);
		} catch (SocketException ex) {
			MsgServer.msgError("socket time-out", ex.getMessage(), expediteur);
			this.sendMsg(this.reponseKo("socket time-out"));
		}

		// flux
		in = null;
		out = null;
		outDonnees = null;

		// Autre
		requete = null;
		finRequete = "\r\n";
		expediteur = socket.toString();
		etatCourant = Etat.AUTHENTIFICATION;
	}

	@Override
	public void run() {

		// Console connexion TCP correct
		MsgServer.msgConnect(true, expediteur);

		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			outDonnees = new BufferedOutputStream(socket.getOutputStream());

			requete = new Requete(outDonnees);

			// Envoi Message de bienvenue
			String msg = "220 lauco.com Simple Mail Transfer" + finRequete;
			outDonnees.write(msg.getBytes(), 0, (int) msg.getBytes().length);
			outDonnees.flush();
			MsgServer.msgInfo("Send", msg, expediteur);

			// Permet de savoir si la connexion est clôturer

			boolean isQuit = false;
			while (!isQuit) {

				// recupere la premiere ligne de la requete du client
				String ligne = readLine();
				MsgServer.msgInfo("Request receive", ligne, expediteur);

				if (ligne != null) {
					isQuit = processingRequest(ligne);
				} else {
					isQuit = true;
				}
			}

		} catch (SocketTimeoutException e) {
			System.out.println(expediteur + " time_out d�pass� : "
					+ e.getMessage());
			// TODO gestion erreur
			// erreur(408);
		} catch (IOException ex) {
			System.out.println(expediteur + " Error : " + ex.getMessage());
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
		MsgServer.msgConnect(false, expediteur);
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
		if (receive.length() >= 4) { // si fin \r\n
			String command = receive.substring(0, 4);
			String params = receive.substring(4);
			MsgServer.msgInfo("Command receive", command, expediteur);
			MsgServer.msgInfo("Params receive", params, expediteur);

			switch (etatCourant) {
			case AUTHENTIFICATION:
				switch (command) {
				case "EHLO":
					MsgServer.msgInfo("processing", "EHLO ...", expediteur);
					etatCourant = requete.processingEhlo(params);
					break;
				case "QUIT":
					MsgServer.msgInfo("processing", "QUIT ...", expediteur);
					requete.getQuit().setUser(expediteur);
					isQuit = requete.processingQuit();
					break;
				default:
					MsgServer.msgWarnning("Unidentified command", command,
							expediteur);
					this.sendMsg(this.reponseKo("Unidentified command"));
					break;
				}
				break;

			case ETABL_TRANSAC:
				switch (command) {
				case "MAIL FROM":
					MsgServer
							.msgInfo("processing", "MAIL FROM ...", expediteur);
					// etatCourant = requete.processingEhlo(params);
					// TRANSAC_NO_DEST
					break;
				case "QUIT":
					MsgServer.msgInfo("processing", "QUIT ...", expediteur);
					isQuit = requete.processingQuit();
					break;
				default:
					MsgServer.msgWarnning("Unidentified command", command,
							expediteur);
					this.sendMsg(this.reponseKo("Unidentified command"));
					break;
				}
				break;
			case TRANSAC_NO_DEST:
				switch (command) {
				case "RCPT TO":
					MsgServer.msgInfo("processing", "RCPT TO ...", expediteur);
					// etatCourant = requete.processingEhlo(params);
					// TRANSAC_DEST
					break;
				case "QUIT":
					MsgServer.msgInfo("processing", "QUIT ...", expediteur);
					isQuit = requete.processingQuit();
					break;
				default:
					MsgServer.msgWarnning("Unidentified command", command,
							expediteur);
					this.sendMsg(this.reponseKo("Unidentified command"));
					break;
				}
				break;
			case TRANSAC_DEST:
				switch (command) {
				case "RCPT TO":
					MsgServer.msgInfo("processing", "RCPT TO ...", expediteur);
					// etatCourant = requete.processingEhlo(params);
					// TRANSAC_DEST
					break;
				case "DATA":
					MsgServer.msgInfo("processing", "DATA ...", expediteur);
					etatCourant = requete.processingData(destinaires);
					break;
				case "QUIT":
					MsgServer.msgInfo("processing", "QUIT ...", expediteur);
					isQuit = requete.processingQuit();
					break;
				default:
					MsgServer.msgWarnning("Unidentified command", command,
							expediteur);
					this.sendMsg(this.reponseKo("Unidentified command"));
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
					MsgServer
							.msgInfo("processing", "MAIL FROM ...", expediteur);
					// etatCourant = requete.processingEhlo(params);
					// TRANSAC_NO_DEST
					break;
				case "QUIT":
					MsgServer.msgInfo("processing", "QUIT ...", expediteur);
					isQuit = requete.processingQuit();
					break;
				default:
					MsgServer.msgWarnning("Unidentified command", command,
							expediteur);
					this.sendMsg(this.reponseKo("Unidentified command"));
					break;
				}
				break;

			default:
				MsgServer.msgWarnning("Unidentified etat",
						etatCourant.toString(), expediteur);
				break;
			}

		} else {
			MsgServer.msgWarnning("Invalid request form", null, expediteur);
			this.sendMsg(this.reponseKo("Invalid request form"));
		}

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

	@SuppressWarnings("unused")
	private String reponseOk(String msg) {
		return "+OK " + msg;
	}

	private String reponseKo(String msg) {
		return "-ERR " + msg;
	}

	private boolean sendMsg(String msg) {
		return sendToClient(msg + "\r\n");
	}

	private boolean sendToClient(String msg) {

		try {
			outDonnees.write(msg.getBytes(), 0, (int) msg.getBytes().length);
			outDonnees.flush();
			MsgServer.msgInfo("Send", msg, expediteur);
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
			MsgServer.msgError("IOException", e.getMessage(), expediteur);
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

		return requeteString[0];
	}

}
