package ir.ac.aut.ceit.udpproxy.proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
            packet = new DatagramPacket(buf, buf.length, address, port); // answer packet

            // proxies the request to its destination. finds destination from Host option of HTTP packets.
            // Host option is a single line in request message so it parses thee message line by line in order
            // to find Host option
            String received
                    = new String(packet.getData(), 0, packet.getLength());

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
