package model;

import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.net.*;

public class ServerThread implements Runnable {
	private Server server;
	private Socket s;
	private String ID;
	private boolean close;
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
		
		conversations = new ConcurrentHashMap<String, Chat>();
		
		writer = new ObjectOutputStream(s.getOutputStream());
		
		reader = new ObjectInputStream(s.getInputStream());
		
		close = false;
	}
	/* receive and handle message from the client */
	
	public void run() {
		try {
			while(!close) {
				Message msg = (Message)reader.readObject();
				handleMessage(msg);
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
			leaveChat(sourceID, targetID);
		} else if(msg instanceof RequestExitMessage) {
			exit();
		} else if(msg instanceof CreateGroupMessage) {
			createGroup();
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
		pushMsg(new ChatMessage(null, chat.getChatroomID(), "now you can chat!"));
	}
	
	private void sendRefuseMessage(String sourceID, String targetID) {
		server.getClients().get(targetID).sendMsg(new RefuseMessage(sourceID, targetID));
	}
	
	private void joinGroup(String groupID) {
		GroupChat group = (GroupChat)(server.getChatrooms().get(groupID));
		group.addMember(this);
		conversations.put(group.getChatroomID(), group);
	}
	
	private void leaveChat(String clientID, String chatroomID) {
		Chat c = conversations.get(chatroomID);
		c.removeMember(this);
		System.out.println("" + clientID + "leaves chatroon " + chatroomID);
		pushMsg(new ChatMessage(clientID, chatroomID, "User " + ID + " leaves the chatroom!"));
		conversations.remove(chatroomID);
		//sendMsg(new AllowLeaveChatMessage(chatroomID, clientID));
	}
	
	private void exit() {
		synchronized(conversations) {
			for(Map.Entry<String, Chat> chat : conversations.entrySet()) {
				chat.getValue().removeMember(this);
				pushMsg(new ChatMessage(ID, chat.getValue().getChatroomID(), "User " + ID + " leaves the chatroom!"));
			}
		}
		conversations.clear();
		server.removeClient(this);
		server = null;
		close = true;
		//sendMsg(new AllowExitMessage());
		
	}
	
	private void createGroup() {
		GroupChat group = new GroupChat(server, this);
		server.addChat(group);
		conversations.put(group.getChatroomID(), group);
		pushMsg(new ChatMessage(ID, group.getChatroomID(), "User " + ID + " enters the group!"));
	}
	
	
	/* send message to the client*/
	public void sendMsg(Message msg) {
		try {
			writer.writeObject(msg);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/* push message to group */
	public void pushMsg(ChatMessage msg) {
		/* dispatch to the chatGroup */
		conversations.get(msg.getTargetID()).pushMsg(msg);
		
	}
	
	public String getID() {
		return ID;
	}
	
	public Map<String, Chat> getConversations() {
		return conversations;
	} 
	
}
