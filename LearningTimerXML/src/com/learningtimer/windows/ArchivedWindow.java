package com.learningtimer.windows;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.learningtimer.LTFileHandler;
import com.learningtimer.LTTableHandler;
import com.learningtimer.dataStoreObjects.Project;

public class ArchivedWindow extends JFrame implements ActionListener, FocusListener {

	private JTable table;
	private JButton btnUnarchive = new JButton("Unarchive");
	private DefaultTableModel model;
	private DefaultTableModel unarchivedProjectModel;

	/**
	 * Launch the application.
	 */

	public ArchivedWindow(DefaultTableModel unarchivedModel) {
		this.unarchivedProjectModel = unarchivedModel;
		initialize();
		this.setVisible(true);
	}


	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		table = new JTable(model) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model = new DefaultTableModel(null, new String[] { "Project name", "Session time" });
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		table.setModel(model);
		table.addFocusListener(this);
		btnUnarchive.setEnabled(false);
		btnUnarchive.addActionListener(this);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);

		this.getContentPane()
				.add(scrollPane, BorderLayout.CENTER);
		this.getContentPane()
				.add(btnUnarchive, BorderLayout.SOUTH);
		this.pack();
		LTTableHandler.fillTheProjectTable(model, true);
		MainWindow.reloadFrameData();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			String projectName = (String) model.getValueAt(selectedRow, 0);
			LTFileHandler.setArchived(new Project(projectName), false);
			LTTableHandler.fillTheProjectTable(model, true);
			LTTableHandler.fillTheProjectTable(unarchivedProjectModel, false);

		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (table.getSelectedRow() != -1) {
			btnUnarchive.setEnabled(true);
		}
		table.setFocusable(false); // Renew the focus
		table.setFocusable(true);
	}

	@Override
	public void focusLost(FocusEvent e) {
	}

}
