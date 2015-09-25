import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerialHelper {
	public static SerialPort g_SerialPort;
	public static InputStream g_SerialIn;
	public static OutputStream g_SerialOut;
	public static SerialHelper serialHelper = new SerialHelper();
	public static SerialReader m_reader;
	public static Time start_time;
	public static List<OutputStream> list = Collections
			.synchronizedList(new ArrayList<OutputStream>());

	public static SerialHelper getInstance() {
		if (serialHelper == null) {
			serialHelper = new SerialHelper();
		}
		return serialHelper;
	}

	private SerialHelper() {
		if (g_SerialPort == null) {
			CommPortIdentifier portIdentifier;
			try {
				portIdentifier = CommPortIdentifier
						.getPortIdentifier("/dev/cu.usbmodem411");
				if (portIdentifier.isCurrentlyOwned()) {
					System.out.println("Error: Port is currently in use");
				} else {
					CommPort commPort = portIdentifier.open(this.getClass()
							.getName(), 2000);

					if (commPort instanceof SerialPort) {
						g_SerialPort = (SerialPort) commPort;
						g_SerialPort.setSerialPortParams(115200, // ������������
								SerialPort.DATABITS_8, // ��������� ������
								SerialPort.STOPBITS_1, // stop ������
								SerialPort.PARITY_NONE); // ���������

						// ������ ���������
						g_SerialIn = g_SerialPort.getInputStream();

						// ������ ���������
						g_SerialOut = g_SerialPort.getOutputStream();

						m_reader = new SerialReader(serialHelper.g_SerialIn);
						m_reader.start();
					}
				}
			} catch (Exception e) {
				System.out.println("Server Error" + e.toString());
			}
		}
	}

	public static void sendAll(String str) {
		for (OutputStream out : list) {
			new PrintWriter(out, true).println(str);
		}
	}

	public void addList(OutputStream outputStream) {
		list.add(outputStream);
	}

}