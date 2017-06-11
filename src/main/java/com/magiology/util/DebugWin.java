package com.magiology.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import org.lwjgl.util.vector.Quaternion;

import com.magiology.util.statics.LogUtil;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class DebugWin extends JFrame{
	
	private static DebugWin instance;
	
	public static DebugWin get(){
		if(instance==null) instance=new DebugWin();
		return instance;
	}
	
	private JPanel		listContain;
	private JScrollPane	scrollPane;
	
	public DebugWin(){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			e.printStackTrace();
		}
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel top=new JPanel();
		top.setBackground(SystemColor.window);
		getContentPane().add(top, BorderLayout.NORTH);
		
		scrollPane=new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel=new JPanel();
		scrollPane.setViewportView(panel);
		GridBagLayout gbl_panel=new GridBagLayout();
		gbl_panel.columnWidths=new int[]{40};
		gbl_panel.columnWeights=new double[]{1.0};
		gbl_panel.rowWeights=new double[]{1.0};
		panel.setLayout(gbl_panel);
		
		listContain=new JPanel();
		GridBagConstraints gbc_listContain=new GridBagConstraints();
		gbc_listContain.fill=GridBagConstraints.HORIZONTAL;
		gbc_listContain.anchor=GridBagConstraints.NORTH;
		gbc_listContain.gridx=0;
		gbc_listContain.gridy=0;
		panel.add(listContain, gbc_listContain);
		listContain.setBackground(SystemColor.window);
		listContain.setLayout(new BoxLayout(listContain, BoxLayout.Y_AXIS));
		setFocusableWindowState(false);
		setSize(new Dimension(400, 600));
		scrollPane.getViewport().addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e){
				listContain.setSize(0,0);
				SwingUtilities.updateComponentTreeUI(listContain);
			}
		});
	}
	
	public static void add(String txt, Color...cs){
		get().add0(txt, cs);
	}
	
	private void add0(String txt, Color[] cs){
		while(listContain.getComponentCount()>50)
			listContain.remove(0);
		if(!isVisible()) setVisible(true);
		
		JTextArea a=new JTextArea();
		a.setLineWrap(true);
		a.setWrapStyleWord(true);
		if(cs.length>0) a.setForeground(cs[0]);
		a.setBackground(cs.length>1?cs[1]:SystemColor.window);
		a.setBorder(new MatteBorder(0, 0, 1, 0, UIManager.getColor("Button.shadow")));
		a.setText(txt);
		a.setEditable(false);
		a.setFocusable(false);
		listContain.add(a);
		
		SwingUtilities.invokeLater(()->{
			setFocusableWindowState(true);
			JScrollBar scb=scrollPane.getVerticalScrollBar();
			boolean b=scb.getValue()==scb.getMaximum()-scb.getModel().getExtent();
			SwingUtilities.updateComponentTreeUI(this);
			if(b)scb.setValue(scb.getMaximum());
		});
	}
	
}
