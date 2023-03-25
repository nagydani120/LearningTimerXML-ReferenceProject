package com.learningtimer.projectWindowButtons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

public class ContinueButton extends JButton implements ActionListener {

	private DefaultTableModel model;
	private JTextPane txtSelectedProjectName;
	private JFrame frame;
	private JTextPane mainWindowsSelectedProjectText;

	public ContinueButton(String buttonName, JTextPane txtSelectedProjectName, JTextPane mainWindowsSelectedProjectText,
			JFrame frame) {
		super(buttonName);
		this.frame = frame;
		this.txtSelectedProjectName = txtSelectedProjectName;
		this.mainWindowsSelectedProjectText = mainWindowsSelectedProjectText;
		this.setFocusable(false);
		this.addActionListener(this);
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!txtSelectedProjectName.getText()
				.equals(("Not selected"))) {
			mainWindowsSelectedProjectText.setText(txtSelectedProjectName.getText());
			frame.dispose();
		} else {
			JOptionPane.showMessageDialog(frame, "Please select Project first.", null, JOptionPane.INFORMATION_MESSAGE);

			this.setEnabled(false);
		}
	}

}
