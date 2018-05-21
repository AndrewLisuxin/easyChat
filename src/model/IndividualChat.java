package model;

public class IndividualChat extends Chat {
	public IndividualChat(Server server, ServerThread a, ServerThread b) {
		super(server);
		//server.getChatrooms().put(chatroomID, this);
		members.add(a);
		members.add(b);
		//a.getConversations().put(chatroomID, this);
		//b.getConversations().put(chatroomID, this);
		
	}
	@Override
	public void removeMember(ServerThread member) {
		members.clear();
		closeChat();
	}
}
