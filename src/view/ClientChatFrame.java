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
	private JList<String> fileList;
	private DefaultListModel<String> fileNames;
	//private JPanel box;
	//private JPanel outputPanel;
	private JPanel inputPanel;
	private JPanel buttonPanel;
	private JButton fileButton;
	private JFileChooser fc;
	private JTextField field;
	public ClientChatFrame(ClientMainFrame mainFrame, String targetID) {
		super();
		setTitle("EasyChat -- " + targetID);
		this.mainFrame = mainFrame;
		this.targetID = targetID;
		
		

		JScrollPane messageScrollPane = new JScrollPane();
		
		messageArea = new JTextArea(30, 30);
		messageArea.setEditable(false);
		
		messageScrollPane.setViewportView(messageArea);
		//messageScrollPane.setPreferredSize(new Dimension(300,400));
	
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
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				messageScrollPane, fileScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setPreferredSize(new Dimension(500, 300));
		splitPane.setDividerLocation(300);
		
		add(splitPane, BorderLayout.CENTER);
		
		inputPanel = new JPanel(new BorderLayout());
		
		buttonPanel = new JPanel();
		
		fileButton = new JButton("file");
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
		buttonPanel.add(fileButton);
		
		inputPanel.add(buttonPanel, BorderLayout.NORTH);
		
		field = new JTextField(30);
		field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = ClientChatFrame.this.mainFrame.getClient().getID() + " : " + field.getText();
				ClientChatFrame.this.mainFrame.sendChatMessage(str, ClientChatFrame.this.targetID);
				field.setText("");
			}
		});
		
		inputPanel.add(field, BorderLayout.CENTER);
		
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
		setVisible(true);
	}
	
	public void printChatMessage(String str) {
		//System.out.println(outputPanel.getPreferredSize());
		messageArea.append(str + "\n");
	}
	
	
	public void updateFile(String fileName) {
		fileNames.addElement(fileName);
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
