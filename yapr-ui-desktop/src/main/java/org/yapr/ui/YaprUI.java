package org.yapr.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yapr.core.ConfigurationFactory;
import org.yapr.core.YaprCore;
import org.yapr.domain.Configuration;
import org.yapr.ui.filter.AllSupportedFilesFilterUI;

public class YaprUI extends JFrame implements ActionListener, ListSelectionListener, ChangeListener {

	private static final long serialVersionUID = -9149009737719622380L;

	private Logger logger = LoggerFactory.getLogger(YaprUI.class);

	private YaprCore _core;
	private ConfigurationFactory _configurationFactory;
	private Configuration _configuration;

	protected JButton btnRename;
	protected JButton btnRemoveSelectedFiles;
	protected JMenuBar menuBar;
	protected JMenu mnHelp;
	protected JMenuItem mntmRemoveSelectedFiles;
	protected JMenuItem mntmRename;
	protected JPanel contentPane;
	protected AccessibleContext accContext;
	protected JFileChooser addFilesDialog;
//	protected JFileChooser destinationFolderDialog;
	protected FileDialog destinationFolderDialog;
	protected JList filesJList;
	protected JSpinner spinner;
	protected JProgressBar progressBar;
	protected JTextField txtTargetFolder;
	protected JRadioButton rdbtnSameFolder;
	protected JRadioButton rdbtnOtherFolder;

	protected static String ADD_FILES = "AddFiles";
	protected static String REMOVE_FILES = "RemoveFiles";
	protected static String PROCEED = "Proceed";
	protected static String SAME_TARGET_FOLDER = "SameTargetFolder";
	protected static String OTHER_TARGET_FOLDER = "OtherTargetFolder";
	protected static String SELECT_TARGET_FOLDER = "SelectTargetFolder";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					YaprUI frame = new YaprUI();
					frame.postInitialize();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 
	 */
	protected void postInitialize() {
		menuBar.add(mnHelp);
	}

	/**
	 * Constructor.
	 */
	public YaprUI() {
		buildUI();
		_configurationFactory = new ConfigurationFactory();
		_configuration = _configurationFactory.newInstance((Integer)spinner.getValue(), null);
		_core = new YaprCore();
	}

	protected void buildUI() {
		setTitle("Yapr - Yet another picture renamer (tool)");

		// Provide Quit functionality
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setBounds(200, 200, 800, 300);
		setResizable(false);

		// Menu bar
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// File menu
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		// "Add Files" menu item
		JMenuItem mntmAddFiles = new JMenuItem("Add files");
		mntmAddFiles.setMnemonic(KeyEvent.VK_A);
		mntmAddFiles.setActionCommand(ADD_FILES);
		mntmAddFiles.addActionListener(this);
		mnFile.add(mntmAddFiles);

		// "Remove Files" menu item
		mntmRemoveSelectedFiles = new JMenuItem("Remove Selected Files");
		mntmRemoveSelectedFiles.setMnemonic(KeyEvent.VK_R);
		mntmRemoveSelectedFiles.setActionCommand(REMOVE_FILES);
		mntmRemoveSelectedFiles.addActionListener(this);
		mntmRemoveSelectedFiles.setEnabled(false);
		mnFile.add(mntmRemoveSelectedFiles);

		// "Rename !" menu item
		mntmRename = new JMenuItem("Rename !");
		mntmRename.setMnemonic(KeyEvent.VK_P);
		mntmRename.setActionCommand(PROCEED);
		mntmRename.addActionListener(this);
		mntmRename.setEnabled(false);
		mnFile.add(mntmRename);

		// "About" menu
		mnHelp = new JMenu("Help");

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		filesJList = new JList(new DefaultListModel());
		filesJList.setVisibleRowCount(10);
		filesJList.addListSelectionListener(this);
		contentPane.add(filesJList, BorderLayout.CENTER);

		JPanel hPanel1 = new JPanel();
		hPanel1.setLayout(new BoxLayout(hPanel1, BoxLayout.X_AXIS));
		JPanel hPanel2 = new JPanel();
		hPanel2.setLayout(new BoxLayout(hPanel2, BoxLayout.X_AXIS));
		JPanel vPanel = new JPanel();
		vPanel.setLayout(new BoxLayout(vPanel, BoxLayout.Y_AXIS));
		vPanel.add(hPanel1);
		vPanel.add(hPanel2);
		contentPane.add(vPanel, BorderLayout.NORTH);

		// "Add Files" button
		JButton btnAddFiles = new JButton("Add Files");
		btnAddFiles.setMnemonic(KeyEvent.VK_A);
		btnAddFiles.setActionCommand(ADD_FILES);
		btnAddFiles.addActionListener(this);
		btnAddFiles.setEnabled(true);
		accContext = btnAddFiles.getAccessibleContext();
		accContext.setAccessibleDescription("Press this button to add new files to the list");
		hPanel1.add(btnAddFiles);

		// "Remove Files" button
		btnRemoveSelectedFiles = new JButton("Remove Selected Files");
		btnRemoveSelectedFiles.setMnemonic(KeyEvent.VK_R);
		btnRemoveSelectedFiles.setActionCommand(REMOVE_FILES);
		btnRemoveSelectedFiles.addActionListener(this);
		btnRemoveSelectedFiles.setEnabled(false);
		accContext = btnRemoveSelectedFiles.getAccessibleContext();
		accContext.setAccessibleDescription("Press this button to remove the selected files from the list");
		hPanel1.add(btnRemoveSelectedFiles);

		// "Rename !" button
		btnRename = new JButton("Rename !");
		btnRename.setMnemonic(KeyEvent.VK_P);
		btnRename.setActionCommand(PROCEED);
		btnRename.addActionListener(this);
		btnRename.setEnabled(false);
		accContext = btnRename.getAccessibleContext();
		accContext.setAccessibleDescription("Press this button to rename the files in the list");
		hPanel1.add(btnRename);
		
		JLabel lblHoursOffset = new JLabel("Hours Offset");
		hPanel1.add(lblHoursOffset);
		
		spinner = new JSpinner(new SpinnerNumberModel());
		spinner.addChangeListener(this);
		hPanel1.add(spinner);

		JLabel lblOutput = new JLabel();
		lblOutput.setText("Destination:");
		hPanel2.add(lblOutput);

		ButtonGroup destinationFolderGroup = new ButtonGroup();
		rdbtnSameFolder = new JRadioButton("Overwrite");
		rdbtnSameFolder.setSelected(Boolean.TRUE);
		rdbtnSameFolder.setActionCommand(SAME_TARGET_FOLDER);
		rdbtnSameFolder.addActionListener(this);
		destinationFolderGroup.add(rdbtnSameFolder);
		hPanel2.add(rdbtnSameFolder);
		
		rdbtnOtherFolder = new JRadioButton("Folder:");
		rdbtnOtherFolder.setActionCommand(OTHER_TARGET_FOLDER);
		rdbtnOtherFolder.addActionListener(this);
		destinationFolderGroup.add(rdbtnOtherFolder);
		hPanel2.add(rdbtnOtherFolder);
		
		txtTargetFolder = new JTextField();
		txtTargetFolder.setColumns(10);
		txtTargetFolder.setEnabled(false);
		hPanel2.add(txtTargetFolder);

		JButton btnSelectTargetFolder = new JButton("...");
		btnSelectTargetFolder.addActionListener(this);
		btnSelectTargetFolder.setActionCommand(SELECT_TARGET_FOLDER);
		hPanel2.add(btnSelectTargetFolder);

		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setVisible(false);
		contentPane.add(progressBar, BorderLayout.SOUTH);

		// "Add Files" dialog
		addFilesDialog = new JFileChooser();
		addFilesDialog.setMultiSelectionEnabled(true);
		addFilesDialog.addChoosableFileFilter(new AllSupportedFilesFilterUI());
		addFilesDialog.setDialogTitle("Select picture and movie files to rename");
		addFilesDialog.setDialogType(JFileChooser.OPEN_DIALOG);
		addFilesDialog.setVisible(false);

		// "destination" folder dialog
		destinationFolderDialog = new FileDialog(this, "Select destination folder", FileDialog.LOAD);
		destinationFolderDialog.setVisible(false);
	}

	/* ---------- ActionListener ---------- */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		DefaultListModel model = (DefaultListModel) filesJList.getModel();
		if (e.getActionCommand().equals(ADD_FILES)) {
			addFilesDialog.setVisible(true);
			if (JFileChooser.APPROVE_OPTION == addFilesDialog.showOpenDialog(null)) {
				File[] newFiles = addFilesDialog.getSelectedFiles();
				if (newFiles.length > 0) {
					for (int loop = 0; loop < newFiles.length; loop++) {
						if (!model.contains(newFiles[loop])) {
							model.addElement(newFiles[loop]);
						}
					}
				}
			}
			addFilesDialog.setVisible(false);
		} else if (e.getActionCommand().equals(REMOVE_FILES)) {
			Object[] selectedFiles = filesJList.getSelectedValues();
			if ((selectedFiles != null) && (selectedFiles.length > 0)) {
				for (int loop = 0; loop < selectedFiles.length; loop++) {
					logger.info(loop + ": removing entry " + selectedFiles[loop]);
					model.removeElement(selectedFiles[loop]);
				}
			}
		} else if (e.getActionCommand().equals(SELECT_TARGET_FOLDER)) {
			System.setProperty("apple.awt.fileDialogForDirectories", "true");
			destinationFolderDialog.setVisible(true);
			String folder = destinationFolderDialog.getDirectory();
			if (folder != null) {
				txtTargetFolder.setText(folder + destinationFolderDialog.getFile());
				rdbtnSameFolder.setSelected(Boolean.FALSE);
				rdbtnOtherFolder.setSelected(Boolean.TRUE);
				txtTargetFolder.setEnabled(Boolean.TRUE);
			}
			destinationFolderDialog.setVisible(false);
			System.setProperty("apple.awt.fileDialogForDirectories", "false");
			
//			destinationFolderDialog.setVisible(true);
//			if (JFileChooser.APPROVE_OPTION == destinationFolderDialog.showOpenDialog(null)) {
//				txtTargetFolder.setText(destinationFolderDialog.getSelectedFile().getPath());
//				rdbtnSameFolder.setSelected(Boolean.FALSE);
//				rdbtnOtherFolder.setSelected(Boolean.TRUE);
//				txtTargetFolder.setEnabled(Boolean.TRUE);
//			}
		} else if (e.getActionCommand().equals(PROCEED)) {
			File[] files = new File[model.getSize()];
			for (int loop = 0; loop < model.getSize(); loop++) {
				files[loop] = (File) model.get(loop);
			}

			if (txtTargetFolder.isEnabled()) {
				StringBuilder folder = new StringBuilder(txtTargetFolder.getText());
				if (folder.lastIndexOf(System.getProperty("file.separator")) != folder.length() - 1) {
					folder.append(System.getProperty("file.separator"));
				}
				_configuration.setTargetFolder(folder.toString());
			} else {
				_configuration.setTargetFolder(null);
			}
			
			// Rename files
			progressBar.setVisible(true);
//			pack();
			_core.processAll(_configuration, files);
			progressBar.setVisible(false);

			// Update the JList
			for (int loop = 0; loop < files.length; loop++) {
				if (!files[loop].exists())
					model.removeElement(files[loop]);
			}
			filesJList.requestFocusInWindow();
		} else if (e.getActionCommand().equals(SAME_TARGET_FOLDER)) {
			txtTargetFolder.setEnabled(false);
		} else if (e.getActionCommand().equals(OTHER_TARGET_FOLDER)) {
			txtTargetFolder.setEnabled(true);
			txtTargetFolder.requestFocusInWindow();
		}

		model.trimToSize();
		btnRename.setEnabled(model.getSize() > 0);
		mntmRename.setEnabled(btnRename.isEnabled());

		//pack();
	}

	/* ---------- ListSelectionListener ---------- */

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent event) {
		btnRemoveSelectedFiles.setEnabled(null != ((JList) event.getSource()).getSelectedValue());
		mntmRemoveSelectedFiles.setEnabled(btnRemoveSelectedFiles.isEnabled());
	}

	/* ---------- ChangeListener ---------- */

	@Override
	public void stateChanged(ChangeEvent event) {
		_configuration.setHoursOffset((Integer)spinner.getValue());
	}


    class AboutAction extends AbstractAction {
		private static final long serialVersionUID = 8507046367689496498L;

		public AboutAction(String title) {
            super(title);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JOptionPane.showMessageDialog(YaprUI.this,
            		"Yapr - Yet another picture renamer (tool) - by Dimitri Hautot\n" +
            		"Rename your pictures & movies shot with your digital camera using their creation date.\n" +
            		"EXIF data extraction made possible thanks to Metadat Extraction Tool (http://meta-extractor.sourceforge.net/)");
        }
    }

}
