package model;

import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.net.*;

public class ServerThread implements Runnable {
	private Server server;
	private Socket s;
	private String ID;
	private boolean open;
	/**
	 * 
	 * */
	private Map<String, Chat> conversations;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	
	public ServerThread(Server server, Socket s) throws Exception {
		this.server = server;
		this.s = s;
		ID = "" + s.getInetAddress() + "[" + s.getPort() + "]";
		
		conversations = new HashMap<String, Chat>();
		
		writer = new ObjectOutputStream(s.getOutputStream());
		
		reader = new ObjectInputStream(s.getInputStream());
		
		open = true;
	}
	/* receive and handle message from the client */
	
	public void run() {
		try {
			try {
				while(open) {
					Message msg = (Message)reader.readObject();
					handleMessage(msg);
				}
			} finally {
				reader.close();
				writer.close();
				s.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
		
		
	}
	
	private void handleMessage(Message msg) {
		String sourceID = msg.getSourceID();
		String targetID = msg.getTargetID();
		if(msg instanceof ChatMessage) {
			sendChatMessage((ChatMessage)msg);
		} else if(msg instanceof InvitationMessage) {
			sendInvitationMessage(msg);
		} else if(msg instanceof ReplyMessage) {
			if(((ReplyMessage) msg).isAccepted()) {
				createIndividualChat(sourceID, targetID);
			} 
			else {
				sendRefuseMessage(sourceID, targetID);
			}
		} else if(msg instanceof JoinGroupMessage) {
			joinGroup(targetID);
			
		} else if(msg instanceof RequestLeaveChatMessage) {
			leaveChat(targetID);
		} else if(msg instanceof RequestExitMessage) {
			exit();
		} else if(msg instanceof CreateGroupMessage) {
			createGroup();
		} else if(msg instanceof FileMessage) {
			addFile((FileMessage) msg);
		} else if(msg instanceof RequestFileMessage) {
			getFile((RequestFileMessage)msg);
		}
	}
	
	
	
	
	
	private void sendChatMessage(ChatMessage msg) {
		pushMsg(msg);
	}
	
	private void sendInvitationMessage(Message msg) {
		System.out.println("" + msg.getSourceID() + " sends an invitation to " + msg.getTargetID());
		server.getClients().get(msg.getTargetID()).sendMsg(msg);
	}
	
	private void createIndividualChat(String sourceID, String targetID) {
		System.out.println("" + sourceID + " and " + targetID + " create an individual chat");
		ServerThread a = server.getClients().get(sourceID);
		ServerThread b = server.getClients().get(targetID);
		IndividualChat chat = new IndividualChat(server, a, b);
		server.addChat(chat);
		a.getConversations().put(chat.getChatroomID(), chat);
		b.getConversations().put(chat.getChatroomID(), chat);
		new Thread(chat).start();
		//pushMsg(new ChatMessage(null, chat.getChatroomID(), "now you can chat!"));
	}
	
	private void sendRefuseMessage(String sourceID, String targetID) {
		server.getClients().get(targetID).sendMsg(new RefuseMessage(sourceID, targetID));
	}
	

	
	private void joinGroup(String groupID) {
		GroupChat group = (GroupChat)(server.getChatrooms().get(groupID));
		group.addMember(this);
		conversations.put(groupID, group);
		//pushMsg(new ChatMessage(ID, groupID, "User " + ID + " enters the group!"));
	}
	
	private void leaveChat(String chatroomID) {
		Chat c = conversations.get(chatroomID);
		c.removeMember(this);
		
		//System.out.println("" + clientID + "leaves chatroon " + chatroomID);
		//pushMsg(new ChatMessage(ID, chatroomID, "User " + ID + " leaves the chatroom!"));
		conversations.remove(chatroomID);
		//sendMsg(new AllowLeaveChatMessage(chatroomID, clientID));
	}
	
	private void exit() {
		Set<String> chatIDs = new HashSet<String>(conversations.keySet());
		for(String chatroomID : chatIDs) {
			leaveChat(chatroomID);
		}
		server.removeClient(this);
		System.out.println("here.........");
		sendMsg(new AllowExitMessage());
		
		/* delay for a while to avoid the socket is closed before sending out the msg */
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {
			
		} finally {
			open = false;
		}
		
		
		
		
		//sendMsg(new AllowExitMessage());
		
	}
	
	private void createGroup() {
		GroupChat group = new GroupChat(server, this);
		server.addChat(group);
		conversations.put(group.getChatroomID(), group);
		new Thread(group).start();
		//pushMsg(new ChatMessage(ID, group.getChatroomID(), "User " + ID + " creates the group!"));
	}
	
	private void addFile(FileMessage msg) {
		String uploader = msg.getSourceID();
		String chatroomID = msg.getTargetID();
		File file = msg.getFile();
		conversations.get(chatroomID).addFile(file, uploader);
	}
	
	private void getFile(RequestFileMessage msg) {
		String targetID = msg.getTargetID();
		String fileName = msg.getFileName();
		sendMsg(new FileMessage(targetID, ID, conversations.get(targetID).getFile(fileName)));
	}
	/* send message to the client*/
	
	/* notice: both server and chat can call this func, and writeObject is NOT thread-safe*/
	public synchronized void sendMsg(Message msg) {
		try {
			writer.writeObject(msg);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/* push message to group */
	public void pushMsg(ChatMessage msg) {
		/* dispatch to the chatGroup */
		String chatroomID = msg.getTargetID();
		ChatMessage transmitMsg = new ChatMessage(chatroomID, null, msg.getContent());
		conversations.get(chatroomID).pushMsg(transmitMsg);
		
	}
	
	public String getID() {
		return ID;
	}
	
	public Map<String, Chat> getConversations() {
		return conversations;
	} 
	
}
