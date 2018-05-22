/**
 * 
 */
package model;

import java.net.ServerSocket;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.net.*;
/**
 * @author suxinli
 *
 */
public class Server extends ServerSocket {
	private static final int SERVER_PORT = 8189;
	
	/* record all connected clients */
	private Map<String, ServerThread> clients;
	
	/* record all chats */
	private Map<String, Chat> chatrooms;
	
	
	
	public Server() throws Exception {
		super(SERVER_PORT);
		clients = new ConcurrentHashMap<String, ServerThread>();
		chatrooms = new ConcurrentHashMap<String, Chat>();
		System.out.println("server starts!");
		start();
	}
	
	/*
	 * start the server
	 * */
	public void start() throws Exception {
		while(true) {
			Socket s = accept();
			System.out.println("receive connection!");
			ServerThread client = new ServerThread(this, s);
			System.out.println("start add client!");
			addClient(client);
			new Thread(client).start();
		}
	}
	
	public void broadcastUpdate(Message msg) {
		synchronized(clients) {
			for(Map.Entry<String, ServerThread> client: clients.entrySet()) {
				client.getValue().sendMsg(msg);
			}
		}
	}
	
	public void addClient(ServerThread client) {
		Message msg = new UpdateMessage(client.getID(), null, UpdateMessage.ADD_CLIENT);
		broadcastUpdate(msg);
		//System.out.println("here");
		clients.put(client.getID(), client);
		List<String> groups = new LinkedList<String>();
		synchronized(chatrooms) {
			for(Map.Entry<String, Chat> chat : chatrooms.entrySet()) {
				if(chat.getValue() instanceof GroupChat) {
					groups.add(chat.getKey());
				}
			}
		}
		System.out.print("clients: ");
		System.out.println(clients);
		msg = new LoadMessage(null, client.getID(), new LinkedList<String>(clients.keySet()), groups);
		client.sendMsg(msg);
	}
	
	public void removeClient(ServerThread client) {
		clients.remove(client.getID());
		Message msg = new UpdateMessage(client.getID(), null, UpdateMessage.REMOVE_CLIENT);
		broadcastUpdate(msg);
	}
	

	
	public void addChat(Chat c) {
		chatrooms.put(c.getChatroomID(), c);
		if(c instanceof GroupChat) {
			Message msg = new UpdateMessage(c.getChatroomID(), null, UpdateMessage.ADD_GROUP);
			broadcastUpdate(msg);
		}
		
	}
	
	public void removeChat(Chat c) {
		chatrooms.remove(c.getChatroomID());
		if(c instanceof GroupChat) {
			Message msg = new UpdateMessage(c.getChatroomID(), null, UpdateMessage.REMOVE_GROUP);
			broadcastUpdate(msg);
		}
		
	}
	
	public Map<String, ServerThread> getClients() {
		return clients;
	}
	
	
	public  Map<String, Chat> getChatrooms() {
		return chatrooms;
	}
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new Server();

	}

	
}
