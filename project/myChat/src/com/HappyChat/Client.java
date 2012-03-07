package com.HappyChat;


public class Client extends Thread {

	public Client() {
		this.start();
	}

	public void run() {
		new ClientThread();
	}

	public static void main(String[] args) {
		new Client();
	}
}
