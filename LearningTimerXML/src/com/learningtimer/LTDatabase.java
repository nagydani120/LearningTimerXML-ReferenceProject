package com.learningtimer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import com.learningtimer.dataStoreObjects.OneDay;

public class LTDatabase {

	public static final String URL = "jdbc:mysql://127.0.0.1:3306/ltdata";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "MyPassword";

	public static void main(String[] args) {

		try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			Statement st = conn.createStatement();
			st.execute("CREATE TABLE IF NOT EXISTS ltdata (date DATE, project TEXT, timePassed TIME,archived BOOLEAN)");
//			ResultSet result = st.executeQuery("SELECT * FROM ltdata");

			Map<LocalDate, OneDay> redData = LTFileHandler.loadData();

			redData.entrySet()
					.stream()
					.forEach(es -> es.getValue()
							.getTimeSessions()
							.stream()
							.filter(ts -> ts.getTimePassed()
									.isAfter(LocalTime.of(0, 0, 1)))
							.forEach(ts -> {
								try {
									System.out.println("INSERT INTO ltdata VALUES (" + es.getKey() + " , " + "\""
											+ ts.getProject()
											.getProjectName()
									+ "\", '" + ts.getTimePassed() + "', " + ts.isArchived() + ")");
									st.execute("INSERT INTO ltdata VALUES ('" + es.getKey() + "' , " + "\""
											+ ts.getProject()
													.getProjectName()
											+ "\", '" + ts.getTimePassed() + "', " + ts.isArchived() + ")");
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}));

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
