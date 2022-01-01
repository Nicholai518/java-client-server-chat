import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		// create socket class
		Socket socket = null;

		// to communicate over a socket connection, streams are used.
		// stream = sequence of data. character stream = text files / byte stream = images or binary files
		// input stream = read data from
		// output stream = output data

		// we will read characters from the server such as "Message Received"
		InputStreamReader inputStreamReader = null;

		// this is how we will output data to the server
		OutputStreamWriter outputStreamWriter = null;

		// buffers help with efficiency
		// does NOT write one character at a time, instead writes a large block at a time
		// flushing a stream forces any buffered bytes to be written out OR read in
		// a buffer will only flush if full or forced to through code

		// EX: Instead of processing A-B-C-D-E-F one character at a time
		// a buffer will process the characters in batches of 3 [A, B, C] , [D, E, F]

		// the contents of the buffer, when full, get flushed to the InputStreamReader
		// which then gets flushed to the underlying InputStream

		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;


		// Confirmation that the client session has started
		System.out.println("The client session has started.");
		System.out.println("Type Commands below:");
		System.out.println();
		try {
			// instanciate socket
			socket = new Socket("localhost", 1234);

			// NOTE:
			// In java, character streams end with Reader or Writer.
			// Byte Streams end with stream
			// thus, InputStreamReader is a character stream
			// while socket.getInputStream() returns a byte stream
			// instanciate inputStreamReader
			inputStreamReader = new InputStreamReader(socket.getInputStream());

			outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

			// buffer to improve efficiency
			bufferedReader = new BufferedReader(inputStreamReader);
			bufferedWriter = new BufferedWriter(outputStreamWriter);

			// using Scanner class because we are taking in input from the console
			Scanner scanner = new Scanner(System.in);

			// this will run forever, this is an infinite loop
			// infinite look is broken by command "Shut down" which will use a break statement
			while (true) {
				System.out.print("> ");

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

				// The readLine() method of BufferedReader reads a line of text.
				// A line is considered to be terminated by a line feed (enter key or \n)
				// a carriage return (\r or tab key), or a carriage return followed immediately by a line feed

				// We are going to get a message BACK from the server "Message Received" which we will output
				System.out.println(bufferedReader.readLine());

				// When the break keyword is encountered inside a loop in Java, the loop is immediately terminated
				// and the program control resumes at the next statement following the loop.
				// the loop here is the while(true)
				if (messageToSend.equalsIgnoreCase("Shut down")) {

					// console output to notify client the session has ended
					System.out.println("Your session has been ended.");
					System.out.println(" ");

					break;
				}
			}
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
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (outputStreamWriter != null) {
					outputStreamWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
