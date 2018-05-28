package model;

public class GroupChat extends Chat {
	public GroupChat(Server server, ServerThread creator) {
		super(server);
		members.add(creator);
		
		creator.sendMsg(new LoadChatMessage(chatroomID, null, "User " + creator.getID() + " creates the group!", getMemberIDs(), getFileNames()));
		//creator.getConversations().put(chatroomID, this);
		
	}
	
	
	public void addMember(ServerThread member) {
		String content = "User " + member.getID() + " enters the group!";
		pushMsg(new UpdateMemberMessage(chatroomID, member.getID(), content, UpdateMemberMessage.ADD_MEMBER));
		members.add(member);
		member.sendMsg(new LoadChatMessage(chatroomID, null, content, getMemberIDs(), getFileNames()));
		//member.getConversations().put(chatroomID, this);
		
	}
	
	
	
	
}
