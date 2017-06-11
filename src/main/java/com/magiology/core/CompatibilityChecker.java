package com.magiology.core;

import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.UtilM;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.magiology.core.MReference.NAME;

public class CompatibilityChecker{
	
	static void checkJava8(){
		try{
			if(!Loader.instance().java8){
				JFrame error=new JFrame("Sorry, "+NAME+" requires Java 8!");
				error.setLayout(new BorderLayout());
				error.setBackground(Color.WHITE);
				error.setSize(200, 60);
				error.addWindowListener(new WindowAdapter(){
					
					@Override
					public void windowClosing(WindowEvent e){
						UtilM.exit(1);
					}
				});
				
				JPanel panel=new JPanel(new GridLayout(2, 1));
				JLabel line1=new JLabel("/--Java 8 or above--\\", JLabel.CENTER), line2=new JLabel("\\---is required!!---/", JLabel.CENTER);
				line1.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
				line2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
				panel.add(line1);
				panel.add(line2);
				error.add(panel);
				
				error.setSize(200+error.getWidth()-panel.getWidth(), 60+error.getHeight()-panel.getHeight());
				error.setResizable(false);
				error.setLocationRelativeTo(Display.getParent());
				error.setVisible(true);
				while(true){
					error.setVisible(true);
					UtilM.sleep(1000);
				}
			}
		}catch(Exception e){
			throw new IllegalStateException("Sorry, "+NAME+" requires Java 8!");
		}
		LogUtil.printWrapped("Java 8 or above is running! "+NAME+" can continue loading!");
	}
}
