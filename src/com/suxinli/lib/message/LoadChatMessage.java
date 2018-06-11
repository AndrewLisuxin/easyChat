package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class LoadChatMessage extends ChatMessage {
	public static final long serialVersionUID = 18l;
	private List<String> fileNames;
	private List<String> members;
	public LoadChatMessage(String sourceID, String targetID, String content, List<String> members, List<String> fileNames) {
		super(sourceID, targetID, content);
		this.fileNames = fileNames;
		this.members = members;
	}
	public List<String> getFileNames() {
		return fileNames;
	}
	public List<String> getMembers() {
		return members;
	}
}
