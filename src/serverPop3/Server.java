package serverPop3;

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
        portServeur = 110;
        initSocketServeur();
    }

    // METHODES
    public void initSocketServeur() {
        try {
            socketServeur = new ServerSocket(portServeur, 6);// nombre maximum de client connect�
    		System.out.println("Server started ......");
        } catch (IOException ex) {
            System.err.println("Port d�j� occup� : " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket connexion;

                // Attente de demande de connexion
                connexion = socketServeur.accept();

                // Cr�ation d'une communication
                Communication com = new Communication(connexion);
                
                // Activation de la communication
                com.start();

            } catch (IOException ex) {
                System.out.println("Probl�me de connection : " + ex.getMessage());
            }
        }
    }
}
