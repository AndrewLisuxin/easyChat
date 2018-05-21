package view;

import java.awt.*;
import javax.swing.*;

public class ClientMainFrame extends JFrame {
	private MainTabbedPane mainPane;
	
	public ClientMainFrame() {
		setTitle("EasyChat");
		mainPane = new MainTabbedPane();
		
	}
	
	public class MainTabbedPane extends JTabbedPane {
		private JList<String> clientList;
		private JList<String> groupList;
		public MainTabbedPane() {
			clientList = new JList<String>();
			clientList.setFixedCellWidth(300);
			addTab("Users", clientList);
			
			groupList = new JList<String>();
			groupList.setFixedCellWidth(300);
			addTab("Groups", groupList);
		}
	}
	
}
