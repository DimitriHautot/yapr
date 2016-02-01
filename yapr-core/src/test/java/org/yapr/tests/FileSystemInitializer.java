package org.yapr.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yapr.domain.FileRenamer;
import org.yapr.domain.LyingRenamer;

/**
 * @author Dimitri
 *
 */
public class FileSystemInitializer {

	public static String PHOTO_0_1 = "file-0_1.jpg";
	public static String PHOTO_0_2 = "file-0_2.jpg";
	public static String MOVIE_0_1 = "file-0_1.avi";
	public static String PHOTO_0_3 = "file-0_3.jpg";
	public static String MOVIE_0_3 = "file-0_3.mpg";
	public static String PHOTO_0_4 = "file-0_4.thm";
	public static String MOVIE_0_4 = "file-0_4.mpeg";
	public static String FOLDER_1 = "folder1";
	public static String FOLDER_2 = "folder2";
	public static String PHOTO_2_1 = "file-2_1.jpg";
	public static String MOVIE_2_1 = "file-2_1.mov";
	public static String MOVIE_2_2 = "file-2_2.mov";
	public static String PHOTO_2_2 = "file-2_2.jpg";
	public static String RAW_2_2   = "file-2_2.crw";
	public static String RAW_2_3   = "file-2_3.crw";

	private static Logger logger = LoggerFactory.getLogger(FileSystemInitializer.class);
	private static AtomicInteger invocationCount = new AtomicInteger(0);
	private File baseFolder = null;
	private File[] allFiles = null;
	private String _baseFolderName;
	private Map<String, File> mapFiles;

	public FileSystemInitializer(String baseFolderName) {
		mapFiles = new HashMap<String, File>();
		if (baseFolderName != null) {
			_baseFolderName = baseFolderName;
		} else {
			_baseFolderName = "FileSystemInitializer";
		}
		buildTemporaryFilesAndFolders();
	}

	public void finalize() {
		recursivelyDeleteFolderAndContent(baseFolder);
		baseFolder = null;
		allFiles = null;
	}

	public File[] getAllFiles() {
		return allFiles;
	}

	public File getBaseFolder() {
		return baseFolder;
	}
	
	public File get(String filename) {
		return mapFiles.get(filename);
	}

	/**
	 * <p>Build a hierarchy of files and folders suitable for tests in this class.<br>
	 * The hierarchy looks like this:
	 * <ul>
	 *   <li>YaprCoreTest0/</li>
	 *   <li>YaprCoreTest0/photo0_1.jpg</li>
	 *   <li>YaprCoreTest0/photo0_2.jpg</li>
	 *   <li>YaprCoreTest0/movie0_1.avi</li>
	 *   <li>YaprCoreTest0/photo0_3.jpg</li>
	 *   <li>YaprCoreTest0/photo0_3.mpg</li>
	 *   <li>YaprCoreTest0/photo0_4.thm</li>
	 *   <li>YaprCoreTest0/photo0_4.mpeg</li>
	 *   <li>YaprCoreTest0/folder1/</li>
	 *   <li>YaprCoreTest0/folder2/</li>
	 *   <li>YaprCoreTest0/folder2/photo2_1.jpg</li>
	 *   <li>YaprCoreTest0/folder2/movie2_1.mov</li>
	 *   <li>YaprCoreTest0/folder2/movie2_2.mov</li>
	 *   <li>YaprCoreTest0/folder2/file-2_2.jpg</li>
	 *   <li>YaprCoreTest0/folder2/file-2_2.crw</li>
	 *   <li>YaprCoreTest0/folder2/file-2_3.crw</li>
	 * </ul>
	 * with {@code YaprCoreTest0} itself being created under the temporary folder of the machine, obtained via {@code System.getProperty("java.io.tmpdir")}.<br>
	 * {@code 0} at the end of the base folder name is an auto-incremental value.
	 * All files and folder are created with the flag set to delete them on JVM exit. ({@link File#deleteOnExit()})<br>
	 * So there are 3 photos and 2 movies to process, and an empty folder. The {@link FileRenamer} instance used in the tests is a {@link LyingRenamer}. It accepts any file, without actually renaming them.
	 * That's OK here because we don't test the renaming process, we test the various invocations of the Yapr core engine.</p>
	 * 
	 * <p>The method initialize these 3 instance variables:
	 * <ul>
	 *   <li>{@code private File baseFolder}</li>
	 *   <li>{@code private File[] allFiles}</li>
	 *   <li>{@code private File[] allFolders}</li>
	 * </ul></p>
	 */
	private void buildTemporaryFilesAndFolders() {
		synchronized (invocationCount) {
			baseFolder = new File(System.getProperty("java.io.tmpdir"), _baseFolderName + invocationCount);
//			try {
//				baseFolder = new File(ClassLoader.getSystemResource(".").toURI());
//			} catch (URISyntaxException e) {
//				throw new RuntimeException("Unable to create base temporary directory");
//			}
			// In case a previous JVM has abruptly been interrupted, the base folder has not been emptied/deleted; let's do it here
			if (baseFolder.exists()) {
				recursivelyDeleteFolderAndContent(baseFolder);
			}
			invocationCount.incrementAndGet();
		}
		baseFolder.deleteOnExit();
		boolean result = baseFolder.mkdir();
		if (! result) {
			throw new RuntimeException("Unable to create base temporary directory");
		}

		File photo0_1 = touchNewFile(baseFolder, PHOTO_0_1);	mapFiles.put(PHOTO_0_1, photo0_1);
		File photo0_2 = touchNewFile(baseFolder, PHOTO_0_2);	mapFiles.put(PHOTO_0_2, photo0_2);
		File movie0_1 = touchNewFile(baseFolder, MOVIE_0_1);	mapFiles.put(MOVIE_0_1, movie0_1);
		File photo0_3 = touchNewFile(baseFolder, PHOTO_0_3);	mapFiles.put(PHOTO_0_3, photo0_3);
		File movie0_3 = touchNewFile(baseFolder, MOVIE_0_3);	mapFiles.put(MOVIE_0_3, movie0_3);
		File photo0_4 = touchNewFile(baseFolder, PHOTO_0_4);	mapFiles.put(PHOTO_0_4, photo0_4);
		File movie0_4 = touchNewFile(baseFolder, MOVIE_0_4);	mapFiles.put(MOVIE_0_4, movie0_4);

		File folder1 = new File(baseFolder, FOLDER_1);
		folder1.deleteOnExit();
		result = folder1.mkdir();
		if (! result) {
			throw new RuntimeException("Unable to create temporary 'folder1' directory");
		}

		File folder2 = new File(baseFolder, FOLDER_2);
		folder2.deleteOnExit();
		result = folder2.mkdir();
		if (! result) {
			throw new RuntimeException("Unable to create temporary 'folder2' directory");
		}

		File photo2_1 = touchNewFile(folder2, PHOTO_2_1);	mapFiles.put(PHOTO_2_1, photo2_1);

		File movie2_1 = touchNewFile(folder2, MOVIE_2_1);	mapFiles.put(MOVIE_2_1, movie2_1);

		File photo2_2 = touchNewFile(folder2, PHOTO_2_2);	mapFiles.put(PHOTO_2_2, photo2_2);
		File raw2_2 = touchNewFile(folder2, RAW_2_2);	mapFiles.put(RAW_2_2, raw2_2);
		File raw2_3 = touchNewFile(folder2, RAW_2_3);	mapFiles.put(RAW_2_3, raw2_3);

		allFiles = new File[] {photo0_1, photo0_2, movie0_1, photo0_3, movie0_3, photo0_4, movie0_4, photo2_1, movie2_1, photo2_2, raw2_2, raw2_3};
	}

	/**
	 * 
	 * @param folder
	 * @param name
	 * @return
	 */
	public File touchNewFile(File folder, String name) {
		File file = new File(folder, name);
		file.deleteOnExit();
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException("Unable to create new, empty file '" + name + "' in folder '" + folder);
		}
		return file;
	}

	public File createTempFile(String prefix, String suffix, File folder) throws IOException {
		File file = null;
		String thePrefix = prefix != null ? prefix : "yapr-tests-tempfile-";
		String theSuffix = suffix != null ? suffix : ".test";
		if (folder != null) {
			file = File.createTempFile(thePrefix, theSuffix, folder);
		} else {
			file = File.createTempFile(thePrefix, theSuffix);
		}
		file.deleteOnExit();
		return file;
	}

	private static void recursivelyDeleteFolderAndContent(File baseFolder) {
		File[] files = baseFolder.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					recursivelyDeleteFolderAndContent(files[i]);
				} else {
					if (! files[i].delete()) {
						logger.error("Unable to delete {} !", files[i]);
					}
				}
			}
		}
		baseFolder.delete();
	}
}
