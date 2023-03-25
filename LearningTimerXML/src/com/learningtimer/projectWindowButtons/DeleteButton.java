package com.learningtimer.projectWindowButtons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import com.learningtimer.LTFileHandler;
import com.learningtimer.LTPercentCalculator;
import com.learningtimer.LTTableHandler;
import com.learningtimer.dataStoreObjects.Project;

public class DeleteButton extends JButton implements ActionListener {

	private DefaultTableModel model;
	private JTable table;
	private JTextPane selectedProjectNameText;
	private JTextPane mainWindowsSelectedProjectText;

	public DeleteButton(String buttonName, DefaultTableModel model, JTable table, JTextPane selectedProjectNameText,
			JTextPane mainWindowsSelectedProjectText) {
		super(buttonName);

		this.model = model;
		this.table = table;
		this.selectedProjectNameText = selectedProjectNameText;
		this.mainWindowsSelectedProjectText = mainWindowsSelectedProjectText;
		this.addActionListener(this);
		this.setFocusable(false);
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int answer = JOptionPane.showConfirmDialog(null, "Are you sure to delete project?", "Delete project",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (answer == JOptionPane.YES_OPTION)
			LTFileHandler.removeProject(new Project(model.getValueAt(table.getSelectedRow(), 0)
					.toString()));
		selectedProjectNameText.setText("Not selected");
		mainWindowsSelectedProjectText.setText("Not selected");
		LTTableHandler.fillTheProjectTable(model,false);
		this.setEnabled(false);
	}

}
