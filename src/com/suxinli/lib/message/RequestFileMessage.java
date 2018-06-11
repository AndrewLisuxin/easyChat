package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class RequestFileMessage extends Message {
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
