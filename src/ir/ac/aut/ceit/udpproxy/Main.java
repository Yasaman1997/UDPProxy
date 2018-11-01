package ir.ac.aut.ceit.udpproxy;

import ir.ac.aut.ceit.udpproxy.proxy.Server;

public class Main {

    public static void main(String[] args) {
        new Server().start(); // start the server thread
    }
}
