import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.function.Consumer;

public class InboundThread extends Thread {

	// fields
	private final Socket socket;
	private Consumer<String> onMessageReceivedCallback;

	// constructors
	public InboundThread(Socket socket) {
		this.socket = socket;
	}

	// methods
	public void run() {
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			inputStreamReader = new InputStreamReader(socket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			while (!socket.isClosed()) {
				// We are going to receive messages from the server
				String message = bufferedReader.readLine();
				if (this.onMessageReceivedCallback != null) {
					this.onMessageReceivedCallback.accept(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close for security purposes
			try {
				if (socket != null) {
					socket.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}

				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// accessors
	public Socket getSocket() {
		return socket;
	}

	public void onMessageReceived(Consumer<String> onMessageReceivedCallback) {
		this.onMessageReceivedCallback = onMessageReceivedCallback;
	}
}
