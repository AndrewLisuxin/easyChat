package model;

public class GroupChat extends Chat {
	public GroupChat(Server server, ServerThread creator) {
		super(server);
		members.add(creator);
		//creator.getConversations().put(chatroomID, this);
		
	}
	
	
	public void addMember(ServerThread member) {
		members.add(member);
		//member.getConversations().put(chatroomID, this);
		
	}
	
	@Override
	public void removeMember(ServerThread member) {
		members.remove(member);
		if(members.isEmpty()) {
			closeChat();
			
		}
	}
	
}
