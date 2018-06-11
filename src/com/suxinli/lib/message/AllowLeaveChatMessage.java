package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class AllowLeaveChatMessage extends Message {
	public static final long serialVersionUID = 11l;
	public AllowLeaveChatMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	} 
}
