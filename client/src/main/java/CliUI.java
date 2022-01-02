import java.util.Date;

public class CliUI {

	LimitedQueue<String> messages = new LimitedQueue<>(10);

	public void addMessage(String message) {
		String prefix = "[" + new Date() + "]";
		messages.add(prefix + " " + message);
	}

	public void render() {
		clearConsole();
		renderHeader();
		renderDivider();
		renderMessages();
		renderDivider();
		System.out.print("> ");
	}

	private void renderHeader() {
		System.out.println("The client session is connected.");
	}

	private void renderDivider() {
		System.out.println("------------------------------------------------------------");
	}

	private void renderMessages() {
		this.messages.forEach(System.out::println);
	}

	public void clearConsole() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
}
