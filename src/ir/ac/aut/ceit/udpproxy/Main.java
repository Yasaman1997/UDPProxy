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

        // read user input and sent it to proxy server with udp packets
        System.out.println("Please enter a valid url (http://aut.ac.ir/) that ends with slash:");
        Scanner stdin = new Scanner(System.in);
        String url = stdin.next(); // read a line from stdin
        // separate host and path from the given url
        String request = "GET / HTTP/1.1\n" + "Host: " + url; // build a http request

        try {
            DatagramSocket client = new DatagramSocket();
            client.send(new DatagramPacket(request.getBytes(), request.length(), InetAddress.getLoopbackAddress(), 1373));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
