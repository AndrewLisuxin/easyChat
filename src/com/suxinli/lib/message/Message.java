package com.suxinli.lib.message;

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














