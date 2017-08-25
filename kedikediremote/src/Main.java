import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import lejos.pc.comm.NXTConnector;

public class Main {
	public static void main (String[] args) {
		NXTConnector connector = new NXTConnector();
		if (!connector.connectTo())
			System.err.println("Fucked up");
		Scanner reader = new Scanner(System.in);
		DataOutputStream dataOut = new DataOutputStream(connector.getOutputStream());
		for (;;)
		{
			try {
				String send = reader.nextLine();
				dataOut.writeUTF(send);
				dataOut.flush();
				System.out.println("Sent: " + send);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
