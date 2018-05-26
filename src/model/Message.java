package model;

import java.io.*;
import java.util.*;

public abstract class Message implements Serializable {
	protected String sourceID;
	protected String targetID;
	
	public Message(String sourceID, String targetID) {
		this.sourceID = sourceID;
		this.targetID = targetID;
	}
	
	public String getSourceID() {
		return sourceID;
	}
	
	public String getTargetID() {
		return targetID;
	}
}

class ChatMessage extends Message {
	public static final long serialVersionUID = 1l;
	private String content;
	public ChatMessage(String sourceID, String targetID, String content) {
		super(sourceID, targetID);
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	
}

class InvitationMessage extends Message {
	public static final long serialVersionUID = 2l;
	public InvitationMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	}
}

class ReplyMessage extends Message {
	public static final long serialVersionUID = 3l;
	private boolean accepted;
	public ReplyMessage(String sourceID, String targetID, boolean accepted) {
		super(sourceID, targetID);
		this.accepted = accepted;
	}
	public boolean isAccepted() {
		return accepted;
	}
}

class JoinGroupMessage extends Message {
	public static final long serialVersionUID = 4l;
	public JoinGroupMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	}
}

class UpdateMessage extends Message {
	public static final long serialVersionUID = 5l;
	public static final int ADD_CLIENT = 1;
	public static final int REMOVE_CLIENT = 2;
	public static final int ADD_GROUP = 3;
	public static final int REMOVE_GROUP = 4;
	private int op;
	public UpdateMessage(String sourceID, String targetID, int op) {
		super(sourceID, targetID);
		this.op = op;
	}
	public int getOp() {
		return op;
	}
}

class LoadMessage extends Message {
	public static final long serialVersionUID = 6l;
	private List<String> clients;
	private List<String> groups;
	public LoadMessage(String sourceID, String targetID, List<String> clients, List<String> groups) {
		super(sourceID, targetID);
		this.clients = clients;
		this.groups = groups;
	}
	public List<String> getClients() {
		return clients;
	}
	
	public List<String> getGroups() {
		return groups;
	}
}


class RefuseMessage extends Message {
	public static final long serialVersionUID = 7l;
	public RefuseMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	}
}

class RequestLeaveChatMessage extends Message {
	public static final long serialVersionUID = 8l;
	public RequestLeaveChatMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	} 
}

class AllowLeaveChatMessage extends Message {
	public static final long serialVersionUID = 11l;
	public AllowLeaveChatMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	} 
}
class RequestExitMessage extends Message {
	public static final long serialVersionUID = 9l;
	public RequestExitMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	} 
}
class AllowExitMessage extends Message {
	public static final long serialVersionUID = 12l;
	public AllowExitMessage() {
		super(null, null);
	} 
}

class CreateGroupMessage extends Message {
	public static final long serialVersionUID = 10l;
	public CreateGroupMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	} 
	
}

class FileMessage extends Message {
	public static final long serialVersionUID = 13l;
	private File file;
	public FileMessage(String sourceID, String targetID, File file) {
		super(sourceID, targetID);
		this.file = file;
	} 
	public File getFile() {
		return file;
	}
}


class RequestFileMessage extends Message {
	public static final long serialVersionUID = 14l;
	private String fileName;
	public RequestFileMessage(String sourceID, String targetID, String fileName) {
		super(sourceID, targetID);
		this.fileName = fileName;
	} 
	
	public String getFileName() {
		return fileName;
	}
	
}

class UpdateFileMessage extends Message {
	public static final long serialVersionUID = 15l;
	private String fileName;
	public UpdateFileMessage(String sourceID, String targetID, String fileName) {
		super(sourceID, targetID);
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}
}
