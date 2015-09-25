import java.net.ServerSocket;
import java.net.Socket;

public class TwoWaySerialComm {
	public TwoWaySerialComm() {
		super();
	}
 
	public static void main(String[] args) {

		int count = 0;
		try {
			ServerSocket server = new ServerSocket(5555);
			System.out.println("Server : Server Create");
			while (true) {
				Socket client = server.accept();
				System.out
						.println("Server : Client " + (count++) + "Connected");
				ClientThread thread = new ClientThread(client);
				thread.start();
			}
		} catch (Exception e) {
			System.out.println("Server : " + e.toString());
		}
	}

}
