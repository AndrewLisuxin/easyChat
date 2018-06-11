package com.suxinli.lib.message;

import java.io.*;
import java.util.*;
public class RefuseMessage extends Message {
	public static final long serialVersionUID = 7l;
	public RefuseMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	}
}
