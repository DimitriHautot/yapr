package org.yapr.ui;

import java.awt.EventQueue;

import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;

public class YaprUIMac extends YaprUI implements ApplicationListener {

	private static final long serialVersionUID = 8637745919652925124L;

	private Application application;
	private AboutAction aboutAction;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					YaprUIMac frame = new YaprUIMac();
					frame.buildUI();
					frame.setApplication(new DefaultApplication());
					frame.postInitialize();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public YaprUIMac() {
		super();
	}

	private void setApplication(Application application) {
		this.application = application;
	}

	/**
	 * 
	 */
	@Override
	protected void postInitialize() {
		application.addApplicationListener(this);
		application.setEnabledAboutMenu(true);
		application.setEnabledPreferencesMenu(false);

		aboutAction = new AboutAction("About");
		mnHelp.add(aboutAction);
	}


	/* ---------- ApplicationListener ---------- */

	public void handleAbout(ApplicationEvent event) {
		aboutAction.actionPerformed(null);
        event.setHandled(true);
	}

	public void handleOpenApplication(ApplicationEvent event) {
		// TODO Auto-generated method stub
	}

	public void handleOpenFile(ApplicationEvent event) {
		// TODO Auto-generated method stub
	}

	public void handlePreferences(ApplicationEvent event) {
		// TODO Auto-generated method stub
	}

	public void handlePrintFile(ApplicationEvent event) {
		// TODO Auto-generated method stub
	}

	public void handleQuit(ApplicationEvent event) {
		System.exit(0);
	}

	public void handleReOpenApplication(ApplicationEvent event) {
		setVisible(true);
	}

}
