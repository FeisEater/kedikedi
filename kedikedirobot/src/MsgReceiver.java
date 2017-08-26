import java.io.IOException;

import lejos.nxt.Motor;

public class MsgReceiver extends Thread {
	public MsgReceiver() {
	}
	public void run() {
		for (;;) {
			try {
				String received = Main.dataIn.readUTF();
				switch (received) {
					case "grabber":
						Main.doGrabber();
						break;
					case "start_forward":
						Main.setMovement(1,0);
						break;
					case "start_backward":
						Main.setMovement(-1,0);
						break;
					case "start_left":
						Main.setMovement(0,-1);
						break;
					case "start_right":
						Main.setMovement(0,1);
						break;
					case "stop_forward":
						Main.setMovement(-1,0);
						break;
					case "stop_backward":
						Main.setMovement(1,0);
						break;
					case "stop_left":
						Main.setMovement(0,1);
						break;
					case "stop_right":
						Main.setMovement(0,-1);
						break;
					case "mode_spinny":
						Main.spinnyMode = true;
						Motor.A.resetTachoCount();
						Motor.B.resetTachoCount();
						Main.spinnyTravel = 0;
						Motor.A.stop(true);
						Motor.B.stop(true);
						break;
					case "mode_rc":
						System.out.println("disable");
						Main.spinnyMode = false;
						Motor.A.stop(true);
						Motor.B.stop(true);
						break;
					default:
						if (received.startsWith("speeds")) {
							String parse = received.substring(7);
							Main.fullSpeed = Integer.parseInt(parse.substring(0, parse.indexOf(" ")));
							parse = parse.substring(parse.indexOf(" ") + 1);
							Main.turnSpeed = Integer.parseInt(parse.substring(0, parse.indexOf(" ")));
							parse = parse.substring(parse.indexOf(" ") + 1);
							Main.staticTurnSpeed = Integer.parseInt(parse);
						}
						break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Oops");
				try {Main.dataIn.close();} catch (IOException ignore) {}
				try {Main.dataOut.close();} catch (IOException ignore) {}
				Main.connection.close();
				Main.running = false;
				break;
			}
		}
		try {Main.dataIn.close();} catch (IOException ignore) {}
		try {Main.dataOut.close();} catch (IOException ignore) {}
		Main.connection.close();
		Main.running = false;
	}
}
