import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

public class Remote {
	private boolean ready;
	private DataOutputStream dataOut;
	
	public Remote() {
		ready = false;
	}
	public void run() {
		NXTConnector connector = new NXTConnector();
		if (!connector.connectTo())
			System.err.println("Fucked up");
		dataOut = new DataOutputStream(connector.getOutputStream());
		ready = true;
	}
	
	public void stop() {
		ready = false;
		try {dataOut.close();} catch(IOException ignore) {}
	}
	
	public void sendMsg(String msg) {
//		System.out.println(msg);
		try {
			dataOut.writeUTF(msg);
			dataOut.flush();
			System.out.println("Sent: " + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
