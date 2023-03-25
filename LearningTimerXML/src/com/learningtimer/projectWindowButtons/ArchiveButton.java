package com.learningtimer.projectWindowButtons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import com.learningtimer.LTFileHandler;
import com.learningtimer.LTTableHandler;
import com.learningtimer.dataStoreObjects.Project;
import com.learningtimer.windows.MainWindow;

@SuppressWarnings("serial")
public class ArchiveButton extends JButton implements ActionListener {

	private DefaultTableModel unarchivedProjectModel;
	private JTextPane selectedProjectName;

	public ArchiveButton(String buttonName, JTextPane selectedProjectName, DefaultTableModel unarchivedProjectModel) {
		super(buttonName);
		this.selectedProjectName = selectedProjectName;
		this.unarchivedProjectModel = unarchivedProjectModel;
		initialize();
	}

	private void initialize() {
		this.setFocusable(false);
		this.addActionListener(this);
		this.setEnabled(false);
		this.addFocusListener(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int answer = JOptionPane.showConfirmDialog(getParent(),
				"Are you sure want to archive? \n Your project is not shown more in this table. \n\n (It has no effect to daily progress history)",
				null, JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.OK_OPTION) {
			LTFileHandler.setArchived(new Project(selectedProjectName.getText()), true);
			LTTableHandler.fillTheProjectTable(unarchivedProjectModel, false);
			selectedProjectName.setText("Not selected");
			MainWindow.reloadFrameData();
			JOptionPane.showMessageDialog(null, "Your project is archived.");
		}
	}

}
