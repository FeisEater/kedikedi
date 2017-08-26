package infrared_test;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
//import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
//import lejos.nxt.LCD;

public class infrared {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LightSensor ls1 = new LightSensor(SensorPort.S3);
		LightSensor ls2 = new LightSensor(SensorPort.S4);
		
		while (true) {
			int ls1_val = ls1.getLightValue();
			int ls2_val = ls2.getLightValue();
			LCD.drawInt(ls1_val, 4, 0, 0);
			LCD.drawInt(ls2_val, 4, 0, 1);
			if (ls1_val < 50) {
				//turn right
			}
			if (ls2_val<50) {
				//turn left
			}	
		}
		
	}

}
