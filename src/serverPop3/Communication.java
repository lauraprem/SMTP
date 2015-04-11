package serverPop3;

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

import serverPop3.requete.Requete;
import util.Lock.Lock;
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
	private String user;

	private static final int uneMinute = 60000;

	public Communication(Socket connexion) {
		SO_TIMEOUT = 10 * uneMinute;
		socket = connexion;
		try {
			socket.setSoTimeout(SO_TIMEOUT);
		} catch (SocketException ex) {
			MsgServer.msgError("socket time-out", ex.getMessage(), user);
			this.sendMsg(this.reponseKo("socket time-out"));
		}

		// flux
		in = null;
		out = null;
		outDonnees = null;

		// Autre
		requete = null;
		finRequete = "\r\n";
		// finRequete = "<CR><LF>";
		user = socket.toString();
		etatCourant = Etat.AUTORISATION;
	}

	@Override
	public void run() {

		// Console connexion TCP correct
		MsgServer.msgConnect(true, user);

		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			outDonnees = new BufferedOutputStream(socket.getOutputStream());

			requete = new Requete(outDonnees);

			// Envoi Message de bienvenue
			String msg = "+OK Serveur POP3 ready" + finRequete;
			outDonnees.write(msg.getBytes(), 0, (int) msg.getBytes().length);
			outDonnees.flush();
			MsgServer.msgInfo("Send", msg, user);

			// Permet de savoir si la connexion est � cl�turer
			boolean isQuit = false;
			while (!isQuit) {

				// recupere la premiere ligne de la requete du client
				// String ligne = in.readLine();
				String ligne = readLine(); // in.readLine();
				// if(user.equals(socket))
				MsgServer.msgInfo("Request receive", ligne, user);

				if (ligne != null) {
					isQuit = processingRequest(ligne);
				} else {
					isQuit = true;
				}
			}

		} catch (SocketTimeoutException e) {
			System.out.println(user + " time_out dépassé : " + e.getMessage());

			// erreur(408);
		} catch (IOException ex) {
			System.out.println(user + " Error : " + ex.getMessage());
			// erreur(500);
		} finally {
			if (MsgServer.isUserFormat(user) && Lock.existUser(user)) {
				Lock.unlock(user);
			}
			close(in);
			close(out);
			close(outDonnees);
			close(socket);
		}
		MsgServer.msgConnect(false, user);
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
		if (receive.length() >= 4) {  // si fin \r\n
			String command = receive.substring(0, 4);
			String params = receive.substring(4);
			MsgServer.msgInfo("Command receive", command, user);
			MsgServer.msgInfo("Params receive", params, user);

			switch (etatCourant) {
			case AUTORISATION:
				switch (command) {
				case "APOP":
					MsgServer.msgInfo("processing", "APOP ...", user);
					etatCourant = requete.processingApop(params);

					// R�cup�ration des mails
					if (etatCourant == Etat.TRANSACTION) {
						user = requete.getApop().getUser();
					}
					break;
				case "QUIT":
					MsgServer.msgInfo("processing", "QUIT ...", user);
					requete.getQuit().setUser(user);
					isQuit = requete.processingQuit();
					break;
				default:
					MsgServer
							.msgWarnning("Unidentified command", command, user);
					this.sendMsg(this.reponseKo("Unidentified command"));
					break;
				}
				break;

			case TRANSACTION:
				switch (command) {
				case "RETR":
					MsgServer.msgInfo("processing", "RETR ...", user);
					requete.processingRetr(params);
					break;
				case "QUIT":
					MsgServer.msgInfo("processing", "QUIT ...", user);
					isQuit = requete.processingQuit();
					break;
				default:
					MsgServer
							.msgWarnning("Unidentified command", command, user);
					this.sendMsg(this.reponseKo("Unidentified command"));
					break;
				}
				break;

			default:
				MsgServer.msgWarnning("Unidentified etat",
						etatCourant.toString(), user);
				break;
			}

		} else {
			MsgServer.msgWarnning("Invalid request form", null, user);
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
				System.err.println("Unable to close object: " + stream);
			}
		} catch (Exception e) {
			System.err.println("Error closing stream: " + e);
		}
	}

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
			MsgServer.msgInfo("Send", msg, user);
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
			MsgServer.msgError("IOException", e.getMessage(), user);
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
