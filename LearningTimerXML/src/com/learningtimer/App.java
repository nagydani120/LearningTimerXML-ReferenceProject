package com.learningtimer;

import java.awt.EventQueue;

import com.learningtimer.windows.MainWindow;

public class App {

	public static void main(String[] args) {

		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow mw = new MainWindow();
					mw.getMainFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
