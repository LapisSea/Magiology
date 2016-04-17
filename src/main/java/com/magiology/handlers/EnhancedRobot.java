package com.magiology.handlers;

import java.awt.AWTException;
import java.awt.Robot;

public class EnhancedRobot extends Robot{
	public EnhancedRobot()throws AWTException{super();}
	
	public void clickKeyKeyboard(int keyCode){
		try{
			keyPress(keyCode);
			keyRelease(keyCode);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void clickKeyMouse(int keyCode){
		try{
			mousePress(keyCode);
			mouseRelease(keyCode);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
