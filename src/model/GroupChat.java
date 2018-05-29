package model;

import java.util.*;
public class GroupChat extends Chat {
	public GroupChat(Server server, ServerThread creator) {
		super(server);
		members.add(creator);
		
		creator.sendMsg(new LoadChatMessage(chatroomID, null, "User " + creator.getID() + " creates the group!", getMemberIDs(), getFileNames()));
		//creator.getConversations().put(chatroomID, this);
		
	}
	
	
	public void addMember(ServerThread member) {
		/* use broadcast thread might cause race condition */
		String id = member.getID();
		String content = "User " + member.getID() + " enters the group!";
		pushMsg(new UpdateMemberMessage(chatroomID, id, content, UpdateMemberMessage.ADD_MEMBER));
		List<String> ids = getMemberIDs();
		ids.add(id);
		member.sendMsg(new LoadChatMessage(chatroomID, null, content, ids, getFileNames()));
		members.add(member);
		//member.getConversations().put(chatroomID, this);
		
	}
	
	
	
	
}
