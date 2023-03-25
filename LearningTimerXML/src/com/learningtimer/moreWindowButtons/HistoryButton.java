package com.learningtimer.moreWindowButtons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.learningtimer.windows.ProgressHistoryWindow;

@SuppressWarnings("serial")
public class HistoryButton extends JButton implements ActionListener {
	private ProgressHistoryWindow mhw;

	public HistoryButton(String buttonName) {
		super(buttonName);
		this.setBounds(139, 64, 139, 39);
		this.addActionListener(this);
		this.setFocusable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		{
			if (mhw == null || !mhw.isVisible()) {
				mhw = new ProgressHistoryWindow("History of daily progress");
			}
		}
	}

}
