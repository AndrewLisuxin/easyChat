package com.suxinli.lib.message;

import java.io.*;
import java.util.*;
public class LoadMessage extends Message {
	public static final long serialVersionUID = 6l;
	private List<String> clients;
	private List<String> groups;
	public LoadMessage(String sourceID, String targetID, List<String> clients, List<String> groups) {
		super(sourceID, targetID);
		this.clients = clients;
		this.groups = groups;
	}
	public List<String> getClients() {
		return clients;
	}
	
	public List<String> getGroups() {
		return groups;
	}
}

