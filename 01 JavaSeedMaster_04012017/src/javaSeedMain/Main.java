package javaSeedMain;

import java.lang.reflect.InvocationTargetException;

import javaSeed.driverSheet.*;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException {

		String EnvItr = "2";//args[0];
		TriggerJavaSeed.TriggerScenarios(EnvItr);
		
	}

}
