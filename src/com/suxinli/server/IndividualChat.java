package com.suxinli.server;

import java.util.*;
import com.suxinli.lib.message.*;

public class IndividualChat extends Chat {
	public IndividualChat(Server server, ServerThread a, ServerThread b) {
		super(server);
		//server.getChatrooms().put(chatroomID, this);
		members.add(a);
		members.add(b);
		
		List<String> members = getMemberIDs(); 
		List<String> files = getFileNames();
		a.sendMsg(new LoadChatMessage(chatroomID, null, "now you can chat!", members, files));
		b.sendMsg(new LoadChatMessage(chatroomID, null, "now you can chat!", members, files));
		//a.getConversations().put(chatroomID, this);
		//b.getConversations().put(chatroomID, this);
		
	}
	
	
	
}
