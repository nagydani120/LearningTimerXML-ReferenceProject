package com.learningtimer.moreWindowButtons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import com.learningtimer.LTFileHandler;

@SuppressWarnings("serial")
public class ChangePTimeButton extends JButton implements ActionListener {
	private JCheckBox progressBarCheckBox;

	public ChangePTimeButton(String buttonName, JCheckBox progressBarCheckBox) {
		super(buttonName);
		this.setBounds(139, 127, 139, 42);
		this.progressBarCheckBox = progressBarCheckBox;
		this.addActionListener(this);
		this.setFocusable(false);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Pattern pattern = Pattern.compile("^[0-9]{2,3}$");
		String answer = JOptionPane.showInputDialog(getComponentPopupMenu(), "Please give the time in minutes:", null,
				JOptionPane.OK_CANCEL_OPTION);
		Matcher matcher = pattern.matcher(answer);
		if (matcher.find()) {
			int valueInSec = Integer.parseInt(answer) * 60;
			LocalTime timeSet = LocalTime.ofSecondOfDay(valueInSec);

			int answerToRestart = JOptionPane.showConfirmDialog(getComponentPopupMenu(),
					"Your new setting for the daily progress time is "
							+ timeSet.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
							+ "\n To update settings please restart the program. \n \n (This setting has no effect for the "
							+ "older sessions progress percent)",
					null, JOptionPane.OK_CANCEL_OPTION);
			System.out.println(answerToRestart);
			// 0 if true 2 if cancel
			if (answerToRestart == 0) {
				LTFileHandler.saveSettings(progressBarCheckBox.isSelected(), valueInSec);
				System.exit(0);
			} else {
				return;
			}

		} else {
			JOptionPane.showMessageDialog(getComponentPopupMenu(),
					"Invalid minute value. \n -Value cant contain letter! \n -Value must be between 10 - 999 min!",
					null, JOptionPane.ERROR_MESSAGE);
		}

	}

}
