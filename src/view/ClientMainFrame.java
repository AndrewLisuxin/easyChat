package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import model.*;

public class ClientMainFrame extends JFrame {
	private Client client;
	private java.util.Map<String, ClientChatFrame> chatFrames;
	
	
	private JTabbedPane mainPane;
	private JList<String> clientList;
	private JList<String> groupList;
	private DefaultListModel<String> clients;
	private DefaultListModel<String> groups;
	
	public ClientMainFrame() {
		/* dfs */
		setTitle("EasyChat");
		
		mainPane = new JTabbedPane();
		
		JScrollPane clientScrollPane = new JScrollPane();
		
		clientList = new JList<String>();
		clients = new DefaultListModel<String>();
		clientList.setModel(clients);
		clientList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					int idx = clientList.locationToIndex(e.getPoint());
					String targetID = clients.get(idx);
					
				}
			}
		});
		
		clientScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 400));
		clientScrollPane.setViewportView(clientList);
		
		mainPane.addTab("Users", clientScrollPane);
		
		JScrollPane groupScrollPane = new JScrollPane();
		
		groupList = new JList<String>();
		groups = new DefaultListModel<String>();
		groupList.setModel(groups);
		
		groupScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 400));
		groupScrollPane.setViewportView(groupList);
		
		mainPane.addTab("Groups", groupScrollPane);
		
		add(mainPane);
		
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
			
	}
	
	public void addClient(String client) {
		clients.addElement(client);
	}
	
	public void removeClient(String client) {
		String[] items = (String[])clients.toArray();
		int idx = 0;
		while(!items[idx].equals(client)) { ++idx; }
		clients.remove(idx);
	}
	
	public void addGroup(String group) {
		clients.addElement(group);
	}
	
	public void removeGroup(String group) {
		String[] items = (String[])groups.toArray();
		int idx = 0;
		while(!items[idx].equals(group)) { ++idx; }
		groups.remove(idx);
	}
	
	public void load(java.util.List<String> clients, java.util.List<String> groups) {
		for(String client : clients) {
			addClient(client);
		}
		for(String group : groups) {
			addClient(group);
		}
	}
	
	public void sendChatMessage(String str, String targetID) {
		client.sendChatMessage(str, targetID);
	}
	
	public void printChatMessage(String str, String sourceID) {
		chatFrames.get(sourceID).printChatMessage(str);
	}
	
	public void printReceivedInvitationMessage(String sourceID) {
		String str = "User " + sourceID + " sends you a chat invitation!";
		
	}
}


