package model;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client extends Socket {
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 8189;
	//private ObjectInputStream reader;
	private ObjectOutputStream writer;
	
	public Client() throws Exception {
		
		super(SERVER_IP, SERVER_PORT);
		
		writer = new ObjectOutputStream(getOutputStream());
		
		/* create and start the message receive thread */
		new Thread(new MsgReceiver()).start();
		
		
		
		
	}
	
	/* the main thread receives input from System.in, and send it to server */
	public void sendMsg() throws IOException {
		
	}
	
	public class MsgReceiver implements Runnable {
		private ObjectInputStream reader;
		
		
		public void run() {
			try {
				reader = new ObjectInputStream(getInputStream());
				while(true) {
					Message msg = (Message)reader.readObject();
					handleMessage(msg);
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
	
	private void handleMessage(Message msg) {
		String sourceID = msg.getSourceID();
		String targetID = msg.getTargetID();
		if(msg instanceof ChatMessage) {
			
		} else if(msg instanceof InvitationMessage) {
			
		} else if(msg instanceof UpdateMessage) {
			
		} else if(msg instanceof LoadMessage) {
			
		} else if(msg instanceof RefuseMessage) {
			
		} else if(msg instanceof AllowLeaveChatMessage) {
			
		} else if(msg instanceof AllowExitMessage) {
			
		}
	}
	
	private void loadInfor(Message msg) {
		LoadMessage infor = (LoadMessage)msg;
		
	}
	public static void main(String[] args) throws Exception {
		new Client();
		
	} 
}
