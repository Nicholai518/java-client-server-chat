import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class OutboundThread extends Thread {

	// fields
	private final Socket socket;

	// constructors
	public OutboundThread(Socket socket) {
		this.socket = socket;
	}

	// methods
	public void run() {
		// this is how we will output data to the server
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bufferedWriter = null;

		try {
			outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
			bufferedWriter = new BufferedWriter(outputStreamWriter);

			// using Scanner class because we are taking in input from the console
			Scanner scanner = new Scanner(System.in);

			while (!socket.isClosed()) {
				// the clients message
				String messageToSend = scanner.nextLine();

				// The write() method of BufferedWriter writes the given argument, String in this case,
				// to the underlying writer which writes it to the underlying output stream.
				// Here that is the OutputStreamWriter and OutputStream respectively
				bufferedWriter.write(messageToSend);

				// The newLine() method of BufferedWriter writes a line separator to the underlying streams
				// This is important as the scanner's nextLine() method does not return a line separator
				bufferedWriter.newLine();

				// A buffer flushes its stream in each of these cases:
				// 1) The buffer is full
				// 2) the flush method is called  (this is what we are doing)
				// 3) the buffer stream is closed (the buffer is flushed before closing)
				// Note:
				// Buffers are normally used with large text files because the buffer flushes when full
				// The messages we are sending to the server will most likely not fill the buffer.
				// But we want the efficiency of the buffer anyway
				bufferedWriter.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close for security purposes
			try {
				if (socket != null) {
					socket.close();
				}

				if (outputStreamWriter != null) {
					outputStreamWriter.close();
				}

				if (bufferedWriter != null) {
					bufferedWriter.close();
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
}
