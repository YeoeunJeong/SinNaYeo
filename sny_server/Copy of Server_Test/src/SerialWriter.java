import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Random;
import java.util.Scanner;

/**
 * ������������ ������
 */
public class SerialWriter extends Thread {
	private OutputStream out;
	// ��������������������� ������
	private BufferedReader in = null;// stream ��������� ��������� ��������� ��������� ��� ������ ������.
	
	public SerialWriter(OutputStream out, InputStream in) {
		this.out = out;
		this.in = new BufferedReader(new InputStreamReader(in));
	}

	public void run() {
		try {
			System.out.println("\nKeyborad Input Read!!!!");
			Scanner sc = new Scanner(System.in);
			String res;
			while (true) {
				String inputLine = ""; // in ������ ������������ ������������ ��������� string ������
				inputLine = in.readLine(); // in��� ��������� ������������ String ��������� ������ ���
				// ���������������
				// String��� ������
				// res = sc.nextLine();
				// for (int i = 0; i < inputLine.length(); i++)
				if (inputLine != null) {
					System.out.println("Read!!!!"+inputLine);	
					
					//���������������
					if (inputLine.charAt(0) == 'a') {
						String[] temp_hum = inputLine.split("-");
						StringBuilder builder = new StringBuilder(temp_hum[0]);
						temp_hum[0] = builder.deleteCharAt(0).toString();
						for (String s : temp_hum) {

							System.out.println(s);
						}
						this.out.write('5');

						this.out.write((byte) (Integer.parseInt(temp_hum[0])));
						this.out.write((byte) (Integer.parseInt(temp_hum[1])));
					} 
					else if (inputLine.charAt(0) == 'b'){
						StringBuilder builder = new StringBuilder(inputLine);
						
						
						
					}
					else if (inputLine.charAt(0) == '1'){
						Random rg = new Random();
						float temp = rg.nextInt() % 100;
						float hum = rg.nextInt() % 100;
						
						String s = temp + " " + hum;
						System.out.println(s);
						SerialHelper.sendAll(s);
						
					}
					else {
						this.out.write(inputLine.charAt(0));
					}
				}
				else{
					in.close();
				}

			}
		} catch (Exception e) {
			System.out.println("dfdf "+e.toString());
		}
	}
}