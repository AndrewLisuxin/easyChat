package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class FileMessage extends Message {
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
