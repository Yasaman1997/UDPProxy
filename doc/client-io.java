import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	Socket mSocket;
	int port = 9090;
	String serverAddress = "127.0.0.1";

	InputStream fromServerStream;
	OutputStream toServerStream;

	DataInputStream reader;
	PrintWriter writer;

	public Client() {
		try {

			mSocket = new Socket(serverAddress, port);

			System.out.println("connect to server ....");

			// input stream (stream from server)
			fromServerStream = mSocket.getInputStream();

			// output stream (stream to server)
			toServerStream = mSocket.getOutputStream();

			reader = new DataInputStream(fromServerStream);
			writer = new PrintWriter(toServerStream, true);

			// first : read server message
			String msg = reader.readLine();
			System.out.println("Server :" + msg);

			// send message to server
			writer.println("Salam Servere man");
			System.out.println("Client :Salam Servere man");

			// read server message
			msg = reader.readLine();
			System.out.println("Server :" + msg);

			// send message to server
			writer.println("Ohum!!!");
			System.out.println("Client :Ohum!!!");

		} catch (UnknownHostException e) {
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public static void main(String[] args) {
		new Client();
	}
}