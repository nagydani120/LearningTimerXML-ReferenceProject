package com.learningtimer.projectWindowButtons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.learningtimer.LTFileHandler;
import com.learningtimer.dataStoreObjects.OneDay;
import com.learningtimer.dataStoreObjects.Project;
import com.learningtimer.dataStoreObjects.TimeSession;

public class AddButton extends JButton implements ActionListener {

	DefaultTableModel model;

	public AddButton(String buttonName, DefaultTableModel model) {
		super(buttonName);
		this.model = model;
		this.setFocusable(false);
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		projectNameField();
	}

	private void projectNameField() {
		String projectName;
		String error = null;
		do {
			projectName = JOptionPane.showInputDialog(null, "Please give the projects name:", "Create project",
					JOptionPane.PLAIN_MESSAGE);
			error = checkProjectName(projectName);
			if (error != null) {
				JOptionPane.showMessageDialog(null, error, null, JOptionPane.ERROR_MESSAGE);
				projectName = null;
			}
		} while (error != null);

		//
		if (projectName != null && projectName.length() > 2) {
			Project p = new Project(projectName);
			model.addRow(new String[] { p.getProjectName(), "0 hour 0 min. 0 sec." });

			OneDay od = new OneDay(LocalDate.now(), LocalTime.MIN, 0);
			TimeSession sess = new TimeSession(LocalDateTime.now(), LocalTime.MIN, p, false);
			LTFileHandler.dataWriter(od, sess);

		}
	}

	private String checkProjectName(String projectName) {
		if (projectName == null) {
			return null;
		}
		Map<LocalDate, OneDay> loadData = LTFileHandler.loadData();
		boolean anyMatch = loadData.entrySet()
				.stream()
				.anyMatch(d -> d.getValue()
						.getTimeSessions()
						.stream()
						.anyMatch(ts -> ts.getProject()
								.getProjectName()
								.equals(projectName)));

		// settings saver
		// set the progress timer
		Pattern pattern = Pattern.compile("^[a-zA-Z]");
		Matcher matcher = pattern.matcher(projectName);

		if (anyMatch) {
			return "This project already exists";
		} else if (!matcher.find()) {
			return "The project name has to start with letter";
		} else if (projectName.length() < 3) {
			return "Project name has to be at least 3 characters length!";
		} else if (projectName.length() > 20) {
			return "Project name has to be maximum 20 characters length!";
		}

		return null;
	}

}
