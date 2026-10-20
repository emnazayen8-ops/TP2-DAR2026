package TP2.serverPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2_1 {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Je suis un serveur en attente la connexion d'un client ");
            Socket socket = serverSocket.accept();
            System.out.println("un client est connecté");

            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);

            String operande1 = br.readLine();
            while (operande1 != null && !operande1.equals("quit")) {
                String operateur = br.readLine();
                String operande2 = br.readLine();
                System.out.println("Opération reçue : " + operande1 + " " + operateur + " " + operande2);

                String reponse;
                try {
                    reponse = calculer(Double.parseDouble(operande1), operateur,
                            Double.parseDouble(operande2));
                } catch (NumberFormatException e) {
                    reponse = "Erreur : opérande invalide";
                }

                pw.println(reponse);
                System.out.println("Réponse envoyée : " + reponse);

                operande1 = br.readLine();
            }

            System.out.println("Fin de la communication");
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }

    static String calculer(double a, String operateur, double b) {
        double r;
        switch (operateur) {
            case "+": r = a + b; break;
            case "-": r = a - b; break;
            case "*": r = a * b; break;
            case "/":
                if (b == 0) {
                    return "Erreur : division par zéro impossible";
                }
                r = a / b;
                break;
            default:
                return "Erreur : opérateur inconnu";
        }
        return a + " " + operateur + " " + b + " = " + r;
    }

    static String formater(double v) {
        if (v == Math.rint(v) && !Double.isInfinite(v)) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }
}