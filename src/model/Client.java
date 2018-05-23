package model;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

import view.*;

public class Client extends Socket {
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 8189;
	private String ID;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private ClientMainFrame mainFrame;
	public Client(ClientMainFrame frame) throws Exception {
		super(SERVER_IP, SERVER_PORT);
		
		ID = "" + this.getLocalAddress() + "[" + this.getLocalPort() + "]";
		
		mainFrame = frame;
		mainFrame.setClient(this);
		
		writer = new ObjectOutputStream(getOutputStream());
		reader = new ObjectInputStream(getInputStream());
		/* create and start the message receive thread */
		new Thread(new MsgReceiver()).start();
		
	}
	
	/* the main thread receives input from System.in, and send it to server */
	public void sendMsg(Message msg) {
		try {
			writer.writeObject(msg);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public class MsgReceiver implements Runnable {
		
		
		
		public void run() {
			try {
				
				while(true) {
					Message msg = (Message)reader.readObject();
					System.out.println("receive msg!");
					handleReceivedMessage(msg);
				}
			} catch(Exception e) {
				System.err.println(e);
			} finally {
				
				try {
					Client.this.close();
				} catch(IOException e) {
					System.err.println(e);
				} 
				
			}
			
		}
	} 
	
	private void handleReceivedMessage(Message msg) {
		String sourceID = msg.getSourceID();
		String targetID = msg.getTargetID();
		if(msg instanceof ChatMessage) {
			printChatMessage((ChatMessage)msg);
		} else if(msg instanceof InvitationMessage) {
			printReceivedInvitationMessage((InvitationMessage)msg);
		} else if(msg instanceof UpdateMessage) {
			updateInfor((UpdateMessage)msg);
		} else if(msg instanceof LoadMessage) {
			loadInfor((LoadMessage)msg);
		} else if(msg instanceof RefuseMessage) {
			
		} else if(msg instanceof AllowLeaveChatMessage) {
			
		} else if(msg instanceof AllowExitMessage) {
			
		}
	}
	
	private void loadInfor(LoadMessage msg) {
		//System.out.println("receive msg!");
		mainFrame.load(msg.getClients(), msg.getGroups());
	}
	
	private void updateInfor(UpdateMessage msg) {
			
		if(msg.getOp() == UpdateMessage.ADD_CLIENT) {
			mainFrame.addClient(msg.getSourceID());
		} else if(msg.getOp() == UpdateMessage.REMOVE_CLIENT) {
			mainFrame.removeClient(msg.sourceID);
		} else if(msg.getOp() == UpdateMessage.ADD_GROUP) {
			mainFrame.addGroup(msg.getSourceID());
		} else if(msg.getOp() == UpdateMessage.REMOVE_GROUP) {
			
		}
	}
	
	private void printChatMessage(ChatMessage msg) { 
		String str = msg.getContent();
		mainFrame.printChatMessage(str, msg.getSourceID());
	}
	public void sendChatMessage(String str, String targetID) {
		sendMsg(new ChatMessage(ID, targetID, str));
	}
	
	public void sendInvitationMessage(String targetID) {
		sendMsg(new InvitationMessage(ID, targetID));
	}
	
	public void printReceivedInvitationMessage(InvitationMessage msg) {
		mainFrame.printReceivedInvitationMessage(msg.getSourceID());
	}
	
	public void sendReplyMessage(String targetID, boolean accepted) {
		sendMsg(new ReplyMessage(ID, targetID, accepted));
	}
	
	public void sendJoinGroupMessage(String groupID) {
		sendMsg(new JoinGroupMessage(ID, groupID));
	}
	
	public void sendRequestLeaveChatMessage(String targetID) {
		sendMsg(new RequestLeaveChatMessage(ID, targetID));
	}
	
	public void sendRequestExitMessage() {
		sendMsg(new RequestExitMessage(ID, null));
	}
	
	public void sendCreateGroupMessage() {
		sendMsg(new CreateGroupMessage(ID, null));
	}
	public static void main(String[] args) throws Exception {
		new Client(new ClientMainFrame());
	} 
	
	public String getID() {
		return ID;
	}
}
