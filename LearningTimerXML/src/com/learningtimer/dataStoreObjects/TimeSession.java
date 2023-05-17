package com.learningtimer.dataStoreObjects;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.learningtimer.windows.MainWindow;

public class TimeSession {

	private LocalDateTime timeWhenStarted;
	private LocalTime timePassed;
	private Project project;
	private double timeSessionPercent;
	private boolean archived;

	public boolean isArchived() {
		return archived;
	}

	public TimeSession(LocalDateTime timeWhenStarted, Project project) {
		this.timeWhenStarted = timeWhenStarted;
		this.project = project;
	}

	public TimeSession(LocalDateTime timeWhenStarted, LocalTime timePassed, Project project, boolean archived) {
		this.timeWhenStarted = timeWhenStarted;
		this.timePassed = timePassed;
		this.project = project;
		this.archived = archived;
	}

	public Project getProject() {
		return project;
	}

	public double getTimeSessionPercent() {
		return timeSessionPercent;
	}

	public LocalDateTime getTimeWhenStarted() {
		return timeWhenStarted;
	}

	public LocalTime getTimePassed() {
		return timePassed;
	}

	public void setTimePassed(LocalTime timePassed) {
		this.timePassed = timePassed;
	}

	public void calculateSessionPercent() {
		// this formats the sessions percent to get readable content in the .xml (not
		// like 11.8451848851515)
		double percentToFormat = (double) timePassed.toSecondOfDay() / MainWindow.TIME_TO_PROGRESS_IN_SECONDS
				* MainWindow.FULL_PERCENT;
		DecimalFormat df = new DecimalFormat("#.###");
		timeSessionPercent = Double.valueOf(df.format(percentToFormat));
	}

// just for testing
	@Override
	public String toString() {
		return "TimeSession [timeWhenStarted=" + timeWhenStarted + ", timePassed=" + timePassed + "]";
	}
}
