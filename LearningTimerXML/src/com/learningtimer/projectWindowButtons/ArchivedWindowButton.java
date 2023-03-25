package com.learningtimer.projectWindowButtons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import com.learningtimer.windows.ArchivedWindow;

@SuppressWarnings("serial")
public class ArchivedWindowButton extends JButton implements ActionListener {

	private DefaultTableModel unarchivedModel;
	private ArchivedWindow aw;

	public ArchivedWindowButton(String buttonName, DefaultTableModel model) {
		super(buttonName);
		this.setFocusable(false);
		this.addActionListener(this);
		this.unarchivedModel = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (aw == null || !aw.isVisible()) {
			aw = new ArchivedWindow(unarchivedModel);
		}
	}

}
