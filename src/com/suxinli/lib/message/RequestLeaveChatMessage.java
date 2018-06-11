package com.suxinli.lib.message;

import java.io.*;
import java.util.*;
public class RequestLeaveChatMessage extends Message {
	public static final long serialVersionUID = 8l;
	public RequestLeaveChatMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	} 
}
