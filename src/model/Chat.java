package model;

import java.util.*;
import java.util.concurrent.*;


public abstract class Chat implements Runnable {
	protected static final int MESSAGE_QUEUE_LENGTH = 20;
	private static int count = 0;
	protected String chatroomID;
	protected Server server;
	protected List<ServerThread> members;
	protected BlockingQueue<Message> msgQueue;
	
	public Chat(Server server) {
		this.server = server;
		chatroomID = "room_" + (++count);
		members = Collections.synchronizedList(new LinkedList<ServerThread>());
		msgQueue = new ArrayBlockingQueue<Message>(MESSAGE_QUEUE_LENGTH);
	}
	
	/* broadcast messages to every member in the group*/
	public void run() {
		try{
			while(!members.isEmpty()) {
				Message msg = msgQueue.take();
				
				synchronized (members) {
					for(ServerThread member: members) {
						member.sendMsg(msg);
					}
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pushMsg(Message msg) {
		try {
			msgQueue.put(msg);
		} catch(InterruptedException e) {
			
		}
	}
	
	public abstract void removeMember(ServerThread member);
	
	public void closeChat() {
		assert members.isEmpty();
		server.removeChat(this);
		server = null;
	}
	

	
	public String getChatroomID() {
		return chatroomID;
	}
}
