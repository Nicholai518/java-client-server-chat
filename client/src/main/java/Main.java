import java.io.IOException;
import java.net.Socket;


public class Main {
	public static void main(String[] args) {
		// create socket class
		Socket socket = null;

		try {
			// instantiate socket
			socket = new Socket("localhost", 1234);

			// create inbound and outbound threads
			InboundThread inboundThread = new InboundThread(socket);
			OutboundThread outboundThread = new OutboundThread(socket);

			inboundThread.start();
			outboundThread.start();

			while(!socket.isClosed()) {}

			// Confirmation that the client session has started
			System.out.println("The client session has started.");
			System.out.println("Type Commands below:");
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// The finally block is executed whether there is an exception or not.
		// In other words, it is always executed.
		// Therefore, it should contain all the crucial statements.
		// here, these are closing the streams
		finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
