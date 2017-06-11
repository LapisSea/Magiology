package com.magiology.client;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class MagiLog extends JFrame{
	
	static{
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	private static final MagiLog LOG=new MagiLog();
	
	private JTextField findField;
	private JButton    findButton;
	private JTextArea  textArea;
	private int pos=0;
	
	public MagiLog(){
		
		setLayout(new BorderLayout());
		findButton=new JButton("Next");
		findField=new JTextField("Java", 10);
		textArea=new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setAutoscrolls(true);
		
		Reader reader=null;
		try{
			reader=new FileReader(new File("Java.txt"));
			textArea.read(reader, null);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				reader.close();
			}catch(Exception e){
			}
		}
		
		JPanel header=new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.anchor=GridBagConstraints.WEST;
		header.add(findField, gbc);
		gbc.gridx++;
		header.add(findButton, gbc);
		
		add(header, BorderLayout.NORTH);
		add(new JScrollPane(textArea));
		
		findButton.addActionListener(e->{
			
			// Get the text to find...convert it to lower case for eaiser comparision
			String find=findField.getText().toLowerCase();
			// Focus the text area, otherwise the highlighting won't show up
			textArea.requestFocusInWindow();
			// Make sure we have a valid search term
			if(find!=null&&find.length()>0){
				Document document=textArea.getDocument();
				int findLength=find.length();
				try{
					boolean found=false;
					// Rest the search position if we're at the end of the document
					if(pos+findLength>document.getLength()){
						pos=0;
					}
					// While we haven't reached the end...
					// "<=" Correction
					while(pos+findLength<=document.getLength()){
						// Extract the text from teh docuemnt
						String match=document.getText(pos, findLength).toLowerCase();
						// Check to see if it matches or request
						if(match.equals(find)){
							found=true;
							break;
						}
						pos++;
					}
					
					// Did we find something...
					if(found){
						// Get the rectangle of the where the text would be visible...
						Rectangle viewRect=textArea.modelToView(pos);
						// Scroll to make the rectangle visible
						textArea.scrollRectToVisible(viewRect);
						// Highlight the text
						textArea.setCaretPosition(pos+findLength);
						textArea.moveCaretPosition(pos);
						// Move the search position beyond the current match
						pos+=findLength;
					}
					
				}catch(Exception exp){
					exp.printStackTrace();
				}
				
			}
		});
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		setSize(new Dimension(600, 700));
	}
	
	public static void append(String str){
		LOG.append0(str);
	}
	
	private void append0(String str){
		
		textArea.append(str);
		try{
			textArea.scrollRectToVisible(textArea.modelToView(textArea.getText().length()));
		}catch(Exception e){
		}
		if(!isVisible()) setVisible(true);
	}
	
}
