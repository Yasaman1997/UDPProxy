import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	ServerSocket mServer;
	int serverPort = 9090;
	
	InputStream fromClientStream;
	OutputStream toClientStream;

	DataInputStream reader;
	PrintWriter writer;

	public Server() {

		try {
			// create server socket!
			mServer = new ServerSocket(serverPort);

			System.out.println("Server Created!");

			// wait for client
			// hold an object of Socket for each client
			Socket client = mServer.accept();

			// horaaaaa
			System.out.println("Connected to New Client!");

			// input stream (stream from client)
			fromClientStream = client.getInputStream();

			// output sream (stream to client)
			toClientStream = client.getOutputStream();

			reader = new DataInputStream(fromClientStream);
			writer = new PrintWriter(toClientStream, true);

			// send message to client
			writer.println("Salam Client joon");
			System.out.println("Server :Salam Client joon");

			// Receive client response (javab:D)
			String javab = reader.readLine();
			System.out.println("Client :" + javab);

				// send message to client (again)
			writer.println("khobi??");
			System.out.println("Server :khobi?");

			// Receive client response (javab:D)
			javab = reader.readLine();
			System.out.println("Client :" + javab);

		} catch (IOException e) {

		}

	}

	public static void main(String[] args) {
		new Server();
	}
}