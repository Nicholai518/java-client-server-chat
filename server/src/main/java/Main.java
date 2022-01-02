import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Main {
	// code is very similar to client-project-one
	public static void main(String[] args) throws IOException {

		// Map to hold all our client connections
		// Should use ConcurrentHashMap because multiple threads could access at same time
		// ConcurrentHashMap helps avoid thread lock and other "thread safety" issues
		// Key, Value = port, serverThread
		Map<Integer, ServerThread> activeConnections = new ConcurrentHashMap<>();

		// main difference from client is this ServerSocket object
		// A server socket waits for requests to come in over the network.
		// Out client we made is trying to connect to port 1234
		// Thus, we want our server socket to be waiting for a connection on port 1234
		ServerSocket serverSocket = new ServerSocket(1234);

		// confirmation that server is up
		log.info("server started: {}", serverSocket.getLocalSocketAddress());

		// Another difference is having two while loops
		// The first while loop is to ensure the server is constantly running.
		// The second while loop is to ensure that, once the client is connected, the server is constantly interacting
		// with the client until the client disconnects. ( Sends the command "Shut down"
		while (!serverSocket.isClosed()) {

			try {
				// The accept() method of the ServerSocket class waits for a client connection
				// (the program won't advance until a client is connected).
				// Once connected, a Socket object is returned that can be used to communicate with the client
				Socket socket = serverSocket.accept();

				// Create a ServerThread using the socket for each connected client
				ServerThread serverThread = new ServerThread(socket);

				// add the severThread / connection to our map
				activeConnections.put(serverThread.getSocketPort(), serverThread);

				// Server console output
				log.info("client connected: {}", socket.getRemoteSocketAddress());

				// This is a consumer method
				// we are telling serverThread call the callback anytime it has input
				// "be ready for when we have input"
				serverThread.onMessage((input) -> {
					// log the client message
					log.info("message received from client. client: {}, msg: {}",
							serverThread.getSocketAddress(),
							input);

					// This is called a broadcast
					// send message to the other clients connected to server
					for (Map.Entry<Integer, ServerThread> entry : activeConnections.entrySet()) {
						ServerThread peer = entry.getValue();
						log.info("broadcasting message from {} to {}. msg: {}",
								serverThread.getSocketAddress(),
								peer.getSocketAddress(),
								input);
						peer.sendMessage(input);
					}
				});

				// calling start() method on thread
				// allows this thread to be started on a new core
				// which will call the .run() method
				serverThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
