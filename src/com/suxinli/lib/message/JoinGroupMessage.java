package com.suxinli.lib.message;

import java.io.*;
import java.util.*;

public class JoinGroupMessage extends Message {
	public static final long serialVersionUID = 4l;
	public JoinGroupMessage(String sourceID, String targetID) {
		super(sourceID, targetID);
	}
}
