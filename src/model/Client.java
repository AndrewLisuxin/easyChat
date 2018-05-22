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
			
		} else if(msg instanceof InvitationMessage) {
			
		} else if(msg instanceof UpdateMessage) {
			updateInfor(msg);
		} else if(msg instanceof LoadMessage) {
			loadInfor(msg);
		} else if(msg instanceof RefuseMessage) {
			
		} else if(msg instanceof AllowLeaveChatMessage) {
			
		} else if(msg instanceof AllowExitMessage) {
			
		}
	}
	
	private void loadInfor(Message msg) {
		//System.out.println("receive msg!");
		LoadMessage infor = (LoadMessage)msg;
		mainFrame.load(infor.getClients(), infor.getGroups());
	}
	
	private void updateInfor(Message msg) {
		UpdateMessage update = (UpdateMessage)msg;
		if(update.getOp() == UpdateMessage.ADD_CLIENT) {
			mainFrame.addClient(update.getSourceID());
		} else if(update.getOp() == UpdateMessage.REMOVE_CLIENT) {
			
		} else if(update.getOp() == UpdateMessage.ADD_GROUP) {
			
		} else if(update.getOp() == UpdateMessage.REMOVE_GROUP) {
			
		}
	}
	
	private void printChatMessage(ChatMessage msg) { 
		String str = msg.getSourceID() + " : " + msg.getContent() + "\n";
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
	public static void main(String[] args) throws Exception {
		new Client(new ClientMainFrame());
	} 
	
}
