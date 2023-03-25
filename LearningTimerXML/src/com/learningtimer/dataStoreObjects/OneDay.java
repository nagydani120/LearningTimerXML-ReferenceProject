package com.learningtimer.dataStoreObjects;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class OneDay {

	private LocalDate date;
	private ArrayList<TimeSession> timeSessions = new ArrayList<>();
	private LocalTime timeSum = LocalTime.MIN;
	private double dailyProgressPercent;

	public OneDay(LocalDate date) {
		this.date = date;
	}

	public OneDay(LocalDate date, LocalTime timeSum, double dailyProgressPercent) {
		super();
		this.date = date;
		this.timeSum = timeSum;
		this.dailyProgressPercent = dailyProgressPercent;
	}

	public LocalDate getDate() {
		return date;
	}

	public ArrayList<TimeSession> getTimeSessions() {
		return timeSessions;
	}

	public LocalTime getTimeSum() {
		return timeSum;
	}

	public void setTimeSum(LocalTime timeSum) {
		this.timeSum = timeSum;
	}

	public double getDailyProgressPercent() {
		return dailyProgressPercent;
	}

	// just for testing purpose
	@Override
	public String toString() {
		return "OneDay [numberOfTimeSessionsThisDay=" + timeSessions.size() + ", date=" + date + ", timeSessions="
				+ timeSessions + ", timeSum=" + timeSum + ", dailyProgressPercent=" + dailyProgressPercent + "]\n";
	}

	public void setDailyProgressPercent(double dailyProgressPercent) {
		this.dailyProgressPercent = dailyProgressPercent;
	}

}
