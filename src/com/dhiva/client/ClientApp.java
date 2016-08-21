package com.dhiva.client;

import java.util.ArrayList;

public class ClientApp {
	public static void main(String[] args) {
		ArrayList<Thread> threadPool = new ArrayList<>();
		int i = 100;
		try {
			while (i >= 0) {
				@SuppressWarnings("resource")
				ClientThreads clientThreadObj = new ClientThreads();
				Thread thread = new Thread(clientThreadObj);
				threadPool.add(thread);
				i--;
			}
			for (Thread t : threadPool) {
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
