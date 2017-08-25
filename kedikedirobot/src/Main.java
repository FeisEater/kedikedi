import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class Main {
	public static void main (String[] args) {
		LCD.clear();
		LCD.drawString("Waiting", 0, 0);
		BTConnection connection = Bluetooth.waitForConnection(); // this method is very patient. 
		LCD.clear();
		LCD.drawString("Connected", 0, 0);
		DataInputStream dataIn = connection.openDataInputStream();
		DataOutputStream dataOut = connection.openDataOutputStream();
		Sound.beepSequence();
		for (;;)
		{
			try {
				System.out.println(dataIn.readUTF());
				Sound.beep();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Oops");
			}
		}
	}
}
