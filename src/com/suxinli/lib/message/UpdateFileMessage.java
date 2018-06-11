package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class UpdateFileMessage extends Message {
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
