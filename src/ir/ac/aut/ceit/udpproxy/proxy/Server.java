package ir.ac.aut.ceit.udpproxy.proxy;

import java.io.IOException;
import java.net.*;

public class Server extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256]; // maximum buffer size is 256
    // TODO: packet segmentation

    /**
     * Creates new proxy server that listens on 1373/udp and binds on loopback interface
     * TODO: bind on all interfaces
     */
    public Server() {
        try {
            socket = new DatagramSocket(1373, InetAddress.getLoopbackAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        running = true;

        while (running) {
            // in each iteration waits for new packet then handles it
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort(); // finds sender port in order to answer on

            // proxies the request to its destination. finds destination from Host option of HTTP packets.
            // Host option is a single line in request message so it parses thee message line by line in order
            // to find Host option
            String received
                    = new String(packet.getData(), 0, packet.getLength());
            String[] headers = received.split("\n");
            String host = "";
            for (String header : headers) {
                String[] parsedHeader = header.split(":");
                if (parsedHeader[0].equals("Host:")) { // Host option is found
                    host = parsedHeader[1];
                    break;
                }
            }
            if (host.isEmpty()) {
                continue;
                // This packet is not valid so Goodbye
            }

            // Lets connect to the host that is found.
            try {
                Socket httpSocket = new Socket(host, 80);
            } catch (IOException e) {
                e.printStackTrace();
            }

            packet = new DatagramPacket(buf, buf.length, address, port); // answer packet

            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        socket.close();
    }

    @Override
    public void interrupt() {
        super.interrupt();
        this.running = false;
    }
}
