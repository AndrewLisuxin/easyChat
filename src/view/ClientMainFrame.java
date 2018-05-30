package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import model.*;

import java.io.*;

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
		
		chatFrames = new ConcurrentHashMap<String, ClientChatFrame>();
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
					
					if(!targetID.equals(client.getID())) {
						client.sendInvitationMessage(targetID);
					}
					
				}
			}
		});
		
		clientScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 400));
		clientScrollPane.setViewportView(clientList);
		
		mainPane.addTab("Users", clientScrollPane);
		
		JPanel groupPanel = new JPanel();

		JButton createGroupButton = new JButton("Create");
		createGroupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.sendCreateGroupMessage();
			}
		});
	
		groupPanel.add(createGroupButton);
		
		JScrollPane groupScrollPane = new JScrollPane();
		
		groupList = new JList<String>();
		groups = new DefaultListModel<String>();
		groupList.setModel(groups);
		groupList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					int idx = groupList.locationToIndex(e.getPoint());
					String groupID = groups.get(idx);
					if(chatFrames.containsKey(groupID)) {
						chatFrames.get(groupID).toFront();
					}
					else {
						client.sendJoinGroupMessage(groupID);
					}
					
				}
			}
		});
		
		groupScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 400));
		groupScrollPane.setViewportView(groupList);
		
		groupPanel.add(groupScrollPane);
		
		mainPane.addTab("Groups", groupPanel);
		
		add(mainPane);
		
		pack();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println(client.getID() + "logs off");
				for(Map.Entry<String, ClientChatFrame> chatFrame : chatFrames.entrySet()) {
					chatFrame.getValue().dispose();
				}
				client.sendRequestExitMessage();
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
			
	}
	
	public void setClient(Client client) { this.client = client; }
	
	public void addClient(String client) {
		clients.addElement(client);
	}
	
	public void removeClient(String client) {
		int i;
		for(i = 0; !client.equals(clients.get(i)); ++i);
		clients.remove(i);
	}
	
	public void addGroup(String group) {
		groups.addElement(group);
	}
	
	public void removeGroup(String group) {
		int i;
		for(i = 0; !group.equals(groups.get(i)); ++i);
		groups.remove(i);
	}
	
	public void load(java.util.List<String> clients, java.util.List<String> groups) {
		for(String client : clients) {
			addClient(client);
		}
		for(String group : groups) {
			addGroup(group);
		}
	}
	
	public void sendChatMessage(String str, String targetID) {
		client.sendChatMessage(str, targetID);
	}
	
	public void printChatMessage(String str, String chatroomID) {
		System.out.println("chat content: " + str);
		System.out.println("chatroom: " + chatroomID);
		
		chatFrames.get(chatroomID).printChatMessage(str);
	}
	
	public void addChatFrame(String chatroomID, java.util.List<String> members, java.util.List<String> fileNames) {
		ClientChatFrame chatFrame = new ClientChatFrame(this, chatroomID);
		chatFrames.put(chatroomID, chatFrame);
		chatFrame.loadMembers(members);
		chatFrame.loadFiles(fileNames);
		
	}
	
	public void removeChatFrame(String targetID) {
		chatFrames.remove(targetID);
	}
	public void printReceivedInvitationMessage(String sourceID) {
		String str = "User " + sourceID + " sends you a chat invitation. Accept it?";
		System.out.println(str);
		int option = JOptionPane.showConfirmDialog(null, str, "Invitation", JOptionPane.YES_NO_OPTION);
		if(option == JOptionPane.YES_OPTION) {
			client.sendReplyMessage(sourceID, true);
		}
		else {
			client.sendReplyMessage(sourceID, false);
		}
		
	}
	
	public void printRefuseMessage(String sourceID) {
		JOptionPane.showMessageDialog(null, "" + sourceID + " refuses your chat invitation");
	}
	
	public void printSaveFileFailure(String fileName) {
		JOptionPane.showMessageDialog(null, "save file '" + fileName + "' failed!");
	}
	public void sendRequestLeaveChatMessage(String targetID) {
		client.sendRequestLeaveChatMessage(targetID);
	}
	
	public void sendFile(File file, String targetID) {
		client.sendFileMessage(file, targetID);
	}
	
	public void requestFile(String fileName, String chatroomID) {
		client.requestFile(fileName, chatroomID);
	}
	
	public Client getClient() {
		return client;
	}
	
	public void addSavePath(String fileName, File file) {
		client.addSavePath(fileName, file);
	}
	
	public void printFileUpdate(String chatroomID, String fileName) {
		System.out.println("" + chatroomID + " " + fileName);
		chatFrames.get(chatroomID).updateFile(fileName);
	}
	
	public void addMember(String chatroomID, String member) {
		System.out.println("" + chatroomID + " " + member);
		chatFrames.get(chatroomID).addMember(member);
	}
	
	public void removeMember(String chatroomID, String member) {
		chatFrames.get(chatroomID).removeMember(member);
	}
	
	
}


