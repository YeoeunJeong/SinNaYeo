import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * ��������� ������
 */
public class SerialReader extends Thread {
	
	// ��������������� ��������������� ������
	private BufferedReader reader = null;
	public SerialReader(InputStream in) {
		this.reader = new BufferedReader(new InputStreamReader(in));
	}
	

	public void run() {
		String str = "";
		try {
			while (true) {
				str = reader.readLine();
				GCMServerSide.sendMessage("������!!!");
				if (!(str.equals("��������� ���������") || str.equals("��������� ������������") || str.equals("������") || str.equals("������"))){
//					GCMServerSide.sendMessage();
					SerialHelper.sendAll(str);
				}
				
				System.out.println("read: " + str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

