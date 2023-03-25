package com.learningtimer.moreWindowButtons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.learningtimer.LTFileHandler;
import com.learningtimer.windows.MainWindow;

public class ClearButton extends JButton implements ActionListener {

	public ClearButton(String buttonName) {
		super(buttonName);
		this.setBounds(139, 0, 139, 39);
		this.setMaximumSize(new Dimension(20, 10));
		this.setFocusable(false);
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int opt = JOptionPane.showInternalConfirmDialog(null, "Are you sure to clear all the existing log?", "Clear",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
		if (opt == 0) {
			LTFileHandler.clearFile();
			MainWindow.reloadFrameData();
		}
	}

}
