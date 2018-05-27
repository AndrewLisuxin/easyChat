package model;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.channels.FileChannel;
import java.util.*;

import view.*;

public class Client extends Socket {
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 8189;
	private String ID;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private ClientMainFrame mainFrame;
	private boolean open;
	private Map<String, File> saveMap;
	public Client(ClientMainFrame frame) throws Exception {
		super(SERVER_IP, SERVER_PORT);
		
		ID = "" + this.getLocalAddress() + "[" + this.getLocalPort() + "]";
		
		mainFrame = frame;
		mainFrame.setClient(this);
		
		writer = new ObjectOutputStream(getOutputStream());
		reader = new ObjectInputStream(getInputStream());
		
		saveMap = new HashMap<String, File>();
		
		open = true;
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
				
				while(open) {
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
			System.out.println("update!!!");
			updateInfor((UpdateMessage)msg);
		} else if(msg instanceof LoadMessage) {
			loadInfor((LoadMessage)msg);
		} else if(msg instanceof RefuseMessage) {
			printRefuseMessage((RefuseMessage)msg);
		} else if(msg instanceof AllowLeaveChatMessage) {
			
		} else if(msg instanceof AllowExitMessage) {
			open = false;
		} else if(msg instanceof FileMessage) {
			saveFile((FileMessage)msg);
		} else if(msg instanceof UpdateFileMessage) {
			printFileUpdate((UpdateFileMessage)msg);
		}
	}
	
	private void loadInfor(LoadMessage msg) {
		//System.out.println("receive msg!");
		mainFrame.load(msg.getClients(), msg.getGroups());
	}
	
	private void updateInfor(UpdateMessage msg) {
			
		if(msg.getOp() == UpdateMessage.ADD_CLIENT) {
			System.out.println("add a client!!!");
			mainFrame.addClient(msg.getSourceID());
		} else if(msg.getOp() == UpdateMessage.REMOVE_CLIENT) {
			System.out.println("remove a client!!!");
			mainFrame.removeClient(msg.getSourceID());
		} else if(msg.getOp() == UpdateMessage.ADD_GROUP) {
			mainFrame.addGroup(msg.getSourceID());
		} else if(msg.getOp() == UpdateMessage.REMOVE_GROUP) {
			mainFrame.removeGroup(msg.getSourceID());
		}
	}
	
	private void printChatMessage(ChatMessage msg) { 
		String str = msg.getContent();
		System.out.println("in client.java, content: " + str);
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
	
	public void sendFileMessage(File file, String targetID) {
		sendMsg(new FileMessage(ID, targetID, file));
	}
	
	public static void main(String[] args) throws Exception {
		new Client(new ClientMainFrame());
	} 
	
	
	public void printRefuseMessage(RefuseMessage msg) {
		mainFrame.printRefuseMessage(msg.getSourceID());
	}
	
	public void saveFile(FileMessage msg) {
		File file = msg.getFile();
		FileChannel src = null;
		FileChannel dest = null;
		try {
			 src = new FileInputStream(file).getChannel();
			 dest = new FileOutputStream(saveMap.get(file.getName())).getChannel();
			 dest.transferFrom(src, 0, src.size());
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				src.close();
				dest.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	public void printFileUpdate(UpdateFileMessage msg) {
		String chatroomID = msg.getSourceID();
		String fileName = msg.getFileName();
		mainFrame.printFileUpdate(chatroomID, fileName);
	}
	
	public void requestFile(String fileName, String chatroomID) {
		sendMsg(new RequestFileMessage(ID, chatroomID, fileName));
	}
	public String getID() {
		return ID;
	}
	
	public void addSavePath(String fileName, File file) {
		saveMap.put(fileName, file);
	}
	
	
}
