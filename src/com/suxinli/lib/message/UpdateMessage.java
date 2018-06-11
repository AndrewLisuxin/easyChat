package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class UpdateMessage extends Message {
	public static final long serialVersionUID = 5l;
	public static final int ADD_CLIENT = 1;
	public static final int REMOVE_CLIENT = 2;
	public static final int ADD_GROUP = 3;
	public static final int REMOVE_GROUP = 4;
	private int op;
	public UpdateMessage(String sourceID, String targetID, int op) {
		super(sourceID, targetID);
		this.op = op;
	}
	public int getOp() {
		return op;
	}
}
