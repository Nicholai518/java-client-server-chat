import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	// code is very similar to client-project-one
	public static void main(String[] args) throws IOException {

		// create socket class
		Socket socket = null;
		// we will read characters from the client such as "Hello" or "Shut down"
		InputStreamReader inputStreamReader = null;
		// this is how we will output data to the client
		OutputStreamWriter outputStreamWriter = null;

		// buffers help with efficiency
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		// main difference from client is this ServerSocket object
		// A server socket waits for requests to come in over the network.
		// Out client we made is trying to connect to port 1234
		// Thus, we want our server socket to be waiting for a connection on port 1234
		ServerSocket serverSocket = null;
		serverSocket = new ServerSocket(1234);

		// confirmation that server is up
		System.out.println("Server running.");

		// Another difference is having two while loops
		// The first while loop is to ensure the server is constantly running.
		// The second while loop is to ensure that, once the client is connected, the server is constantly interacting
		// with the client until the client disconnects. ( Sends the command "Shut down"
		while (true) {

			try {
				// The accept() method of the ServerSocket class waits for a client connection
				// (the program won't advance until a client is connected).
				// Once connected, a Socket object is returned that can be used to communicate with the client
				socket = serverSocket.accept();

				System.out.println("");
				System.out.println("A client is now connected to the server.");
				System.out.println("");

				inputStreamReader = new InputStreamReader(socket.getInputStream());
				outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

				bufferedReader = new BufferedReader(inputStreamReader);
				bufferedWriter = new BufferedWriter(outputStreamWriter);

				// second while loop
				while (true) {
					String messageFromClient = bufferedReader.readLine();

					// output message to the console
					System.out.println("client: " + messageFromClient);

					String response = "echo: " + messageFromClient;

					// send response back to client
					bufferedWriter.write(response);
					// adds a new line for spacing / formatting purposes
					bufferedWriter.newLine();
					// forcing to flush
					bufferedWriter.flush();

					// checking for shut down message, this will break while loop
					// this affects the current client
					// the first while loop still runs, waiting for the next client
					if (messageFromClient.equalsIgnoreCase("Shut down")) {
						System.out.println(" ");
						System.out.println("------------------------------------");
						System.out.println("Client Session has ended.");
						System.out.println("Server now waiting for new client.");
						System.out.println("-----------------------------------");
						System.out.println("");

						break;
					}
				}

				// close
				socket.close();
				inputStreamReader.close();
				outputStreamWriter.close();
				bufferedReader.close();
				bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
