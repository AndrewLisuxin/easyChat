package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class InvitationMessage extends Message {
	public static final long serialVersionUID = 2l;
	public InvitationMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	}
}

