package view;

import model.*;

import java.awt.BorderLayout;

import javax.swing.*;

import java.awt.event.*;

public class ClientChatFrame extends JFrame {
	private ClientMainFrame mainFrame;
	private String targetID;
	private JTextArea area;
	private JTextField field;
	public ClientChatFrame(ClientMainFrame mainFrame, String targetID) {
		super();
		setTitle("EasyChat in " + targetID);
		this.mainFrame = mainFrame;
		this.targetID = targetID;
		area = new JTextArea(30, 30);
		area.setEditable(false);
		add(area, BorderLayout.CENTER);
		
		field = new JTextField(30);
		field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = ClientChatFrame.this.mainFrame.getClient().getID() + " : " + field.getText();
				ClientChatFrame.this.mainFrame.sendChatMessage(str, ClientChatFrame.this.targetID);
				field.setText("");
			}
		});
		add(field, BorderLayout.SOUTH);
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("leave chat " + ClientChatFrame.this.targetID);
				ClientChatFrame.this.mainFrame.sendRequestLeaveChatMessage(ClientChatFrame.this.targetID);
				ClientChatFrame.this.mainFrame.removeChatFrame(ClientChatFrame.this.targetID);
			}
		});
		
		pack();
		setVisible(true);
	}
	
	public void printChatMessage(String str) {
		area.append(str + "\n");
	}
	
	
}
