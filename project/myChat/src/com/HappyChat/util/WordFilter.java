package com.HappyChat.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class WordFilter {

	/**
	 * ׼�����˵�����
	 */
	private String word = "";

	/**
	 * �õ����˵�����
	 * 
	 * @return ���˵�����
	 */
	public String getWord() {
		return word;
	}

	/**
	 * ���ù�������
	 * 
	 * @param word
	 *            ��������
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * ���ù�������
	 * 
	 * @param word
	 *            ��������
	 */
	public WordFilter(String word) {
		super();
		this.word = word;
	}

	/**
	 * ����������
	 * 
	 */
	public WordFilter() {
		// TODO �Զ����ɹ��캯�����
	}

	/**
	 * �����������
	 * 
	 */
	public void process() {
		String badWord = this.getFile("OtherWords.txt");
		String badWordList[] = badWord.split(",");
		for (int i = 0; i < badWordList.length; i++) {
			if (word.indexOf(badWordList[i].trim()) != -1) {
				word = "�Ƿ�����,ϵͳ����";
			}
		}

	}

	/**
	 * �õ������ļ�����
	 * 
	 * @param file
	 *            �����ļ���
	 * @return �����ļ�����
	 */
	public String getFile(String file) {
		String fileString = "";
		try {
			File files = new File(file);
			// System.out.println(files.getAbsolutePath());
			FileReader fileReader = new FileReader(files);
			BufferedReader read = new BufferedReader(fileReader);
			while (true) {
				String line = read.readLine();
				if (line == null) {
					break;
				}
				fileString += (line);
				// fileString += (line + "\n");
			}
			read.close();
			// System.out.println(fileString);
		} catch (FileNotFoundException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}

		return fileString;
	}

	/**
	 * �������ݣ����غϷ�����
	 * 
	 * @param word
	 *            ��������
	 * @return �Ϸ�����
	 */
	public static String filter(String word) {
		WordFilter wf = new WordFilter(word);
		wf.process();

		return wf.getWord();

	}

}
