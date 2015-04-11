package util.FileMails;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class FileMails implements Serializable {

	// ATTRIBUTS
	private String name;
	private String extension;
	private String location;
	private ArrayList<Mail> listMail;

	// private String DEFAULT_ENCODING = "utf-8";

	// CONSTRUCTEURS
	public FileMails(String name, String extension, String location,
			ArrayList<Mail> listMail) {
		this.name = name;
		this.extension = extension;
		this.location = location;
		this.listMail = listMail;
	}

	public FileMails(String name, String extension, String location) {
		super();
		this.name = name;
		this.extension = extension;
		this.location = location;
		this.listMail = new ArrayList<Mail>();
	}
	
	public FileMails(String extension, String location) {
		super();
		this.name = null;
		this.extension = extension;
		this.location = location;
		this.listMail = new ArrayList<Mail>();
	}

	// METHODES
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ArrayList<Mail> getListMail() {
		return listMail;
	}

	public void setListMail(ArrayList<Mail> listMail) {
		this.listMail = listMail;
	}

	public Mail getMail(int num) {
		if (!listMail.isEmpty()) {
			for (Mail mail : listMail) {
				if (mail.getId() == num) {
					return mail;
				}
			}
		}
		return null;
	}

	public int CalculationSizeMail(int num) {

		// Recherche du mail
		Mail mail = getMail(num);

		if (mail != null) {
			return mail.CalculationSizeMail();
		}

		return 0;
	}

	public boolean extractMails() {
		String path = location + name + extension;
		File fichier = new File(path);
		ObjectInputStream ois;

		try {
			ois = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(fichier)));

			// R�cup�ration des mails
			int i =1;
			while (true) {
				try {
					Mail mail = (Mail) ois.readObject();
					mail.setId(i);
					i++;
					this.listMail.add(mail);
				} catch (EOFException e) {
					break;
				}
			}

			ois.close();

		} catch (java.io.EOFException e) { // fichier vide d�s d�but => 0 mails
			// e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			return false;
		} catch (IOException e) {
			// e.printStackTrace();
			return false;
		}

		return true;
	}

	public void saveMails() {
		String path = location + name + extension;
		File fichier = new File(path);
		ObjectOutputStream oos;

		try {
			// premi�re �criture lors cr�ation du fichier
			oos = new ObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(fichier)));

			// Ecrit � la suite
			// oos = new AppendObjectOutputStream(new
			// FileOutputStream(fichier));

			// Ecrire chaque objet Mail dans le fichier			
			for (int i = 0; i < listMail.size(); i++) {
				oos.writeObject(listMail.get(i));
			}
			

//			listHeader = new ArrayList<String>();
//			listHeader.add("Message-ID: <1234@local.machine.example");
//			listHeader.add("From: John Doe <jdoe@machine.example>");
//			listHeader.add("Subject: C'est un essai 2");
//			listLine = new ArrayList<String>();
//			listLine.add("C'est un message juste pour tester. Alors, \"Test\".");
//			oos.writeObject(new Mail(2, listHeader, listLine));
//
//			listHeader = new ArrayList<String>();
//			listHeader.add("Message-ID: <1234@local.machine.example>");
//			listHeader.add("From: John Doe <jdoe@machine.example>");
//			listHeader.add("Subject: C'est un essai 3");
//			listLine = new ArrayList<String>();
//			listLine.add("C'est un message juste pour tester. Alors, \"Test\".");
//			oos.writeObject(new Mail(3, listHeader, listLine));

			oos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void CreateSaveMails() {

		String path = location + name + extension;
		// TODO Cr�er le fichier
		File fichier = new File(path);
		ObjectOutputStream oos;

		try {
			// premi�re �criture lors cr�ation du fichier
			oos = new ObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(fichier)));

			// Ecrire chaque objet Mail dans le fichier
			// ArrayList<String> listLine = new ArrayList<String>();
			// listLine.add("C'est un message juste pour tester. Alors, \"Test\".<CR><LF>");
			// oos.writeObject(new Mail(1234, listHeader, listLine));

			oos.close();

		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
	}

	public void supressMail() {
		listMail = new ArrayList<Mail>();
	}
	
	public void addMail(Mail m) {
		listMail.add(m);
	}

	// public void extractMails() {
	// // Creer un fichier
	// String path = location+name;
	// File fichier = new File(path);
	//
	// FileInputStream fileIn = null;
	//
	// // Creer une liste de byte pour enregister les donnees du fichier
	// byte[] fichierDonnees = new byte[(int) fichier.length()];
	//
	// try {
	// // On ouvre le fichier et on le lit
	// fileIn = new FileInputStream(fichier);
	// fileIn.read(fichierDonnees);
	// fileIn.close();
	//
	// // Convertion en String
	// String content = new String(fichierDonnees,DEFAULT_ENCODING);
	//
	// return true;
	// } catch (FileNotFoundException ex) {
	// } catch (IOException ex) {
	// }
	// return false;
	// }

}
