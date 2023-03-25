package com.learningtimer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import com.learningtimer.dataStoreObjects.OneDay;
import com.learningtimer.dataStoreObjects.Project;

public class LTTableHandler {

	public static void fillTheHistoryTable(DefaultTableModel model) {
		Map<LocalDate, OneDay> redData = LTFileHandler.loadData();
		redData.entrySet()
				.stream()
				.forEach(m -> model.addRow(new String[] { m.getKey()
						.toString(),
						m.getValue()
								.getTimeSum()
								.toString(),
						"" + m.getValue()
								.getDailyProgressPercent() }));
	}

	/*
	 * Comment
	 */
	public static void fillMainTableWithData(DefaultTableModel model) {
		Map<LocalDate, OneDay> redData = LTFileHandler.loadData();
		model.setRowCount(0);
		if (redData != null) {
			redData.forEach((ld, od) -> od.getTimeSessions()
					.forEach(s -> {
						if (s.getTimePassed()
								.isAfter(LocalTime.of(0, 0, 1))) // IF AT LEAST 1 SECOND
							model.insertRow(0, new String[] { ld.toString(), s.getTimePassed()
									.format(LTFileHandler.timeFormat),
									s.getProject()
											.getProjectName() });
					}));

		}
	}

	public static void fillTheProjectTable(DefaultTableModel projectModel, boolean showArchived) {

		Map<Project, Duration> projectsWithDuration = new LinkedHashMap<>();
		Map<LocalDate, OneDay> redData = LTFileHandler.loadData();

		projectModel.setRowCount(0);
//		boolean archived = true;

		redData.forEach((ld, od) -> od.getTimeSessions()
				.forEach(ts -> {
					boolean archived = ts.isArchived(); // the default setting is to shown the non archived project
					// but if the show archived is true then its just showing the archived

					if (showArchived) {
						archived = !archived;
					}
					if (!archived) { // Just if is archived and the showArchived has been set
						// to true

						if (!projectsWithDuration.containsKey(ts.getProject())) {
							projectsWithDuration.put(ts.getProject(), Duration.ofSeconds(ts.getTimePassed()
									.toSecondOfDay()));
						} else {
							projectsWithDuration.merge(ts.getProject(), Duration.ofSeconds(ts.getTimePassed()
									.toSecondOfDay()), (a, b) -> a.plus(b));
						}
					}
				}));
		projectsWithDuration.forEach((a, b) -> {
			projectModel.addRow(new String[] { a.getProjectName(),
					b.toHoursPart() + " hour " + b.toMinutesPart() + " min. " + b.toSecondsPart() + " sec. " });
		});

	}

	public static void fillSideSessionTable(DefaultTableModel projectSessionsModel, JTextPane txtSelectedProjectName) {
		String text = txtSelectedProjectName.getText();
		projectSessionsModel.setRowCount(0); // clear the columns
		Map<LocalDate, OneDay> redData = LTFileHandler.loadData();
		redData.forEach((ld, od) -> od.getTimeSessions()
				.forEach(ts -> {
					if (ts.getProject()
							.getProjectName()
							.equals(text)) {
						if (ts.getTimePassed()
								.isAfter(LocalTime.of(0, 0, 1))) // IF AT LEAST HAS 1 SECOND
							projectSessionsModel.addRow(new String[] { ld.toString(), ts.getTimePassed()
									.format(LTFileHandler.timeFormat) });
					}
				}));

	}

}
