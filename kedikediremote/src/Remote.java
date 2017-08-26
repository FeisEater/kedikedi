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
		if (msg.startsWith("speeds")) {
			String parse = msg.substring(7);
			System.out.println(Integer.parseInt(parse.substring(0, parse.indexOf(" "))));
			parse = parse.substring(parse.indexOf(" ")+1);
			System.out.println(Integer.parseInt(parse.substring(0, parse.indexOf(" "))));
			parse = parse.substring(parse.indexOf(" ")+1);
			System.out.println(Integer.parseInt(parse));
		}
		try {
			dataOut.writeUTF(msg);
			dataOut.flush();
			System.out.println("Sent: " + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
