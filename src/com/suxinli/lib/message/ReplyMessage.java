package com.suxinli.lib.message;

import java.io.*;
import java.util.*;
public class ReplyMessage extends Message {
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
