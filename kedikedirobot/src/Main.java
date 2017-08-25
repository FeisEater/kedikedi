import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.DifferentialPilot;

public class Main {
	static int fullSpeed = 540;
	static int turnSpeed = 90;
	static int staticTurnSpeed = 360;

	public static void main (String[] args) {
		LCD.clear();
		LCD.drawString("Waiting", 0, 0);
		BTConnection connection = Bluetooth.waitForConnection(); // this method is very patient. 
		LCD.clear();
		LCD.drawString("Connected", 0, 0);
		DataInputStream dataIn = connection.openDataInputStream();
		DataOutputStream dataOut = connection.openDataOutputStream();
		Sound.beepSequence();
		DifferentialPilot pilot = new DifferentialPilot(2.1f, 4.4f, Motor.A, Motor.B);
		Motor.A.setSpeed(0);
		Motor.B.setSpeed(0);
		for (;;)
		{
			try {
				String received = dataIn.readUTF();
				switch (received) {
					case "grabber":
						doGrabber();
						break;
					case "start_forward":
						setMovement(1,0);
						break;
					case "start_backward":
						setMovement(-1,0);
						break;
					case "start_left":
						setMovement(0,-1);
						break;
					case "start_right":
						setMovement(0,1);
						break;
					case "stop_forward":
						setMovement(-1,0);
						break;
					case "stop_backward":
						setMovement(1,0);
						break;
					case "stop_left":
						setMovement(0,1);
						break;
					case "stop_right":
						setMovement(0,-1);
						break;
					default:
						break;
				}
				int leftSpeed = 0;
				int rightSpeed = 0;
				boolean leftForward = true;
				boolean rightForward = true;
				if (forward == 1) {
					if (right == 1) {
						leftSpeed = turnSpeed;
						rightSpeed = fullSpeed;
						leftForward = true;
						rightForward = true;
					} else if (right == -1) {
						leftSpeed = fullSpeed;
						rightSpeed = turnSpeed;
						leftForward = true;
						rightForward = true;
					} else {
						leftSpeed = fullSpeed;
						rightSpeed = fullSpeed;
						leftForward = true;
						rightForward = true;
					}
				} else if (forward == -1) {
					if (right == 1) {
						leftSpeed = -turnSpeed;
						rightSpeed = -fullSpeed;
						leftForward = false;
						rightForward = false;
					} else if (right == -1) {
						leftSpeed = -fullSpeed;
						rightSpeed = -turnSpeed;
						leftForward = false;
						rightForward = false;
					} else {
						leftSpeed = -fullSpeed;
						rightSpeed = -fullSpeed;
						leftForward = false;
						rightForward = false;
					}
				} else {
					if (right == 1) {
						leftSpeed = -staticTurnSpeed;
						rightSpeed = staticTurnSpeed;
						leftForward = false;
						rightForward = true;
					} else if (right == -1) {
						leftSpeed = staticTurnSpeed;
						rightSpeed = -staticTurnSpeed;
						leftForward = true;
						rightForward = false;
					} else {
						leftSpeed = 0;
						rightSpeed = 0;
					}
				}
				Motor.A.setSpeed(leftSpeed);
				Motor.B.setSpeed(rightSpeed);
				if (leftForward) Motor.A.forward(); else Motor.A.backward();
				if (rightForward) Motor.B.forward(); else Motor.B.backward();
				//Sound.beep();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Oops");
				try {dataIn.close();} catch (IOException ignore) {}
				try {dataOut.close();} catch (IOException ignore) {}
				connection.close();
				break;
			}
		}
		try {dataIn.close();} catch (IOException ignore) {}
		try {dataOut.close();} catch (IOException ignore) {}
		connection.close();
	}
	
	static boolean grabbing = false;
	public static void doGrabber() {
		Motor.C.setSpeed(360);
		Motor.C.rotate(grabbing ? -360 : 360, false);
		grabbing = !grabbing;
	}
	
	static int forward = 0;
	static int right = 0;
	public static void setMovement(int fdir, int rdir) {
		forward += fdir;
		right += rdir;
		if (forward >= 1) forward = 1;
		else if (forward <= -1) forward = -1;
		if (right >= 1)	right = 1;
		else if (right <= -1)	right = -1;		
	}
}
