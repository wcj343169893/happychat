package com.HappyChat.util;

import java.util.Properties;
import java.util.StringTokenizer;
import java.io.*;

public class CProperties {
	private Properties props = null;

	public CProperties(String filename) {
		try {
			props = this.getProperties(filename);
		} catch (Exception ex) {
		}
	}

	public String getResource(String key) {
		String strRet = "";

		strRet = props.getProperty(key);
		return strRet;
	}

	private Properties getProperties(String filename) throws Exception {
		Properties props = new Properties();
		FileInputStream fs = null;

		try {
			// 取出文件里所有的数据
			File f = new File(filename);
			fs = new FileInputStream(f);

			int filesize = fs.available();
			byte[] contentbytes = new byte[filesize];

			fs.read(contentbytes);
			String contentstr = new String(contentbytes);

			// 按照行分解
			StringTokenizer stoken = new StringTokenizer(contentstr, "\n\r");

			while (stoken.hasMoreTokens()) {
				// 取出每一行
				String line = stoken.nextToken();

				int parsepos = line.indexOf("=");
				String key = line.substring(0, parsepos).trim();
				String val = line.substring(parsepos + 1).trim();

				props.put(key, val);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				fs.close();
			} catch (Exception ex) {
			}
		}

		return props;
	}
}