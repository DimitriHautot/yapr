package org.yapr.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.yapr.domain.Configuration;
import org.yapr.domain.FileRenamer;
import org.yapr.filter.DirectoryFilter;
import org.yapr.filter.MovieFilter;
import org.yapr.filter.PictureFilter;
import org.yapr.filter.RawPictureFilter;

/**
 * Cette classe est le coeur du moteur de renommage d'assets.
 * Il est habituellement appelé via une interface graphique, mais peut également être invoqué en ligne de commande.
 * 
 * Created on Jul 27, 2005
 * @author Dimitri
 */
public class YaprCore {

	public YaprCore() {
	}

	/**
	 * 
	 * @param configuration
	 * @param files
	 * @return
	 */
	public int processAll(Configuration configuration, File[] files) {
		int processed = 0;
		List<File> standaloneFiles = new ArrayList<File>();
		List<File> pairedFiles = new ArrayList<File>();
		List<File> subFolders = new ArrayList<File>();
		
		if (files != null) {
			for (File file : files) {
					 if (MovieFilter.acceptFile(file)) pairedFiles.add(file);
				else if (RawPictureFilter.acceptFile(file)) pairedFiles.add(file);
				else if (PictureFilter.acceptFile(file)) standaloneFiles.add(file);
				else if (DirectoryFilter.acceptFile(file)) subFolders.add(file);
			}
			processed = process(configuration, pairedFiles.toArray(new File[pairedFiles.size()]), standaloneFiles.toArray(new File[standaloneFiles.size()]), subFolders.toArray(new File[subFolders.size()]));
		}
		return processed;
	}

	/**
	 * Process several files.
	 * 
	 * @param configuration
	 * @param pairedFiles Files whose renaming depend on the presence of a thumbnail file (e.g. THM + JPG, MP4 + JPG or CR2 + JPG)
	 * @param standaloneFiles Files embedding some date/time to be used
	 * @param subFolders The sub-folders of the current folder (level 1 only)
	 * @return The amount of files renamed
	 */
	public int process(Configuration configuration, File[] pairedFiles, File[] standaloneFiles, File[] subFolders) {
		int processed = 0;

		// (1) Process the paired files in this directory
		if (pairedFiles != null) {
			processed += process(configuration, pairedFiles);
		}

		// (2) Process the stand-alone files in this directory
		if (standaloneFiles != null) {
			processed += process(configuration, standaloneFiles);
		}

		// (3) Process the sub-folders of this directory
		if (subFolders != null) {
			for (int loop = 0; loop < subFolders.length; loop++) {
				processed += process(configuration, subFolders[loop]);
			}
		}

		return processed;
	}

	/**
	 * Used by the GUI and indirectly by the CLI.
	 * 
	 * @param files
	 * @return the number of files effectively renamed
	 */
	public int process(Configuration configuration, File[] files) {
		int processed = 0;

		if (configuration == null || configuration.getRenamers() == null || configuration.getRenamers().size() == 0) {
			// FIXME Find a better exception type and a more explanatory message
			throw new RuntimeException("Configuration invalide");
		}

		if ((null == files) || (files.length == 0)) {
			return 0;
		}

		for (int filesLoop = 0; filesLoop < files.length; filesLoop++) {
			for (int renamersLoop = 0; renamersLoop < configuration.getRenamers().size(); renamersLoop++) {
				FileRenamer renamer = configuration.getRenamers().get(renamersLoop);
				File renamedAsset = null;
				if (renamer.accept(files[filesLoop])) {
					renamedAsset = renamer.rename(files[filesLoop], configuration);
					if (renamedAsset != null) {
						processed++;
						break; // Go on with next file
					}
				}
				
			}
		}

		return processed;
	}

	public int process(Configuration configuration) {
		return process(configuration, configuration.getSourceFolder());
	}

	/**
	 * Used by the CLI.
	 * 
	 * @param configuration
	 * @param baseFolder
	 * @return
	 */
	public int process(Configuration configuration, File baseFolder) {
		int processed = 0;

		if (null == baseFolder) {
			// FIXME Find a better exception type
			throw new RuntimeException("No base folder to operate from");
		}

		// (1) Process the movies in this directory
		File[] movies = baseFolder.listFiles(new MovieFilter());
		int loop = 0;
		if (movies != null) {
			processed += process(configuration, movies);
		}

		// (2) Process the pictures in this directory
		File[] pictures = baseFolder.listFiles(new PictureFilter());
		if (pictures != null) {
			processed += process(configuration, pictures);
		}

		// (3) Process the subfolders of this directory
		if (configuration.isProcessSubFolders()) {
			File[] subFolders = baseFolder.listFiles(new DirectoryFilter());
			if (subFolders != null) {
				for (loop = 0; loop < subFolders.length; loop++) {
					processed += process(configuration, subFolders[loop]);
				}
			}
		}

		return processed;
	}
}
