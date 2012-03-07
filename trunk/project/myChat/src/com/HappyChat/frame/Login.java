package com.HappyChat.frame;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.jdesktop.application.Application;

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
public class Login extends javax.swing.JFrame {
	private JPanel panelMain;
	private JLabel lblIP;

	public JTextField txtPort;
	public JTextField txtName;
	public JPasswordField pdfPassword;

	public JButton btnCancel;
	public JButton btnLogin;
	public JButton btnRegister;

	private JLabel lblPassword;
	private JLabel lblName;

	public JTextField txtIPAddress;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Login inst = new Login();
				inst.setTitle("登录");
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public Login() {
		super();
		try { // 使用Windows的界面风格
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		setTitle("登录");
		setLocationRelativeTo(null);
		setVisible(true);
		initGUI();

	}

	private void initGUI() {
		try {

			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			GridLayout thisLayout = new GridLayout(1, 1);
			thisLayout.setColumns(1);
			thisLayout.setHgap(5);
			thisLayout.setVgap(5);
			getContentPane().setLayout(thisLayout);
			{
				panelMain = new JPanel();
				getContentPane().add(panelMain);
				panelMain.setPreferredSize(new java.awt.Dimension(360, 250));
				panelMain.setLayout(null);
				{
					lblIP = new JLabel();
					panelMain.add(lblIP);
					lblIP.setBounds(34, 51, 85, 15);
					lblIP.setName("lblIP");
				}
				{
					txtIPAddress = new JTextField();
					panelMain.add(txtIPAddress);
					txtIPAddress.setBounds(97, 48, 174, 22);
					txtIPAddress.setName("txtIPAddress");
				}
				{
					txtPort = new JTextField();
					panelMain.add(txtPort);
					txtPort.setBounds(277, 48, 59, 22);
					txtPort.setName("txtPort");
				}
				{
					lblName = new JLabel();
					panelMain.add(lblName);
					lblName.setBounds(41, 98, 66, 15);
					lblName.setName("lblName");
				}
				{
					txtName = new JTextField();
					panelMain.add(txtName);
					txtName.setBounds(97, 95, 174, 22);
				}
				{
					lblPassword = new JLabel();
					panelMain.add(lblPassword);
					lblPassword.setBounds(51, 140, 34, 15);
					lblPassword.setName("lblPassword");
				}
				{
					btnRegister = new JButton();
					panelMain.add(btnRegister);
					btnRegister.setBounds(36, 195, 79, 22);
					btnRegister.setName("btnRegister");
				}
				{
					btnLogin = new JButton();
					panelMain.add(btnLogin);
					btnLogin.setBounds(120, 195, 91, 22);
					btnLogin.setName("btnLogin");
				}
				{
					btnCancel = new JButton();
					panelMain.add(btnCancel);
					btnCancel.setBounds(216, 195, 85, 22);
					btnCancel.setName("btnCancel");
					btnCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							System.exit(0);
						}
					});
				}
				{
					pdfPassword = new JPasswordField();
					panelMain.add(pdfPassword);
					pdfPassword.setBounds(97, 137, 174, 22);
				}
			}
			pack();
			Application.getInstance().getContext().getResourceMap(getClass())
					.injectComponents(getContentPane());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
