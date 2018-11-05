package ir.ac.aut.ceit.udpproxy;

import ir.ac.aut.ceit.udpproxy.proxy.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Server().start(); // start the server thread

        Scanner stdin = new Scanner(System.in);
        while (true) {
            // read user input and sent it to proxy server with udp packets
            System.out.println("Please enter a valid url (aut.ac.ir):");
            String url = stdin.next(); // read a line from stdin
            // separate host and path from the given url
            String request = "GET / HTTP/1.1\n" + "Host: " + url + "\n\n"; // build a http request

            try {
                DatagramSocket client = new DatagramSocket();
                client.send(new DatagramPacket(request.getBytes(), request.length(), InetAddress.getLoopbackAddress(), 1373));
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Do you want to continue? (Y/n)");
            String answer = stdin.next();
            if (!answer.equals("Y")) {
                break;
            }
        }
        System.exit(0);
    }
}
