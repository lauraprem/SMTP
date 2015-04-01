package smtpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Corinne & Laura
 */
public class Server extends Thread {

    // ATTRIBUTS
    private ServerSocket socketServeur;
    private int portServeur;
    
    // CONSTRUCTEUR
    public Server() {
        portServeur = 25;
        initSocketServeur();
    }

    // METHODES
    public void initSocketServeur() {
        try {
            socketServeur = new ServerSocket(portServeur, 6);// nombre maximum de client connecté
    		System.out.println("Server started ......");
        } catch (IOException ex) {
            System.err.println("Port déjà occupé : " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket connexion;

                // Attente de demande de connexion
                connexion = socketServeur.accept();

                // Création d'une communication
                Communication com = new Communication(connexion);
                
                // Activation de la communication
                com.start();

            } catch (IOException ex) {
                System.out.println("Problème de connection : " + ex.getMessage());
            }
        }
    }
}
