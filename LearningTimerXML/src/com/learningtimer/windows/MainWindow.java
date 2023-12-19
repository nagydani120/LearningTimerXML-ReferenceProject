package com.learningtimer.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.learningtimer.LTFileHandler;
import com.learningtimer.LTPercentCalculator;
import com.learningtimer.LTTableHandler;
import com.learningtimer.dataStoreObjects.OneDay;
import com.learningtimer.dataStoreObjects.Project;
import com.learningtimer.dataStoreObjects.TimeSession;

public class MainWindow implements ActionListener {
	// @ formatter:off
	/*
	 * Description.
	 * 
	 * See more about storing the data in LT File Handler class.
	 */
	// @ formatter:on
	public static final int TIME_TO_PROGRESS_IN_SECONDS;
	public static final int FULL_PERCENT = 100;

	private JFrame frame = new JFrame("Learning Timer 1.1");
	private final JTable table = new JTable();
	private JTextPane timePassed;
	private JTextPane passedTimePanel;
	private final JTextPane txtSelectedProject = new JTextPane();
	private static JTextPane txtSelectedProjectName = new JTextPane();

	private static JProgressBar progressBar = new JProgressBar();
	protected static DefaultTableModel model;

	private final JButton btnStart = new JButton();
	private final JButton btnPause = new JButton();
	private final JButton btnStop = new JButton();
	private final JButton btnMore = new JButton("More");
	private final JButton btnProjectMenu = new JButton("Project Manager");

	private ProjectWindow pw;
	private MoreOptionWindow mow;

	private TimeSession session;
	private OneDay thisDay;
	private Timer timer;
	private static LocalTime time = LocalTime.MIN;
	private final JPanel projectPanel = new JPanel();

	private final String columns[] = new String[] { "Date", "Time", "Project" };

	// initialize the daily progress time value and the progress bar is hidden
	// settings
	// from the .xml file if exists
	static {
		if (new File("ltdata.xml").isFile()) {
			Object[] loadSettings = LTFileHandler.loadSettings();
			TIME_TO_PROGRESS_IN_SECONDS = (int) loadSettings[0];
			progressBar.setVisible((boolean) loadSettings[1]);
		} else {
			TIME_TO_PROGRESS_IN_SECONDS = 18000;
		}
	}

	/*
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		JPanel buttonPanel = new JPanel();
		JPanel timerPanel = new JPanel();
		JPanel tablePanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane();

		model = new DefaultTableModel(null, columns);
		timePassed = new JTextPane();
		passedTimePanel = new JTextPane();

		/*
		 * Intialize the main frame,setting the parameters.
		 */

		frame.setBounds(100, 100, 500, 430);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {

			/*
			 * This method saves the changes if no stop button pressed before exit.
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				if (session != null) {
					session.calculateSessionPercent();
					LTFileHandler.dataWriter(thisDay, session);
				}
				LTFileHandler.saveSettings(progressBar.isVisible(), TIME_TO_PROGRESS_IN_SECONDS);
				System.exit(0);
			}
		});
		;
		tablePanel.setBackground(new Color(255, 255, 255));
		tablePanel.setLayout(new BorderLayout(0, 0));
		timerPanel.setLayout(new FlowLayout(1, 55, 5));
		buttonPanel.setLayout(new FlowLayout(1, 30, 5));
		frame.setFocusTraversalKeysEnabled(false);
		frame.setFocusable(true);

		/*
		 * BUTTONS:
		 */
		btnStart.setFocusable(false);
		btnPause.setFocusable(false);
		btnPause.setEnabled(false);
		btnStop.setFocusable(false);
		btnStop.setEnabled(false);
		btnMore.setFocusable(false);
		btnProjectMenu.setFocusable(false);

		btnStart.addActionListener(this);
		btnPause.addActionListener(this);
		btnStop.addActionListener(this);
		btnMore.addActionListener(this);
		btnProjectMenu.addActionListener(this);

		buttonPanel.add(btnMore);
		buttonPanel.add(btnStart);
		buttonPanel.add(btnPause);
		buttonPanel.add(btnStop);

		// the Timer
		timer = new Timer(1000, e -> {
			time = time.plusSeconds(1);
			timePassed.setText(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
			setProgressBarPercent();
		});

		// The scroll panel with table below
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setViewportBorder(new LineBorder(Color.BLACK));
		scrollPane.setViewportView(table);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setEnabled(false);
		table.setModel(model);
		table.setFont(new Font("Arial", Font.ITALIC, 12));
		table.setBorder(new LineBorder(new Color(64, 64, 64)));

		// PROGRESS BAR
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(34, 139, 34));
		setProgressBarPercent();
		// Popup message to show the actual setting for daily time progress
		progressBar.setToolTipText("The actual daily learning target is "
				+ LocalTime.ofSecondOfDay(TIME_TO_PROGRESS_IN_SECONDS).format(LTFileHandler.timeFormat));

		timerPanel.add(passedTimePanel);
		timerPanel.add(timePassed);

		tablePanel.add(scrollPane, BorderLayout.CENTER);
		tablePanel.add(progressBar, BorderLayout.SOUTH);
		tablePanel.add(timerPanel, BorderLayout.NORTH);

		// AUTO GENERATED CODE BY WINDOW BUILDER DESIGN
		GridBagLayout gbl_projectPanel = new GridBagLayout();
		gbl_projectPanel.columnWidths = new int[] { 118, 146, 150, 0 };
		gbl_projectPanel.rowHeights = new int[] { 30, 0 };
		gbl_projectPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_projectPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		projectPanel.setLayout(gbl_projectPanel);

		// PROJECT PANEL

		GridBagConstraints gbc_txtpnProjectMenu = new GridBagConstraints();
		gbc_txtpnProjectMenu.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtpnProjectMenu.insets = new Insets(0, 0, 0, 5);
		gbc_txtpnProjectMenu.gridx = 0;
		gbc_txtpnProjectMenu.gridy = 0;
		projectPanel.add(txtSelectedProject, gbc_txtpnProjectMenu);

		GridBagConstraints gbc_txtpnDef = new GridBagConstraints();
		gbc_txtpnDef.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtpnDef.insets = new Insets(0, 0, 0, 5);
		gbc_txtpnDef.gridx = 1;
		gbc_txtpnDef.gridy = 0;
		projectPanel.add(txtSelectedProjectName, gbc_txtpnDef);

		GridBagConstraints gbc_btnProjectMenu = new GridBagConstraints();
		gbc_btnProjectMenu.fill = GridBagConstraints.BOTH;
		gbc_btnProjectMenu.gridx = 2;
		gbc_btnProjectMenu.gridy = 0;
		projectPanel.add(btnProjectMenu, gbc_btnProjectMenu);

		projectPanel.setBackground(new Color(143, 240, 164));
		projectPanel.setBorder(new EmptyBorder(2, 2, 2, 2));

		frame.getContentPane().add(tablePanel, BorderLayout.CENTER);
		frame.getContentPane().add(projectPanel, BorderLayout.SOUTH);
		frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);

		setButtonsIcon();
		LTTableHandler.fillMainTableWithData(model);
		setMainWindowsTextComponentsAtInitializing();
	}

	private void setMainWindowsTextComponentsAtInitializing() {
		Color greenColor = new Color(143, 240, 164);
		Color timePanelsColor = new Color(238, 238, 238);
		Font selectedProjectPanelColor = new Font("Nimbus Roman No9 L", Font.BOLD, 13);
		Font timePanelsFont = new Font("Dialog", Font.BOLD, 16);
		// Setting the timer panel where is the timer located

		passedTimePanel.setText("Time :");
		passedTimePanel.setFont(timePanelsFont);
		passedTimePanel.setEditable(false);
		passedTimePanel.setBackground(timePanelsColor);

		timePassed.setText("00:00:00");
		timePassed.setFont(timePanelsFont);
		timePassed.setEditable(false);
		timePassed.setBackground(timePanelsColor);

		txtSelectedProject.setBorder(null);
		txtSelectedProject.setFocusable(false);
		txtSelectedProject.setEditable(false);

		txtSelectedProject.setFont(selectedProjectPanelColor);
		txtSelectedProjectName.setFont(selectedProjectPanelColor);

		txtSelectedProject.setBackground(greenColor);
		txtSelectedProjectName.setBackground(greenColor);

		txtSelectedProject.setText(" Selected project: "); // reads from the settings at the start
		txtSelectedProject.setEditable(false);
		txtSelectedProjectName.setText(setLastProjectUsed());
	}

	private void setButtonsIcon() {
		ImageIcon start = new ImageIcon(getClass().getResource("/icons/start.png"));
		ImageIcon pause = new ImageIcon(getClass().getResource("/icons/pause.png"));
		ImageIcon stop = new ImageIcon(getClass().getResource("/icons/stop.png"));

		Image startImage = start.getImage();
		Image pauseImage = pause.getImage();
		Image stopImage = stop.getImage();

		Image newStart = startImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		Image newPause = pauseImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		Image newStop = stopImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);

		btnStart.setIcon(new ImageIcon(newStart));
		btnPause.setIcon(new ImageIcon(newPause));
		btnStop.setIcon(new ImageIcon(newStop));
		btnMore.setPreferredSize(new Dimension(70, 35));

	}

	private static void setProgressBarPercent() {
		double percent = LTPercentCalculator.getTodaysProgressPercent(time);
		progressBar.setString(String.format("%.2f%s", percent, "%"));
		progressBar.setValue((int) percent);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnMore) {
			if (mow == null || !mow.isVisible()) {
				mow = new MoreOptionWindow(progressBar);
			}
		}
		if (e.getSource() == btnStart) {
			if (isProjectSelected()) {
				if (thisDay == null) { // if the timer just paused then no need to create one more time,or it resets
										// the timer
					thisDay = new OneDay(LocalDate.now());
					session = new TimeSession(LocalDateTime.now(), new Project(txtSelectedProjectName.getText()));
				}
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				btnPause.setEnabled(true);
				btnMore.setEnabled(false);
				btnProjectMenu.setEnabled(false);
				timer.start();

			} else {
				JOptionPane.showMessageDialog(frame, "Please select project first in Project Manager");
			}
		}
		if (e.getSource() == btnStop) {
			timer.stop();
			session.setTimePassed(time);
			session.calculateSessionPercent();
			thisDay.setDailyProgressPercent(Double.valueOf(progressBar.getString().replace("%", "")));
			System.out.println(session.getTimeSessionPercent());

			model.setRowCount(0); // clear the table
			LTFileHandler.dataWriter(thisDay, session);
			LTTableHandler.fillMainTableWithData(model);
			time = LocalTime.MIN;
			timePassed.setText("00:00:00");
			thisDay = null;
			session = null;

			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			btnPause.setEnabled(false);
			btnMore.setEnabled(true);
			btnProjectMenu.setEnabled(true);

		}
		if (e.getSource() == btnPause) {
			btnPause.setEnabled(false);
			btnStart.setEnabled(true);
			timer.stop();
		}
		if (e.getSource() == btnProjectMenu) {
			if (pw == null || !pw.isVisible()) {
				pw = new ProjectWindow(txtSelectedProjectName);

			}
		}

	}

	public static void reloadFrameData() {

		LTTableHandler.fillMainTableWithData(model);
		progressBar.setValue(0);
		setProgressBarPercent();
		txtSelectedProjectName.setText(setLastProjectUsed());
		setLastProjectUsed();
	}

	private static String setLastProjectUsed() {

		Map<LocalDate, OneDay> redData = LTFileHandler.loadData();
		Collection<OneDay> values = redData.values();

		TimeSession lastUsedTimeSession = values.stream().map(
				od -> od.getTimeSessions().stream().filter(ts -> !ts.isArchived()).reduce((a, b) -> b).orElse(null))
				.reduce((a, b) -> b).orElse(null);
		if (lastUsedTimeSession != null) {
			return lastUsedTimeSession.getProject().getProjectName();
		} else {
			return "Not selected";
		}
	}

	private boolean isProjectSelected() {
		return !txtSelectedProjectName.getText().equals("Not selected");
	}

	public JFrame getMainFrame() {
		return frame;
	}
}
