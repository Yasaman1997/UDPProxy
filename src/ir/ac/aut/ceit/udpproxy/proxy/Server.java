package ir.ac.aut.ceit.udpproxy.proxy;

import java.io.IOException;
import java.net.*;

public class Server extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256]; // maximum buffer size is 256 for incoming packets
    private byte[] res = new byte[1024]; // maximum buffer size is 1024 for http results
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
                continue; // try to read a new packet
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort(); // finds sender port in order to answer on

            // proxies the request to its destination. finds destination from Host option of HTTP packets.
            // Host option is a single line in request message so it parses thee message line by line in order
            // to find Host option
            String received
                    = new String(packet.getData(), 0, packet.getLength());

            // logs the arrived packet
            System.out.printf("Packet arrived from: %s:%d with content %s\n", address.toString(), port, received);

            String[] headers = received.split("\n");
            String host = "";
            for (String header : headers) {
                String[] parsedHeader = header.split(": ");
                if (parsedHeader[0].equals("Host")) { // Host option is found
                    host = parsedHeader[1];
                    break;
                }
            }
            if (host.isEmpty()) {
                continue;
                // This packet is not valid so Goodbye
            }
            System.out.printf("Found Host: %s\n", host);

            // Lets connect to the host that is found.
            try {
                Socket httpSocket = new Socket(host, 80);
                httpSocket.getOutputStream().write(buf); // write client packet without any change

                // read all of the http response
                int read = 0;
                int size = 0;
                do {
                    read += size;
                    size = httpSocket.getInputStream().read(res, read, res.length);
                } while (size > 0 && size < buf.length);

                // as you can google response is larger than 1024 so you must do segmentation
                System.out.printf("%s Response: %s\n", host, new String(res));

                // Check response status code
                // 404 indicates page not found and we must return a valid error
                // 301 and 302 indicate redirection and we must redirect to a given destination

            } catch (IOException e) {
                e.printStackTrace();
            }

            packet = new DatagramPacket(res, res.length, address, port); // answer packet

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
