import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
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
	public static int scanningTurnSpeed = 60;
	public static BTConnection connection;
	public static DataInputStream dataIn;
	public static DataOutputStream dataOut;
	public static boolean spinnyMode = false;
	public static boolean mazeMode = false;
	public static boolean running = true;
	public static long zigzagTimer = 0;
	static UltrasonicSensor ultra;
	static DifferentialPilot pilot;
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
		pilot = new DifferentialPilot(5.5f, 10.5f, Motor.A, Motor.B);
		Motor.A.setSpeed(0);
		Motor.B.setSpeed(0);
		ultra = new UltrasonicSensor(SensorPort.S1);
		TouchSensor touch = new TouchSensor(SensorPort.S2);
		TouchSensor touch2 = new TouchSensor(SensorPort.S3);
		LightSensor lightLeft = new LightSensor(SensorPort.S4);
		LightSensor lightRight = new LightSensor(SensorPort.S3);
		long zigzagTime = 0;
		boolean arcRight = true;
		float oldLeft = 0;
		float oldRight = 0;
		while (running) {
			if (spinnyMode) {
				if (ultra.getDistance() <= 27 || touch.isPressed() || touch2.isPressed()) {
					Motor.A.setSpeed(fullSpeed);
					Motor.B.setSpeed(turnSpeed);
					Motor.A.backward();
					Motor.B.backward();
					zigzagTime = System.currentTimeMillis() + 500;
					arcRight = false;
				} else {
					if (System.currentTimeMillis() > zigzagTime) {
						arcRight = !arcRight;
						zigzagTime = System.currentTimeMillis() + 500;
					}
					Motor.A.setSpeed(arcRight ? fullSpeed : turnSpeed);
					Motor.B.setSpeed(arcRight ? turnSpeed : fullSpeed);
					Motor.A.forward();
					Motor.B.forward();
				}
			} else if (!mazeMode) {
			} else {
				int ll = lightLeft.readValue();
				int rl = lightRight.readValue();
				int pos = ll-rl;
				System.out.println(pos);

				int tspd = -30;
				int rspd = 0;

				if (pos>12)
					rspd=-40;
				else if (pos>8)
					rspd = -20;
				if (pos<-12)
					rspd = 40;
				else if (pos<-8)
					rspd = 20;

				float right = (tspd+rspd)/2f;
				float left = tspd-right;
				
				if (Math.abs(oldLeft - left * 15f) > 1) {
					Motor.A.setSpeed(left * 15f);
					if (left > 0) Motor.A.forward(); else Motor.A.backward();
				}
				if (Math.abs(oldRight - right * 15f) > 1) {
					Motor.B.setSpeed(right * 15f);					
					if (right > 0) Motor.B.forward(); else Motor.B.backward();
				}
				oldLeft = left * 30f;
				oldRight = right * 30f;
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
		
		int leftSpeed = 0;
		int rightSpeed = 0;
		boolean leftForward = true;
		boolean rightForward = true;

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
	
	/*public static void doMaze() {
		System.out.println("doing maze");
		int angle = 93;
		pilot.setRotateSpeed(angle);
		pilot.setTravelSpeed(20);
		Motor.A.setSpeed(fullSpeed);
		Motor.B.setSpeed(fullSpeed);
		while (ultra.getRange() > 15) {
			Motor.A.forward();
			Motor.B.forward();
		}
		pilot.rotate(-angle);
		Motor.A.setSpeed(fullSpeed);
		Motor.B.setSpeed(fullSpeed);
		while (ultra.getRange() > 15) {
			Motor.A.forward();
			Motor.B.forward();
		}
		pilot.rotate(angle);
		Motor.A.setSpeed(fullSpeed);
		Motor.B.setSpeed(fullSpeed);
		while (ultra.getRange() > 15) {
			Motor.A.forward();
			Motor.B.forward();
		}
		pilot.rotate(angle);
		Motor.A.setSpeed(fullSpeed);
		Motor.B.setSpeed(fullSpeed);
		while (ultra.getRange() > 15) {
			Motor.A.forward();
			Motor.B.forward();
		}
		pilot.rotate(-angle);
		Motor.A.setSpeed(fullSpeed);
		Motor.B.setSpeed(fullSpeed);
		while (ultra.getRange() > 15) {
			Motor.A.forward();
			Motor.B.forward();
		}
		pilot.rotate(-angle);
		pilot.travel(40);
		pilot.rotate(angle);
		pilot.travel(150);
		mazeMode = false;
	}*/
	
}
