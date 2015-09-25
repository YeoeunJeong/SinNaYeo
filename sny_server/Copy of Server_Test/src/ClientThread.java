import java.net.Socket;

public class ClientThread extends Thread {

	private Socket socket = null;
	private SerialWriter m_writer;

	public ClientThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			SerialHelper serialHelper =  SerialHelper.getInstance();	
			m_writer = new SerialWriter(serialHelper.g_SerialOut,socket.getInputStream());
			serialHelper.addList(socket.getOutputStream());
			//m_reader.start();
			m_writer.start();
		}
		catch(Exception e)
		{
			System.out
			.println("Error: Only serial ports are handled by this example. \n"+e.toString());
			
		}
	}
}
