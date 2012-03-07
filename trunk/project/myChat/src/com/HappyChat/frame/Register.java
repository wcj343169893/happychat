package com.HappyChat.frame;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jdesktop.application.Application;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Register extends javax.swing.JFrame {
	private JPanel pnMain;
	private JLabel lblName;
	public JTextField txtName;
	// 取消 注册
	public JButton btnCancel;
	public JButton btnRegister;
	// 电子邮件
	public JTextField txtEmail;
	private JLabel lblEmail;
	// 年龄
	public JTextField txtAge;
	private JLabel lblAge;
	// 密码 确认密码
	private JLabel lblRePassword;
	public JPasswordField pdfRePassword;
	public JPasswordField pdfPassword;
	private JLabel lblPassword;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Register inst = new Register();
				inst.setLocationRelativeTo(null);
				inst.setTitle("注册");
				inst.setVisible(true);
			}
		});
	}

	public Register() {
		super();
		setTitle("注册");
		setResizable(false);
		setLocationRelativeTo(null);
		this.setVisible(true);
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			{
				pnMain = new JPanel();
				getContentPane().add(pnMain, BorderLayout.CENTER);
				pnMain.setLayout(null);
				pnMain.setName("pnMain");
				{
					lblName = new JLabel();
					pnMain.add(lblName);
					lblName.setBounds(99, 44, 42, 15);
					lblName.setName("lblName");
				}
				{
					txtName = new JTextField();
					pnMain.add(txtName);
					txtName.setBounds(153, 41, 118, 22);
				}
				{
					lblPassword = new JLabel();
					pnMain.add(lblPassword);
					lblPassword.setBounds(104, 78, 30, 15);
					lblPassword.setName("lblPassword");
				}
				{
					pdfPassword = new JPasswordField();
					pnMain.add(pdfPassword);
					pdfPassword.setBounds(152, 75, 119, 22);
				}
				{
					pdfRePassword = new JPasswordField();
					pnMain.add(pdfRePassword);
					pdfRePassword.setBounds(152, 109, 119, 22);
				}
				{
					lblRePassword = new JLabel();
					pnMain.add(lblRePassword);
					lblRePassword.setBounds(85, 112, 67, 15);
					lblRePassword.setName("lblRePassword");
				}
				{
					lblAge = new JLabel();
					pnMain.add(lblAge);
					lblAge.setBounds(104, 144, 30, 15);
					lblAge.setName("lblAge");
				}
				{
					txtAge = new JTextField();
					pnMain.add(txtAge);
					txtAge.setBounds(152, 141, 52, 22);
				}
				{
					lblEmail = new JLabel();
					pnMain.add(lblEmail);
					lblEmail.setBounds(80, 181, 66, 15);
					lblEmail.setName("lblEmail");
				}
				{
					txtEmail = new JTextField();
					pnMain.add(txtEmail);
					txtEmail.setBounds(151, 178, 120, 22);
				}
				{
					btnRegister = new JButton();
					pnMain.add(btnRegister);
					btnRegister.setBounds(71, 219, 80, 22);
					btnRegister.setName("btnRegister");
				}
				{
					btnCancel = new JButton();
					pnMain.add(btnCancel);
					btnCancel.setBounds(203, 219, 85, 22);
					btnCancel.setName("btnCancel");
				}
			}
			pack();
			setSize(400, 300);
			Application.getInstance().getContext().getResourceMap(getClass())
					.injectComponents(getContentPane());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
