import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.DifferentialPilot;

public class Main {
	public static int fullSpeed = 720;
	public static int turnSpeed = 180;
	public static int staticTurnSpeed = 360;
	public static BTConnection connection;
	public static DataInputStream dataIn;
	public static DataOutputStream dataOut;
	public static boolean spinnyMode = false;
	public static boolean running = true;
	public static int spinnyTravel = 0;
	public static void main (String[] args) {
		LCD.clear();
		LCD.drawString("Waiting", 0, 0);
		connection = Bluetooth.waitForConnection(); // this method is very patient. 
		LCD.clear();
		LCD.drawString("Connected", 0, 0);
		dataIn = connection.openDataInputStream();
		dataOut = connection.openDataOutputStream();
		MsgReceiver recv = new MsgReceiver();
		recv.setDaemon(true);
		recv.start();
		Sound.beepSequence();
		DifferentialPilot pilot = new DifferentialPilot(2.1f, 4.4f, Motor.A, Motor.B);
		Motor.A.setSpeed(0);
		Motor.B.setSpeed(0);
		UltrasonicSensor ultra = new UltrasonicSensor(SensorPort.S1);
		TouchSensor touch = new TouchSensor(SensorPort.S2);
		int leftSpeed = 0;
		int rightSpeed = 0;
		boolean leftForward = true;
		boolean rightForward = true;
		long turnTiming = 0;
		while (running) {
			if (spinnyMode) {
				boolean turnRight = false;
				if (Motor.A.getTachoCount() > 1800)
					turnRight = true;
				//if (ultra.getDistance() <= 45 || touch.isPressed()) {
				//	turnTiming = System.currentTimeMillis() + 500;
				//}
				if (ultra.getDistance() <= 30 || touch.isPressed()/* || System.currentTimeMillis() < turnTiming*/) {
					if (turnRight) {
						Motor.A.stop();
						Motor.B.stop();
						//Motor.A.backward();
						//Motor.B.forward();
					} else {
						Motor.A.forward();
						Motor.B.backward();
					}
				} else {
					Motor.A.setSpeed(fullSpeed * 2 / 3);
					Motor.B.setSpeed(fullSpeed * 2 / 3);
					Motor.A.backward();
					Motor.B.backward();
				}
			} else {
				if (forward == 1) {
					if (right == 1) {
						leftSpeed = fullSpeed;
						rightSpeed = turnSpeed;
						leftForward = true;
						rightForward = true;
					} else if (right == -1) {
						leftSpeed = turnSpeed;
						rightSpeed = fullSpeed;
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
						leftSpeed = -fullSpeed;
						rightSpeed = -turnSpeed;
						leftForward = false;
						rightForward = false;
					} else if (right == -1) {
						leftSpeed = -turnSpeed;
						rightSpeed = -fullSpeed;
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
						leftSpeed = staticTurnSpeed;
						rightSpeed = -staticTurnSpeed;
						leftForward = true;
						rightForward = false;
					} else if (right == -1) {
						leftSpeed = -staticTurnSpeed;
						rightSpeed = staticTurnSpeed;
						leftForward = false;
						rightForward = true;
					} else {
						leftSpeed = 0;
						rightSpeed = 0;
					}
				}
				Motor.A.setSpeed(leftSpeed);
				Motor.B.setSpeed(rightSpeed);
				if (leftForward) Motor.A.backward(); else Motor.A.forward();
				if (rightForward) Motor.B.backward(); else Motor.B.forward();
			}
		}
	}
	
	static boolean grabbing = false;
	public static void doGrabber() {
		Motor.C.setSpeed(360);
		Motor.C.rotateTo(grabbing ? 0 : 360, true);
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
