package TP2.serverPackage;

import TP2.modelPackage.Operation;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2_2 {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1235)) {
            System.out.println("Serveur (objets) en attente d'un client...");
            Socket socket = serverSocket.accept();
            System.out.println("Un client est connecté");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Object recu = in.readObject();
                if (recu instanceof String && recu.equals("FIN")) {
                    break;
                }
                Operation op = (Operation) recu;
                System.out.println("Opération reçue : " + op);

                String resultat = calculer(op);
                out.writeObject(resultat);
                out.flush();
                System.out.println("Envoyé : " + resultat);
            }

            System.out.println("Fin de session, fermeture.");
            in.close();
            out.close();
            socket.close();

        } catch (EOFException e) {
            System.out.println("Le client s'est déconnecté brutalement.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String calculer(Operation op) {
        double a = op.getOperande1();
        double b = op.getOperande2();
        double r;
        switch (op.getOperateur()) {
            case '+':
                r = a + b;
                break;
            case '-':
                r = a - b;
                break;
            case '*':
                r = a * b;
                break;
            case '/':
                if (b == 0) return "Erreur : division par zéro";
                r = a / b;
                break;
            default:
                return "Erreur : opérateur inconnu";
        }
        return "Résultat = " + formater(r);
    }

    private static String formater(double r) {
        if (r == Math.rint(r) && Math.abs(r) < 1e15) {
            return String.valueOf((long) r);
        }
        return String.valueOf(r);
    }
}
