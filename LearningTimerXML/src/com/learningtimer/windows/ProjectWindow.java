package com.learningtimer.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import com.learningtimer.LTTableHandler;
import com.learningtimer.projectWindowButtons.AddButton;
import com.learningtimer.projectWindowButtons.ArchiveButton;
import com.learningtimer.projectWindowButtons.ArchivedWindowButton;
import com.learningtimer.projectWindowButtons.ContinueButton;
import com.learningtimer.projectWindowButtons.DeleteButton;

@SuppressWarnings("serial")
public class ProjectWindow extends JFrame implements FocusListener {

	private JTable table;
	private JTable table_2;

	private JButton btnArchiveProject;
	private JButton btnAddProject;
	private JButton btnDeleteProject;

	private DefaultTableModel projectModel;
	private DefaultTableModel projectSessionsModel;
	private JTextPane txtSelectedProject; 
	private JTextPane txtSelectedProjectName;

	private JTextPane mainWindowsSelectedProject;
	private JButton btnContinue;
	private JButton btnArchivedProjects;


	public ProjectWindow(JTextPane mainWindowsSelectedProject) {
		this.mainWindowsSelectedProject = mainWindowsSelectedProject;
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		frame = new JFrame();
		this.setLocation(500, 150);
		this.setFocusable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		projectModel = new DefaultTableModel(new String[] { "Project name", "Time Summary" }, 0);
		projectSessionsModel = new DefaultTableModel(new String[] { "Date", "Time" }, 0);

		JPanel selectedProjectPanel = new JPanel();
		JPanel sidePanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(192, 191, 188));

		JScrollPane scrollPane = new JScrollPane();
		JScrollPane scrollPane_2 = new JScrollPane();

		table = new JTable(projectModel) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.addFocusListener(this);
		table_2 = new JTable(projectSessionsModel);
		table_2.setPreferredScrollableViewportSize(new Dimension(200, 200));

		table.setRowHeight(25);
		table.setFont(new Font("Basic", Font.BOLD, 15));

		scrollPane.setViewportView(table);
		scrollPane_2.setViewportView(table_2);

		sidePanel.setLayout(new BorderLayout(0, 0));
		sidePanel.add(scrollPane_2, BorderLayout.CENTER);

		setProjectWindotTextComponentsAtInitialing();

		// Buttons
		btnAddProject = new AddButton("Add project", projectModel);
		btnDeleteProject = new DeleteButton("Delete project", projectModel, table, txtSelectedProjectName,
				mainWindowsSelectedProject);
		btnContinue = new ContinueButton("Continue", txtSelectedProjectName, mainWindowsSelectedProject, this);
		btnArchiveProject = new ArchiveButton("Archive project", txtSelectedProjectName, projectModel);
		btnArchivedProjects = new ArchivedWindowButton("Archived projects", projectModel);

		buttonPanel.add(btnDeleteProject);
		buttonPanel.add(btnAddProject);
		buttonPanel.add(btnContinue);
		buttonPanel.add(btnArchiveProject);
		buttonPanel.add(btnArchivedProjects);

		selectedProjectPanel.setBackground(new Color(143, 240, 164)); // THE COLOR IS CREATED 3X
		selectedProjectPanel.add(txtSelectedProject);
		selectedProjectPanel.add(txtSelectedProjectName);

		this.getContentPane()
				.add(sidePanel, BorderLayout.EAST);
		this.getContentPane()
				.add(scrollPane, BorderLayout.CENTER);
		this.getContentPane()
				.add(buttonPanel, BorderLayout.SOUTH);
		this.getContentPane()
				.add(selectedProjectPanel, BorderLayout.NORTH);
		LTTableHandler.fillTheProjectTable(projectModel, false);
		this.pack();
	}

	private void setProjectWindotTextComponentsAtInitialing() {
		Color selectedProjectCompColor = new Color(143, 240, 164);
		Font projectCompFont = new Font("Nimbus Roman No9 L", Font.BOLD, 14);

		txtSelectedProject = new JTextPane();
		txtSelectedProjectName = new JTextPane();

		txtSelectedProject.setBackground(selectedProjectCompColor);
		txtSelectedProject.setFont(projectCompFont);
		txtSelectedProject.setText("Selected Project:");
		txtSelectedProject.setEditable(false);

		txtSelectedProjectName.setBackground(selectedProjectCompColor);
		txtSelectedProjectName.setFont(projectCompFont);
		txtSelectedProjectName.setText(mainWindowsSelectedProject.getText());
		txtSelectedProjectName.setEditable(false);

	}

	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() == table) {

			int selectedProjectsRow = table.getSelectedRow();
			String focusedProject = (String) projectModel.getValueAt(selectedProjectsRow, 0);
			txtSelectedProjectName.setText(focusedProject);
			table.setFocusable(false); // renew the focus
			table.setFocusable(true);

			LTTableHandler.fillSideSessionTable(projectSessionsModel, txtSelectedProjectName);
			btnDeleteProject.setEnabled(true);
			btnContinue.setEnabled(true);
			btnArchiveProject.setEnabled(true);
		}

	}

	@Override
	public void focusLost(FocusEvent e) {
	}

}
