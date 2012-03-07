package com.HappyChat.util;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * һ����װ��LookAndFeel�Ӳ˵� �����ڸı������LookAndFeel ��ЩLookAndFeel��JRE�Դ��� LookAndFeel
 * ��Щ���������һ����������� �������ſɸ���LookAndFeel
 * 
 * @author QIU_BaiChao
 */
public class JLookAndFeelMenu extends JMenu {
	/**
	 * Ĭ�ϵĲ˵���
	 */
	private static final String defaultMenuName = "LookAndFeel";

	/**
	 * �õ�ϵͳ�İ�װ������LookAndFeel
	 */
	UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();

	/**
	 * ��ť��,��һ��LookAndFeel�ĵ�ѡ��ť��������
	 */
	ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * �����������LookAndFeelֻ�ܸ��� ��ͬһ���������LookAndFeel
	 */
	Component parent;

	/**
	 * ���캯��
	 * 
	 * @param menuName
	 *            �˵���
	 * @param parrent
	 *            �����������ͬһ������ ������������Component����LookAndFeel
	 */
	public JLookAndFeelMenu(String menuName, final Component parent) {
		// ���ɸ�������ѡ��LookAndFeel�ĵ�ѡ��ť�Ӳ˵�
		// generateLookAndFeelSubMenu(parent);
		setParentComponent(parent);
		// ���ò˵���
		this.setText(menuName);
	}

	/**
	 * ���캯�� ʹ��Ĭ�ϵĲ˵���:LookAndFeel
	 * 
	 * @param parent
	 *            �����������ͬһ������������� ����Component����LookAndFeel
	 */
	public JLookAndFeelMenu(Component parent) {
		this(defaultMenuName, parent);
	}

	/**
	 * Ĭ�Ϲ��캯�� ʹ��Ĭ�ϵĲ˵����� �������ø�������� ����setParentComponent(Component parent)����
	 */
	public JLookAndFeelMenu() {
		super(defaultMenuName);
	}

	/**
	 * ����LookAndFeel�ĵ�ѡ�Ӳ˵�
	 * 
	 * @param parent
	 */
	private void generateLookAndFeelSubMenu(final Component parent) {
		for (int i = 0; i < info.length; i++) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(info[i]
					.getName(), i == 0);
			final String className = info[i].getClassName();
			// �����¼�����
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evn) {
					try {
						UIManager.setLookAndFeel(className);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// ���������LookAndFeel
					SwingUtilities.updateComponentTreeUI(parent);
				}
			});
			buttonGroup.add(item);
			add(item);
		}
	}

	/**
	 * �õ������
	 * 
	 * @return
	 */
	public Component getParentComponent() {
		return parent;
	}

	/**
	 * ���ø����
	 * 
	 * @param parent
	 */
	public void setParentComponent(Component parent) {
		this.parent = parent;
		generateLookAndFeelSubMenu(parent);
	}

	/**
	 * ��ʾ����ʹ�����LookAndFeel�˵�
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setLocation(200, 200);
		JMenuBar bar = new JMenuBar();
		JLookAndFeelMenu menu = new JLookAndFeelMenu();
		menu.setText("���ѡ��");
		menu.setParentComponent(frame);
		bar.add(menu);
		frame.getContentPane().add(BorderLayout.NORTH, bar);
		frame.getContentPane().add(BorderLayout.CENTER, new JButton("Hello"));
		frame.setVisible(true);
	}

}
