import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

// The `Slf4j` annotation provides this class with access to a logger
@Slf4j
public class ServerThread extends Thread {

	// fields
	private String input = null;
	private BufferedReader inputStream = null;
	private BufferedWriter outputStream = null;

	// final means this field cannot be re-assigned
	private final Socket socket;
	private Consumer<String> onMessageCallback;

	// constructors
	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	// methods
	public void run() {

		// command is used by client to quit their session
		final String QUIT_COMMAND = "QUIT";

		// used to log thread information
		// EX: Thread 1, 0.0.0.0:8765
		// 0.0.0.0 is local host
		// client port: 8765
		// the server port being used is 1234
		log.info("client thread started: {}, {}", this.getName(), socket.getRemoteSocketAddress());

		// try to instanciate inputStream, using a BufferedReader for efficiency
		// try to instanciate outputStream, using a BufferedWriter for efficiency
		try {
			inputStream = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			outputStream = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			log.error("failed to establish IO", e);
		}

		try {
			// read the client message, store in the input field
			input = inputStream.readLine();

			// while the socket is NOT closed and the client message is NOT QUIT_COMMAND
			while (!socket.isClosed() && !input.equalsIgnoreCase(QUIT_COMMAND)) {

				if (this.onMessageCallback != null) {
					this.onMessageCallback.accept(input);
				}

				// wait for a new message from the client, then while loop will run again if conditions are met
				input = inputStream.readLine();
			}
		} catch (IOException e) {
			// if the client "disappeared" and the connection is lost to the server
			log.error("{} terminated abruptly", this.getName(), e);
		} catch (NullPointerException e) {
			// the client closed the connection with the server while was performing tasks
			log.error("client {} closed abruptly", this.getName(), e);
		} finally {
			try {
				// if client successfully closes connection, log it
				// closing streams for security purposes
				log.info("client closing: {}, {}", this.getName(), socket.getRemoteSocketAddress());

				if (inputStream != null) {
					inputStream.close();
					log.info("input stream closed");
				}

				if (outputStream != null) {
					outputStream.close();
					log.info("output stream closed");
				}

				if (socket != null) {
					socket.close();
					log.info("socket closed");
				}
			} catch (IOException e) {
				log.error("socket close error", e);
			}
		}
	}

	public void onMessage(Consumer<String> callback) {
		this.onMessageCallback = callback;
	}

	public void sendMessage(String response) {
		try {
			// writing the response to the buffer
			outputStream.write(response);
			// this indicates that the response / message is complete. This is a terminating character
			outputStream.newLine();
			// flush the buffer, which will send this response over the network
			outputStream.flush();
		} catch (IOException e) {
			log.error("failed to send message {}, {}",
					this.getName(),
					this.socket.getRemoteSocketAddress(),
					e);
		}
	}

	// getters / accessors
	public int getSocketPort() {
		return socket.getPort();
	}

	public SocketAddress getSocketAddress() {
		return socket.getRemoteSocketAddress();
	}
}
