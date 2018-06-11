package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class UpdateMemberMessage extends ChatMessage {
	
	public static final long serialVersionUID = 17l;
	public static final int ADD_MEMBER = 1;
	public static final int REMOVE_MEMBER = 2;
	private int op;

	public UpdateMemberMessage(String sourceID, String targetID, String content, int op) {
		super(sourceID, targetID, content);
		this.op = op;
	}
	
	public int getOp() {
		return op;
	}
}
