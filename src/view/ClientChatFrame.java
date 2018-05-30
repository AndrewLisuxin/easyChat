package view;

import model.*;

import java.awt.*;

import javax.swing.*;

import java.io.*;

import java.awt.event.*;

public class ClientChatFrame extends JFrame {
	private ClientMainFrame mainFrame;
	private String targetID;
	private JTextArea messageArea;
	private JList<String> memberList;
	private DefaultListModel<String> memberIDs;
	private JList<String> fileList;
	private DefaultListModel<String> fileNames;
	//private JPanel box;
	//private JPanel outputPanel;
	private JPanel inputPanel;
	private JPanel buttonPanel;
	private JButton fileButton;
	private JFileChooser fc;
	private JTextField field;
	private JButton sendButton;
	public ClientChatFrame(ClientMainFrame mainFrame, String targetID) {
		super();
		setTitle("EasyChat -- " + targetID);
		this.mainFrame = mainFrame;
		this.targetID = targetID;
		
		

		JScrollPane messageScrollPane = new JScrollPane();
		
		messageArea = new JTextArea(20, 30);
		messageArea.setEditable(false);
		
		messageScrollPane.setViewportView(messageArea);
		//messageScrollPane.setPreferredSize(new Dimension(300,400));
	
		JTabbedPane additionalPane = new JTabbedPane();
		
		JScrollPane memberScrollPane = new JScrollPane();
		
		memberList = new JList<String>();
		memberIDs = new DefaultListModel<String>();
		memberList.setModel(memberIDs);
		
		memberScrollPane.setViewportView(memberList);
		
		additionalPane.addTab("Members", memberScrollPane);
		
		JScrollPane fileScrollPane = new JScrollPane();
		
		
		fileList = new JList<String> ();
		fileNames = new DefaultListModel<String>();
		fileList.setModel(fileNames);
		
		fileList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					int idx = fileList.locationToIndex(e.getPoint());
					String fileName = fileNames.get(idx);
					if(fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						ClientChatFrame.this.mainFrame.addSavePath(fileName, file);
					}
					ClientChatFrame.this.mainFrame.requestFile(fileName, ClientChatFrame.this.targetID);
				}
			}
		});
		
		fileScrollPane.setViewportView(fileList);
		
		additionalPane.addTab("Shared files", fileScrollPane);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				messageScrollPane, additionalPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setPreferredSize(new Dimension(600, 400));
		splitPane.setDividerLocation(350);
		
		add(splitPane, BorderLayout.CENTER);
		
		inputPanel = new JPanel(new BorderLayout());
		
		JToolBar toolbar = new JToolBar();
		
		fileButton = new JButton(new ImageIcon("images/file.png"));
		fc = new JFileChooser();
		fileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* select and send file */
				if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					ClientChatFrame.this.mainFrame.sendFile(file, ClientChatFrame.this.targetID);
				}
			}
		});
		toolbar.add(fileButton);
		
		inputPanel.add(toolbar, BorderLayout.NORTH);
		
		JPanel textInputPane = new JPanel();
		
		MessageAction messageAction = new MessageAction("", new ImageIcon("images/msg.png"));
		
		field = new JTextField(30);
		field.addActionListener(messageAction);
		
		textInputPane.add(field);
		
		sendButton = new JButton(messageAction);
		
		textInputPane.add(sendButton);
		
		inputPanel.add(textInputPane, BorderLayout.CENTER);
		
		add(inputPanel, BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("leave chat " + ClientChatFrame.this.targetID);
				ClientChatFrame.this.mainFrame.sendRequestLeaveChatMessage(ClientChatFrame.this.targetID);
				ClientChatFrame.this.mainFrame.removeChatFrame(ClientChatFrame.this.targetID);
			}
		});
		
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public class MessageAction extends AbstractAction {
		public MessageAction(String name, Icon icon) {
			putValue(Action.NAME, name);
			putValue(Action.SMALL_ICON, icon);
		}
		public void actionPerformed(ActionEvent e) {
			String str = mainFrame.getClient().getID() + " : " + field.getText();
			mainFrame.sendChatMessage(str, targetID);
			field.setText("");
		}
	}
	public void printChatMessage(String str) {
		//System.out.println(outputPanel.getPreferredSize());
		messageArea.append(str + "\n");
	}
	
	
	public void updateFile(String fileName) {
		fileNames.addElement(fileName);
	}
	
	public void loadFiles(java.util.List<String> fileNames) {
		for(String fileName : fileNames) {
			updateFile(fileName);
		}
	}
	public void addMember(String ID) {
		memberIDs.addElement(ID);
	}
	
	public void removeMember(String ID) {
		int idx = 0;
		while(!memberIDs.get(idx).equals(ID)) {
			++idx;
		}
		memberIDs.remove(idx);
		
	}
	public void loadMembers(java.util.List<String> memberIDs) {
		for(String ID : memberIDs) {
			addMember(ID);
		}
		repaint();
	}
	/*public class MesssageComponent extends JComponent {
		private String str;
		public	MesssageComponent(String str) {
			super();
			this.str = str;
		}
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			g2.drawString(str, 0, outputPanel.getComponentCount() * 30);
		}
		public Dimension getPreferredSize() {
			return new Dimension(350, 30);
		}
	}
	public class ImageComponent extends JComponent {
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			g2.drawImage(new ImageIcon("tm.png").getImage(), 0, 0, 30, 30, null);
		}
	}*/
}
