package com.HappyChat.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.print.attribute.AttributeSet;
import javax.swing.ActionMap;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.jdesktop.application.Application;

import com.HappyChat.util.JLookAndFeelMenu;
import com.sun.xml.internal.txw2.Document;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ServerFrame extends javax.swing.JFrame {
	private JTabbedPane jTabbedPane1;
	public JButton btnClose;// 关闭服务器
	public JButton btnSaveLog;// 保存日志

	private JLabel lblPort;
	private JTextField txtMaxSize;// 最大连接数
	public JTextField txtPort;// 端口
	private JTextField txtProtocol;// 协议
	public JTextField txtOnline;// 在线人数
	public JTextField txtServerIP;// 服务器ip
	public JTextField txtServiceName;// 服务器名称
	public JTextField txtState;// 服务器状态

	private JLabel lblNumber;
	private JLabel lblMax;
	private JLabel lblServerName;
	private JLabel lblProtocol;
	private JLabel lblIP;
	private JLabel lblMessage;
	private JLabel lblSave;
	private JMenuItem miServer;
	private JMenuItem miClient;
	private JMenuItem miExit;
	private JMenu mfunction;
	private JMenuBar mbMain;
	public JCheckBox cbShowStyle;

	public JTextPane txtpLogs;// 日志
	StyledDocument styleDoc_Log = new DefaultStyledDocument();

	private JScrollPane spLogs;
	public JTextPane txtpUserMessage;// 用户消息
	StyledDocument styledDoc_Message = new DefaultStyledDocument();
	private JScrollPane spUserMessage;

	public JTextPane txtpEditerServerMessage;// 系统消息编辑区
	private JScrollPane spServerMessage;

	public JList listOnlineUser;// 在线用户列表

	public JButton btnTI;// 踢
	public JButton btnSend;// 发送系统消息

	private JLabel lblOnlineUser;
	private JPanel PanelMessage;
	private JPanel panelOnlineUser;
	private JLabel lblState;
	private JPanel jPanel2;
	private JPanel jPanel1;
	private JLabel jlServerInfo;
	public JComboBox cmbSave;
	private JLabel lblLog;
	private JDesktopPane jDesktopPane2;
	private JDesktopPane jDesktopPane1;

	public static String style = "style";

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ServerFrame inst = new ServerFrame();
				inst.setLocationRelativeTo(null);
			}
		});
	}

	public ActionMap getAppActionMap() {
		return Application.getInstance().getContext().getActionMap(this);
	}

	public ServerFrame() {
		super();
		/*
		 * try { // 使用Windows的界面风格 UIManager
		 * .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		initGUI();
	}

	/**
	 * 
	 */
	private void initGUI() {
		try {
			{
				this.setTitle("服务器");
				this.setResizable(false);
				{
					mbMain = new JMenuBar();
					setJMenuBar(mbMain);
					{
						mfunction = new JMenu();
						mfunction.setText("选项");
						mbMain.add(mfunction);
						mfunction.setName("mfunction");
						{
							miServer = new JMenuItem();
							mfunction.add(miServer);
							miServer.setName("miServer");
							miServer.setText("服务器管理");
							miServer.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									jTabbedPane1.setSelectedIndex(0);
								}
							});
						}
						{
							miClient = new JMenuItem();
							mfunction.add(miClient);
							miClient.setName("miClient");
							miClient.setText("用户信息管理");
							miClient.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									jTabbedPane1.setSelectedIndex(1);
								}
							});
						}
						{
							miExit = new JMenuItem();
							mfunction.add(miExit);
							miExit.setName("miExit");
							miExit.setText("退出");
							miExit.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									System.exit(0);
								}
							});
						}
					}
					{
						JLookAndFeelMenu menu = new JLookAndFeelMenu();
						menu.setParentComponent(this);
						menu.setText("外观选择");
						mbMain.add(menu);
						menu.setName("menu");
					}
				}
				jTabbedPane1 = new JTabbedPane();
				getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
				jTabbedPane1.setPreferredSize(new java.awt.Dimension(577, 498));
				jTabbedPane1.setName("jTabbedPane1");
				{
					/**
					 *服务器管理
					 * */
					jDesktopPane1 = new JDesktopPane();
					jTabbedPane1.addTab("服务器管理", null, jDesktopPane1, null);
					jDesktopPane1.setName("jdpServerInfo");
					jDesktopPane1.setPreferredSize(new java.awt.Dimension(570,
							471));
					{
						// 退出服务器
						btnClose = new JButton();
						jDesktopPane1.add(btnClose);
						btnClose.setBounds(187, 440, 125, 22);
						btnClose.setName("btnClose");
					}
					{
						// 保存日志
						btnSaveLog = new JButton();
						jDesktopPane1.add(btnSaveLog);
						btnSaveLog.setBounds(363, 440, 120, 22);
						btnSaveLog.setName("btnSaveLog");
					}
					{
						lblLog = new JLabel();
						jDesktopPane1.add(lblLog);
						lblLog.setBounds(175, 12, 137, 15);
						lblLog.setName("Log");
					}
					{
						jPanel2 = new JPanel();
						GridLayout jPanel2Layout = new GridLayout(1, 1);
						jPanel2Layout.setColumns(1);
						jPanel2Layout.setHgap(5);
						jPanel2Layout.setVgap(5);
						jDesktopPane1.add(jPanel2);
						jPanel2.setBounds(175, 33, 387, 401);
						jPanel2.setLayout(jPanel2Layout);
						{
							spLogs = new JScrollPane();
							jPanel2.add(spLogs);
							spLogs.setPreferredSize(new java.awt.Dimension(387,
									384));
							{
								txtpLogs = new JTextPane(styleDoc_Log);
								txtpLogs.setEditable(false);
								spLogs.setViewportView(txtpLogs);
								txtpLogs
										.setPreferredSize(new java.awt.Dimension(
												384, 398));
							}
						}
					}
					{
						jPanel1 = new JPanel();
						jDesktopPane1.add(jPanel1);
						jPanel1.setBounds(11, 33, 158, 400);
						jPanel1.setLayout(null);
						{
							lblState = new JLabel();
							jPanel1.add(lblState);
							lblState.setBounds(12, 6, 122, 15);
							lblState.setName("lblState");
						}
						{
							txtState = new JTextField();
							jPanel1.add(txtState);
							txtState.setBounds(12, 25, 141, 22);
						}
						{
							lblPort = new JLabel();
							jPanel1.add(lblPort);
							lblPort.setName("lblPort");
							lblPort.setBounds(12, 333, 99, 21);
						}
						{
							lblIP = new JLabel();
							jPanel1.add(lblIP);
							lblIP.setName("lblIP");
							lblIP.setBounds(12, 113, 90, 22);
						}
						{
							lblProtocol = new JLabel();
							jPanel1.add(lblProtocol);
							lblProtocol.setName("lblProtocol");
							lblProtocol.setBounds(12, 272, 74, 21);
						}
						{
							lblServerName = new JLabel();
							jPanel1.add(lblServerName);
							lblServerName.setName("lblServerName");
							lblServerName.setBounds(12, 55, 122, 15);
						}
						{
							lblMax = new JLabel();
							jPanel1.add(lblMax);
							lblMax.setName("lblMax");
							lblMax.setBounds(12, 169, 97, 17);
						}
						{
							lblNumber = new JLabel();
							jPanel1.add(lblNumber);
							lblNumber.setName("lblNumber");
							lblNumber.setBounds(12, 220, 99, 18);
						}
						{
							txtServiceName = new JTextField();
							jPanel1.add(txtServiceName);
							txtServiceName.setBounds(12, 76, 141, 22);
						}
						{
							txtServerIP = new JTextField();
							jPanel1.add(txtServerIP);
							txtServerIP.setBounds(12, 141, 141, 22);
							txtServerIP.setName("txtServerIP");
						}
						{
							txtMaxSize = new JTextField();
							jPanel1.add(txtMaxSize);
							txtMaxSize.setBounds(12, 192, 82, 22);
							txtMaxSize.setName("txtMaxSize");
						}
						{
							txtOnline = new JTextField();
							jPanel1.add(txtOnline);
							txtOnline.setBounds(12, 244, 82, 22);
							txtOnline.setName("txtOnline");
						}
						{
							txtProtocol = new JTextField();
							jPanel1.add(txtProtocol);
							txtProtocol.setBounds(12, 299, 82, 22);
							txtProtocol.setName("txtProtocol");
						}
						{
							txtPort = new JTextField();
							jPanel1.add(txtPort);
							txtPort.setBounds(12, 360, 82, 22);
							txtPort.setName("txtPort");
						}
					}
					{
						jlServerInfo = new JLabel();
						jDesktopPane1.add(jlServerInfo);
						jlServerInfo.setBounds(12, 12, 145, 15);
						jlServerInfo.setName("jlServerInfo");
					}
				}
				/**
				 *用户信息管理
				 * */
				{
					jDesktopPane2 = new JDesktopPane();
					jTabbedPane1.addTab("用户信息管理", null, jDesktopPane2, null);
					jDesktopPane2.setName("jdpUserinfo");
					jDesktopPane2.setPreferredSize(new java.awt.Dimension(568,
							483));
					{
						panelOnlineUser = new JPanel();
						GridLayout panelOnlineUserLayout = new GridLayout(1, 1);
						panelOnlineUserLayout.setColumns(1);
						panelOnlineUserLayout.setHgap(5);
						panelOnlineUserLayout.setVgap(5);
						panelOnlineUser.setLayout(panelOnlineUserLayout);
						jDesktopPane2.add(panelOnlineUser);
						panelOnlineUser.setBounds(382, 33, 171, 405);
						{
							listOnlineUser = new JList();
							panelOnlineUser.add(listOnlineUser);
							listOnlineUser
									.setPreferredSize(new java.awt.Dimension(
											180, 402));
						}

					}
					{
						PanelMessage = new JPanel();
						GridLayout PanelMessageLayout = new GridLayout(1, 1);
						PanelMessageLayout.setColumns(1);
						PanelMessageLayout.setHgap(5);
						PanelMessageLayout.setVgap(5);
						PanelMessage.setLayout(PanelMessageLayout);
						jDesktopPane2.add(PanelMessage);
						PanelMessage.setBounds(7, 33, 369, 323);
						{
							spUserMessage = new JScrollPane();
							PanelMessage.add(spUserMessage);
							spUserMessage.setName("spUserMessage");
							{
								txtpUserMessage = new JTextPane(
										styledDoc_Message);
								txtpUserMessage.setEditable(false);
								spUserMessage.setViewportView(txtpUserMessage);
							}
						}
					}
					{
						lblMessage = new JLabel();
						jDesktopPane2.add(lblMessage);
						lblMessage.setBounds(12, 12, 169, 15);
						lblMessage.setName("lblMessage");
					}
					{
						lblOnlineUser = new JLabel();
						jDesktopPane2.add(lblOnlineUser);
						lblOnlineUser.setBounds(389, 12, 173, 15);
						lblOnlineUser.setName("lblOnlineUser");
					}
					{
						btnSend = new JButton();
						jDesktopPane2.add(btnSend);
						btnSend.setBounds(277, 443, 75, 22);
						btnSend.setName("btnSend");
						btnSend.setMnemonic(KeyEvent.VK_ENTER);
					}
					{
						btnTI = new JButton();
						jDesktopPane2.add(btnTI);
						btnTI.setBounds(409, 443, 78, 22);
						btnTI.setName("btnTI");
					}
					{
						spServerMessage = new JScrollPane();
						jDesktopPane2.add(spServerMessage);
						spServerMessage.setBounds(7, 386, 369, 52);
						{
							txtpEditerServerMessage = new JTextPane();
							spServerMessage
									.setViewportView(txtpEditerServerMessage);
						}
					}
					{
						cbShowStyle = new JCheckBox();
						jDesktopPane2.add(cbShowStyle);
						cbShowStyle.setBounds(249, 360, 135, 19);
						cbShowStyle.setName("cbShowStyle");
						cbShowStyle.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								if (cbShowStyle.isSelected()) {
									style = "style";
								} else {
									style = "";
								}
							}
						});
					}
					{
						ComboBoxModel cmbSaveModel = new DefaultComboBoxModel(
								new String[] { "File", "Oracle" });
						cmbSave = new JComboBox();
						jDesktopPane2.add(cmbSave);
						cmbSave.setModel(cmbSaveModel);
						cmbSave.setBounds(99, 358, 75, 22);
					}
					{
						lblSave = new JLabel();
						jDesktopPane2.add(lblSave);
						lblSave.setBounds(9, 352, 86, 39);
						lblSave.setName("lblSave");
					}
				}
			}
			pack();
			Application.getInstance().getContext().getResourceMap(getClass())
					.injectComponents(getContentPane());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示用户聊天信息
	 * 
	 * @param message
	 *            消息
	 */
	public void InsertMessage(String message) {
		InsertMessage(message, styledDoc_Message.getStyle(style));
	}

	/**
	 * 显示用户聊天信息
	 * 
	 * @param message
	 * @param style
	 *            文本样式
	 */
	public void InsertMessage(String message, Style style) {
		try {
			this.styledDoc_Message.insertString(this.styledDoc_Message
					.getLength(), message, style);
		} catch (BadLocationException e) {
			System.err.println("BadLocationException: " + e);
		}
	}

	/**
	 * 插入图片
	 * 
	 * @param icon
	 */
	public void InsertIcon(Icon icon) {
		txtpUserMessage.setCaretPosition(txtpUserMessage.getStyledDocument()
				.getLength());
		this.txtpUserMessage.insertIcon(icon);
	}

	/**
	 * 插入日志信息
	 * 
	 * @param logs
	 *            日志
	 */
	public void InsertLogs(String logs) {
		try {
			this.styleDoc_Log.insertString(this.styleDoc_Log.getLength(), logs,
					null);
		} catch (BadLocationException e) {
			System.err.println("BadLocationException: " + e);
		}
		this.txtpLogs.selectAll();
	}

	/**
	 * 创建显示字体
	 * 
	 * @param size
	 *            大小
	 * @param bold
	 *            粗体
	 * @param italic
	 *            斜体
	 * @param underline
	 *            下划线
	 * @param color
	 *            颜色
	 * @param fontName
	 *            字体
	 */
	public void createStyle(int size, int style, int underline, Color color,
			String fontName) {
		Style sys = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);
		try {
			this.styledDoc_Message.removeStyle("style");
		} catch (Exception e) {
		} // 先删除这种Style,假使他存在
		Style s = styledDoc_Message.addStyle("style", sys); // 加入
		StyleConstants.setFontSize(s, size); // 大小
		StyleConstants.setBold(s, (style == 1 || style == 3) ? true : false); // 粗体
		StyleConstants.setItalic(s, (style == 2 || style == 3) ? true : false); // 斜体
		StyleConstants.setUnderline(s, (underline == 1) ? true : false); // 下划线
		StyleConstants.setForeground(s, color); // 颜色
		StyleConstants.setFontFamily(s, fontName); // 字体
	}
}
