package TP2.clientPackage;

import TP2.modelPackage.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client2_2 {

    private static final Pattern OPERATION = Pattern.compile(
            "^(-?\\d+(?:\\.\\d+)?)\\s*([+\\-*/])\\s*(-?\\d+(?:\\.\\d+)?)$");

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1235);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connecté au serveur.");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                System.out.print("Opération (ex : 55 * 25) ou 'exit' pour quitter : ");
                String ligne = sc.nextLine().trim();

                if (ligne.equalsIgnoreCase("exit")) {
                    out.writeObject("FIN");
                    out.flush();
                    break;
                }

                Matcher m = OPERATION.matcher(ligne);
                if (!m.matches()) {
                    System.out.println("Syntaxe incorrecte, rien n'est envoyé au serveur.");
                    continue;
                }
                
                Operation op = new Operation(
                        Double.parseDouble(m.group(1)),
                        m.group(2).charAt(0),
                        Double.parseDouble(m.group(3)));

                out.writeObject(op);
                out.flush();

                String resultat = (String) in.readObject();      // (g) lecture
                System.out.println(resultat);
            }

            System.out.println("Fermeture de la connexion.");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
