import java.io.IOException;
import java.net.Socket;


public class Main {
	public static void main(String[] args) {

		String host = "localhost";
		int port = 1234;

		if (args.length > 0) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}

		// create socket class
		Socket socket = null;

		CliUI ui = new CliUI();

		try {
			// instantiate socket
			socket = new Socket(host, port);

			// create inbound and outbound threads
			InboundThread inboundThread = new InboundThread(socket);
			OutboundThread outboundThread = new OutboundThread(socket);

			inboundThread.start();
			outboundThread.start();

			// render the "UI" initially
			ui.render();

			inboundThread.onMessageReceived((msg) -> {
				// update UI and re-render anytime we
				// receive a new message
				ui.addMessage(msg);
				ui.render();
			});

			// while loop keeps client from closing until the socket closes
			while (!socket.isClosed()) {}
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
