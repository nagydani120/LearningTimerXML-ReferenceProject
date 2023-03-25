package com.learningtimer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import com.learningtimer.dataStoreObjects.OneDay;
import com.learningtimer.windows.MainWindow;

public class LTPercentCalculator {
	/*
	 * this method is used to calculate the actual dates progress percent
	 */
	public static double getTodaysProgressPercent(LocalTime timersTime) {
		Map<LocalDate, OneDay> loadData = LTFileHandler.loadData();
		if (loadData != null) {
			long count = loadData.entrySet()
					.stream()
					.filter(ld -> ld.getKey()
							.isEqual(LocalDate.now()))
					.map(o -> o.getValue()
							.getTimeSum())
					.mapToInt(s -> s.toSecondOfDay())
					.sum();
			count = count + timersTime.toSecondOfDay();
			return (double) count / MainWindow.TIME_TO_PROGRESS_IN_SECONDS * MainWindow.FULL_PERCENT;
		} else
			return 0;
	}

}
