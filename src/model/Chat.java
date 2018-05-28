package model;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public abstract class Chat implements Runnable {
	protected static final int MESSAGE_QUEUE_LENGTH = 20;
	private static int count = 0;
	protected String chatroomID;
	protected Server server;
	protected List<ServerThread> members;
	protected BlockingQueue<Message> msgQueue;
	protected List<File> files;
	
	public Chat(Server server) {
		this.server = server;
		chatroomID = "room_" + (++count);
		members = Collections.synchronizedList(new LinkedList<ServerThread>());
		msgQueue = new ArrayBlockingQueue<Message>(MESSAGE_QUEUE_LENGTH);
		files = Collections.synchronizedList(new LinkedList<File>());
	}
	
	/* broadcast messages to every member in the group*/
	public void run() {
		try{
			while(!members.isEmpty()) {
				Message msg = msgQueue.take();
				if(msg instanceof ChatMessage) {
					System.out.println(((ChatMessage)msg).getContent());
				}
				System.out.println(members.size());
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
			Message transmitMsg = null;
			if(msg instanceof ChatMessage) {
				transmitMsg = new ChatMessage(chatroomID, null, ((ChatMessage)msg).getContent());
			} else if(msg instanceof UpdateFileMessage) {
				transmitMsg = new UpdateFileMessage(chatroomID, null, ((UpdateFileMessage)msg).getFileName());
			} else if(msg instanceof UpdateMemberMessage) {
				transmitMsg = msg;
			}
			
			msgQueue.put(transmitMsg);
		} catch(InterruptedException e) {
			
		}
	}
	
	public void removeMember(ServerThread member) {
		members.remove(member);
		pushMsg(new UpdateMemberMessage(chatroomID, member.getID(), "User " + member.getID() + " leaves the chatroom!", UpdateMemberMessage.REMOVE_MEMBER));
		if(members.isEmpty()) {
			closeChat();
		}
	}
	
	public void closeChat() {
		assert members.isEmpty();
		server.removeChat(this);
		//server = null;
	}
	
	public void addFile(File file) {
		files.add(file);
		/* push the file update */
		pushMsg(new  UpdateFileMessage(null, chatroomID, file.getName()));
	}
	
	public File getFile(String fileName) {
		File file = null;
		synchronized(files) {
			for(File f : files) {
				if(f.getName().equals(fileName)) {
					file = f;
					break;
				}
			}
		}
		return file;
	}
	
	public String getChatroomID() {
		return chatroomID;
	}
	
	public synchronized List<String> getMemberIDs() {
		LinkedList<String> res = new LinkedList<String>();
		for(ServerThread member : members) {
			res.add(member.getID());
		}
		return res;
	}
	
	public synchronized List<String> getFileNames() {
		LinkedList<String> res = new LinkedList<String>();
		for(File file: files) {
			res.add(file.getName());
		}
		return res;
	}
}
