package com.HappyChat.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

public class ChatRoom extends javax.swing.JFrame {
	private JPanel pnMain;
	private JLabel lblOnlineUser;
	public JComboBox combUser;
	private JLabel lblTo;
	private JComboBox daXiaoComboBox;
	private JButton btnCheckFontColor;
	/**
	 * �û���Ϣ�༭��
	 * */
	public JTextPane txtpEditerMessage;
	/**
	 * 
	 * */
	StyledDocument styleDoc_U_Message = new DefaultStyledDocument();
	private JScrollPane spEditor;
	/**
	 * �û���Ϣ��ʾ��
	 * */
	public JTextPane txtPMessage;
	private JScrollPane spUserMessage;
	/**
	 * ץ��
	 */
	public JButton btnCatchScreen;
	private JComboBox zitiComboBox;
	private JComboBox yangShiComboBox;

	public JButton btnCancel;
	public JButton btnSend;

	public JList listOnlineUser;

	private JLabel lblUserMessage;

	/**
	 * �������
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ChatRoom inst = new ChatRoom();
				inst.setLocationRelativeTo(null);
				inst.setTitle("����������[�����û�]");
				inst.setVisible(true);
			}
		});
	}

	public ChatRoom() {
		super();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		initGUI();
	}

	/**
	 * 
	 */
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);// �˳�ʱ�����κβ���
			getContentPane().setLayout(null);
			{
				pnMain = new JPanel();
				getContentPane().add(pnMain, "Center");
				pnMain.setLayout(null);
				pnMain.setName("pnMain");
				pnMain.setBounds(0, 0, 654, 485);
				{
					listOnlineUser = new JList();
					pnMain.add(listOnlineUser);
					listOnlineUser.setBounds(483, 23, 168, 452);
				}
				{
					lblOnlineUser = new JLabel();
					pnMain.add(lblOnlineUser);
					lblOnlineUser.setBounds(488, -2, 113, 25);
					lblOnlineUser.setName("lblOnlineUser");
				}
				{
					ComboBoxModel combUserModel = new DefaultComboBoxModel(
							new String[] { "�����û�" });
					combUser = new JComboBox();
					pnMain.add(combUser);
					combUser.setModel(combUserModel);
					combUser.setBounds(43, 346, 92, 22);
				}
				{
					lblTo = new JLabel();
					pnMain.add(lblTo);
					lblTo.setBounds(12, 350, 40, 15);
					lblTo.setName("lblTo");
				}
				{
					lblUserMessage = new JLabel();
					pnMain.add(lblUserMessage);
					lblUserMessage.setBounds(12, 3, 118, 15);
					lblUserMessage.setName("lblUserMessage");
				}
				{
					btnSend = new JButton();
					pnMain.add(btnSend);
					btnSend.setBounds(313, 453, 65, 22);
					btnSend.setName("btnSend");
					btnSend.setMnemonic(KeyEvent.VK_ENTER);
				}
				{
					btnCancel = new JButton();
					pnMain.add(btnCancel);
					btnCancel.setBounds(389, 453, 83, 22);
					btnCancel.setName("btnCancel");
				}
				{
					ComboBoxModel daXiaoComboBoxModel = new DefaultComboBoxModel(
							new String[] { "12", "14", "16", "18", "20" });
					daXiaoComboBox = new JComboBox();
					pnMain.add(daXiaoComboBox);
					daXiaoComboBox.setModel(daXiaoComboBoxModel);
					daXiaoComboBox.setBounds(336, 346, 48, 22);
					daXiaoComboBox.addItemListener(new ItemListener() {
						public void itemStateChanged(ItemEvent arg0) {
							FreshFont();
						}
					});
				}
				{
					ComboBoxModel yangShiComboBoxModel = new DefaultComboBoxModel(
							new String[] { "����", "����", "б��", "��б" });
					yangShiComboBox = new JComboBox();
					pnMain.add(yangShiComboBox);
					yangShiComboBox.setModel(yangShiComboBoxModel);
					yangShiComboBox.setBounds(253, 346, 71, 22);
					yangShiComboBox.addItemListener(new ItemListener() {
						public void itemStateChanged(ItemEvent arg0) {
							FreshFont();
						}

					});
				}
				{
					ComboBoxModel zitiComboBoxModel = new DefaultComboBoxModel(
							GraphicsEnvironment.getLocalGraphicsEnvironment()
									.getAvailableFontFamilyNames());
					zitiComboBox = new JComboBox();
					pnMain.add(zitiComboBox);
					zitiComboBox.setModel(zitiComboBoxModel);
					zitiComboBox.setBounds(147, 346, 94, 22);
					zitiComboBox.addItemListener(new ItemListener() {
						public void itemStateChanged(ItemEvent arg0) {
							FreshFont();
						}
					});
				}
				{
					btnCatchScreen = new JButton();
					pnMain.add(btnCatchScreen);
					btnCatchScreen.setBounds(227, 453, 81, 22);
					btnCatchScreen.setName("btnCatchScreen");
				}
				{
					spUserMessage = new JScrollPane();
					pnMain.add(spUserMessage);
					spUserMessage.setBounds(12, 24, 460, 310);
					{
						txtPMessage = new JTextPane(styleDoc_U_Message);
						txtPMessage.setEditable(false);
						spUserMessage.setViewportView(txtPMessage);
					}
				}
				{
					spEditor = new JScrollPane();
					pnMain.add(spEditor);
					spEditor.setBounds(12, 380, 460, 63);
					{
						txtpEditerMessage = new JTextPane();
						spEditor.setViewportView(txtpEditerMessage);
						txtpEditerMessage.setName("epMessage");
					}
				}
				{
					btnCheckFontColor = new JButton();
					pnMain.add(btnCheckFontColor);
					btnCheckFontColor.setBounds(396, 347, 67, 22);
					btnCheckFontColor.setName("btnCheckFontColor");
					btnCheckFontColor.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							Color c = JColorChooser.showDialog(null, "��ѡ����ɫ",
									Color.black);
							txtpEditerMessage.setForeground(c);
						}
					});
				}
			}
			pack();
			this.setSize(660, 517);
			Application.getInstance().getContext().getResourceMap(getClass())
					.injectComponents(getContentPane());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʾ��Ϣ
	 * 
	 * @param message
	 *            Ҫ��ʾ����Ϣ
	 */
	public void InsertMessage(String message) {
		try {
			this.styleDoc_U_Message
					.insertString(this.styleDoc_U_Message.getLength(), message,
							styleDoc_U_Message.getStyle("style"));
		} catch (BadLocationException e) {
			System.err.println("BadLocationException: " + e);
		}
		this.txtPMessage.selectAll();
	}

	/**
	 * ����ͼƬ
	 * */
	public void InsertIcon(Icon icon) {
		if (icon != null) {
			txtPMessage.setCaretPosition(txtPMessage.getStyledDocument()
					.getLength());
			txtPMessage.insertIcon(icon);
		}
	}

	/**
	 * ˢ������
	 */
	public void FreshFont() {
		String ziti = zitiComboBox.getSelectedItem().toString();
		int style = 0;
		String yangShiString = yangShiComboBox.getSelectedItem().toString();
		if (yangShiString.equals("����")) {
			style = Font.PLAIN;
		} else if (yangShiString.equals("����")) {
			style = Font.BOLD;
		} else if (yangShiString.equals("б��")) {
			style = Font.ITALIC;
		} else if (yangShiString.equals("��б")) {
			style = Font.BOLD | Font.ITALIC;
		}
		int size = Integer
				.parseInt(daXiaoComboBox.getSelectedItem().toString());
		txtpEditerMessage.setFont(new Font(ziti, style, size));
	}

	/**
	 * ������ʾ����
	 * 
	 * @param size
	 *            ��С
	 * @param bold
	 *            ����
	 * @param italic
	 *            б��
	 * @param underline
	 *            �»���
	 * @param color
	 *            ��ɫ
	 * @param fontName
	 *            ����
	 */
	public void createStyle(int size, int style, int underline, Color color,
			String fontName) {
		Style sys = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);
		try {
			this.styleDoc_U_Message.removeStyle("style");
		} catch (Exception e) {
		} // ��ɾ������Style,��ʹ������
		Style s = styleDoc_U_Message.addStyle("style", sys); // ����
		StyleConstants.setFontSize(s, size); // ��С
		StyleConstants.setBold(s, (style == 1 || style == 3) ? true : false); // ����
		StyleConstants.setItalic(s, (style == 2 || style == 3) ? true : false); // б��
		StyleConstants.setUnderline(s, (underline == 1) ? true : false); // �»���
		StyleConstants.setForeground(s, color); // ��ɫ
		StyleConstants.setFontFamily(s, fontName); // ����
	}
}
