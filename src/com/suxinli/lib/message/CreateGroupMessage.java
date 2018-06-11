package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class CreateGroupMessage extends Message {
	public static final long serialVersionUID = 10l;
	public CreateGroupMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	} 
	
}
