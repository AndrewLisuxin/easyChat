package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class ChatMessage extends Message {
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
