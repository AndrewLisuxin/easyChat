package model;

public class IndividualChat extends Chat {
	public IndividualChat(Server server, ServerThread a, ServerThread b) {
		super(server);
		//server.getChatrooms().put(chatroomID, this);
		members.add(a);
		members.add(b);
		
		a.sendMsg(new LoadChatMessage(chatroomID, null, "now you can chat!", getMemberIDs(), getFileNames()));
		b.sendMsg(new LoadChatMessage(chatroomID, null, "now you can chat!", getMemberIDs(), getFileNames()));
		//a.getConversations().put(chatroomID, this);
		//b.getConversations().put(chatroomID, this);
		
	}
	
	
	
}
