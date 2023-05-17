package com.learningtimer.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import com.learningtimer.LTFileHandler;
import com.learningtimer.LTTableHandler;
import com.learningtimer.dataStoreObjects.OneDay;

@SuppressWarnings("serial")
public class ProgressHistoryWindow extends JFrame {

	private JTable table;
	private JPanel panel;
	private JTextPane txtDays;
	private JTextPane txtAverageTime;
	private DefaultTableModel model;

	/**
	 * Launch the application.
	 * 
	 */
	public ProgressHistoryWindow(String name) {
		super(name);
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		table = new JTable();
		panel = new JPanel();
		model = new DefaultTableModel(new String[] { "Day", "Time", "Progress %" }, 0);
		JScrollPane scrollPane = new JScrollPane();

		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setHgap(50);

		this.setBounds(300, 150, 500, 400);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		scrollPane.setFocusable(false);
		scrollPane.setViewportView(table);

		table.setEnabled(false);
		table.setModel(model);

		this.getContentPane()
				.add(panel, BorderLayout.SOUTH);
		this.getContentPane()
				.add(scrollPane, BorderLayout.CENTER);

		// the "Days" and the "Average time" text located on the bottom
		txtDays = new JTextPane();
		txtDays.setEditable(false);
		txtDays.setFocusable(false);
		txtDays.setBackground(new Color(238, 238, 238));

		txtAverageTime = new JTextPane();
		txtAverageTime.setFocusable(false);
		txtAverageTime.setEditable(false);
		txtAverageTime.setBackground(new Color(238, 238, 238));

		panel.add(txtAverageTime);
		panel.add(txtDays);
		fillTheHistoryFrame();

	}

	private void fillTheHistoryFrame() {
		LTTableHandler.fillTheHistoryTable(model);
		txtAverageTime.setText("Average time:   " + calculateAverage());
		txtDays.setText("Days:   " + table.getRowCount());
	}

	private LocalTime calculateAverage() {

		Map<LocalDate, OneDay> redData = LTFileHandler.loadData();
		double average = redData.entrySet()
				.stream()
				.mapToInt(m -> m.getValue()
						.getTimeSum()
						.toSecondOfDay())
				.summaryStatistics()
				.getAverage();
		return LocalTime.ofSecondOfDay((int) average);
	}
}
