package nxt_test;
import lejos.nxt.*;
import lejos.robotics.objectdetection.*;

public class maze implements FeatureListener{
public static int MAX_DETECT = 80;
static int wall = 0;

	public static void main(String[] args) throws Exception {
		
		maze listener = new maze();
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);
		RangeFeatureDetector fd = new RangeFeatureDetector(us, MAX_DETECT, 500);
		fd.addListener(listener);
		Button.ENTER.waitForPressAndRelease();
	}
	
	public void featureDetected(Feature feature, FeatureDetector detector) {
		
		int range = (int)feature.getRangeReading().getRange();
		System.out.println("Range:" + range);
		wall += 1;
		switch(wall) {
		case 1:
			//rotateleft
			//move forward
			System.out.println("case1");
			break;
		case 2:
			//rotateright
			//moveforward
			System.out.println("case2");
			break;
		case 3:
			//rotateright
			//moveforward
			System.out.println("case3");
			break;
		case 4:
			//rotateleft
			//moveforward
			System.out.println("case4");
			break;
		case 5:
			System.out.println("case5");
			//rotateleft
			//moveforward a little
			//stop
			//rotateright
			//moveforward a lot
			//stop
			break;
			
		}
	}

}