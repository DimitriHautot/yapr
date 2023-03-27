package org.yapr.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.yapr.domain.Configuration;
import org.yapr.domain.FileRenamer;
import org.yapr.renamer.strategies.*;

/**
 * @author Dimitri
 *
 */
public class ConfigurationFactory {

	public ConfigurationFactory() {
		super();
	}

	public Configuration newInstance(int hoursOffset, String targetFolder) {
		List<FileRenamer> renamers = buildFileRenamerInstances();
		Configuration configuration = new Configuration();
		configuration.setHoursOffset(hoursOffset);
		configuration.setTargetFolder(targetFolder);
		configuration.setRenamers(renamers);

		return configuration;
	}

	public Configuration newInstance(SimpleDateFormat dateFormat, int hoursOffset, String targetFolder) {
		Configuration configuration = newInstance(hoursOffset, targetFolder);
		configuration.setDateFormat(dateFormat);
		
		return configuration;
	}

	/**
	 * 
	 * @return
	 */
	List<FileRenamer> buildFileRenamerInstances() {
		List<FileRenamer> instances = new ArrayList<FileRenamer>();
		instances.add(new ExifThumbnailRawPictureRenamer());
		instances.add(new ExifThumbnailMovieRenamer());
		instances.add(new ExifPictureRenamer());
		instances.add(new ExifMovieRenamer());
		instances.add(new LastModificationFileRenamer());

		return instances;
	}
}
