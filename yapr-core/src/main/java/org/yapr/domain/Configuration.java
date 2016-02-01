package org.yapr.domain;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dimitri
 *
 */
public class Configuration {

	private SimpleDateFormat dateFormat;
	private int hoursOffset = 0;
	private File sourceFolder = null;
	private String targetFolder = null;
	private boolean processSubFolders = false;
	private List<FileRenamer> renamers = new ArrayList<FileRenamer>();

	public Configuration() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH'h'mm'm'ss");
	}

	public Configuration(SimpleDateFormat dateFormat, int hoursOffset, String targetFolder) {
		this.dateFormat = dateFormat;
		this.hoursOffset = hoursOffset;
		this.targetFolder = targetFolder;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int getHoursOffset() {
		return hoursOffset;
	}
	public void setHoursOffset(int hoursOffset) {
		this.hoursOffset = hoursOffset;
	}

	public String getTargetFolder() {
		return targetFolder;
	}
	public void setTargetFolder(String targetFolder) {
		this.targetFolder = targetFolder;
	}

	public File getSourceFolder() {
		return sourceFolder;
	}
	public void setSourceFolder(File sourceFolder) {
		this.sourceFolder = sourceFolder;
	}

	public boolean isProcessSubFolders() {
		return processSubFolders;
	}
	public void setProcessSubFolders(boolean processSubFolders) {
		this.processSubFolders = processSubFolders;
	}

	public List<FileRenamer> getRenamers() {
		return renamers;
	}
	public void setRenamers(List<FileRenamer> renamers) {
		this.renamers = renamers;
	}

}
