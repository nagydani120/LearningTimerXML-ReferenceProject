package com.learningtimer.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

import com.learningtimer.moreWindowButtons.ChangePTimeButton;
import com.learningtimer.moreWindowButtons.ClearButton;
import com.learningtimer.moreWindowButtons.HistoryButton;

@SuppressWarnings("serial")
public class MoreOptionWindow extends JFrame implements ItemListener {

	private JTextPane txtClearTheLog;
	private JTextPane txtHistory;
	private JButton btnClear;
	private JButton btnMonthHistory;
	private JButton btnChange;
	private JCheckBox chckbxShowProgressPercent;

	private JProgressBar progressBar;


	/**
	 * Create the application.
	 */
	public MoreOptionWindow(JProgressBar progressBar) {
		this.progressBar = progressBar;
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setResizable(false);
		this.setBounds(100, 100, 300, 245);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// The buttons creates a new window

		txtClearTheLog = new JTextPane();
		txtClearTheLog.setFocusable(false);
		txtClearTheLog.setFont(new Font("Dialog", Font.PLAIN, 15));
		txtClearTheLog.setEditable(false);
		txtClearTheLog.setText("Clear the log");
		txtClearTheLog.setBounds(22, 16, 114, 19);
		txtClearTheLog.setBackground(new Color(238, 238, 238));
		this.getContentPane()
				.add(txtClearTheLog);

		txtHistory = new JTextPane();
		txtHistory.setFont(new Font("Dialog", Font.PLAIN, 15));
		txtHistory.setEditable(false);
		txtHistory.setFocusable(false);
		txtHistory.setText("History");
		txtHistory.setBounds(22, 74, 114, 19);
		txtHistory.setBackground(new Color(238, 238, 238));

		chckbxShowProgressPercent = new JCheckBox("Show progress percent");
		chckbxShowProgressPercent.setSelected(progressBar.isVisible());
		chckbxShowProgressPercent.setFont(new Font("Dialog", Font.BOLD, 11));
		chckbxShowProgressPercent.setFocusable(false);
		chckbxShowProgressPercent.setBounds(22, 177, 191, 23);
		chckbxShowProgressPercent.addItemListener(this);

		JTextPane txtpnSetProgressTime = new JTextPane();
		txtpnSetProgressTime.setFont(new Font("Dialog", Font.PLAIN, 15));
		txtpnSetProgressTime.setText("Progress time");
		txtpnSetProgressTime.setBounds(22, 136, 114, 21);
		txtpnSetProgressTime.setBackground(new Color(238, 238, 238));
		txtpnSetProgressTime.setEditable(false);

		btnChange = new ChangePTimeButton("Change", chckbxShowProgressPercent);
		btnClear = new ClearButton("Clear");
		btnMonthHistory = new HistoryButton("Daily History");

		this.getContentPane()
				.add(chckbxShowProgressPercent);
		this.getContentPane()
				.add(txtHistory);
		this.getContentPane()
				.add(btnChange);
		this.getContentPane()
				.add(txtpnSetProgressTime);
		this.getContentPane()
				.add(btnMonthHistory);
		this.getContentPane()
				.setLayout(null);
		this.getContentPane()
				.add(btnClear);

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			progressBar.setVisible(true);
		} else {
			progressBar.setVisible(false);
		}
	}
}
