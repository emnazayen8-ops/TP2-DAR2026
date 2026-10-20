package TP2.clientPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client2_1 {

    static final Pattern SYNTAXE = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+\\-*/])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$");

    public static void main(String[] args) {
        try {
            System.out.println("Je suis un client pas encore connecté…");
            Socket socket = new Socket("localhost", 1234);
            System.out.println("je suis un client connecté");

            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("Donnez une opération (ex : 55 * 25) ou quit : ");
                String saisie = sc.nextLine().trim();
                if (saisie.equals("quit")) {
                    break;
                }

                Matcher m = SYNTAXE.matcher(saisie);
                if (!m.matches()) {
                    System.out.println("Syntaxe invalide, rien n'est envoyé.");
                    continue;
                }

                pw.println(m.group(1));
                pw.println(m.group(2));
                pw.println(m.group(3));

                String reponse = br.readLine();
                System.out.println("Résultat : " + reponse);
            }

            pw.println("quit");
            System.out.println("Fin de la communication");
            sc.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}
