package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class RequestExitMessage extends Message {
	public static final long serialVersionUID = 9l;
	public RequestExitMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	} 
}
