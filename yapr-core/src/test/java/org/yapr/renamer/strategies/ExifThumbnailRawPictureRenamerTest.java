package org.yapr.renamer.strategies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yapr.domain.Configuration;
import org.yapr.tests.FileSystemInitializer;
import org.yapr.tests.FileUtils;

/**
 * @author Dimitri
 *
 */
public class ExifThumbnailRawPictureRenamerTest {

	private FileSystemInitializer _fileSystem;
	private static int duplicateCounter = 0;
	File thumbnailForRawPicture, rawPictureWithThumbnail, rawPictureWithoutThumbnail;

	public ExifThumbnailRawPictureRenamerTest() {
		_fileSystem = new FileSystemInitializer(getClass().getSimpleName());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		thumbnailForRawPicture = duplicate(new File(ClassLoader.getSystemResource("./movie-with-thumbnail.jpg").toURI()), "Test-raw-with-thumbnail.jpg");
		rawPictureWithThumbnail = duplicate(new File(ClassLoader.getSystemResource("./movie-with-thumbnail.mpg").toURI()), "Test-raw-with-thumbnail.crw");
		rawPictureWithoutThumbnail = duplicate(new File(ClassLoader.getSystemResource("./movie-without-thumbnail.mpg").toURI()), "Test-raw-without-thumbnail.crw");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (rawPictureWithoutThumbnail != null) rawPictureWithoutThumbnail.delete();
		if (rawPictureWithThumbnail != null) rawPictureWithThumbnail.delete();
		if (thumbnailForRawPicture != null) thumbnailForRawPicture.delete();
	}

	/**
	 * Test method for {@link org.yapr.renamer.strategies.ExifThumbnailRawPictureRenamer#accept(java.io.File)}.
	 */
	@Test
	public void test_accept() {
		ExifThumbnailRawPictureRenamer instance = new ExifThumbnailRawPictureRenamer();
		boolean result = instance.accept(null);
		assertFalse(result);
		
		result = instance.accept(new File("ahiohioshcioxhccxwkcwc"));
		assertFalse(result);
		
		result = instance.accept(_fileSystem.get(FileSystemInitializer.FOLDER_1));
		assertFalse(result);

		result = instance.accept(_fileSystem.get(FileSystemInitializer.PHOTO_2_1));
		assertFalse(result);
		
		result = instance.accept(_fileSystem.get(FileSystemInitializer.RAW_2_2));
		assertTrue(result);
		
		result = instance.accept(_fileSystem.get(FileSystemInitializer.RAW_2_3));
		assertFalse(result);
	}

	/**
	 * Test method for {@link org.yapr.renamer.strategies.ExifThumbnailRawPictureRenamer#rename(java.io.File, org.yapr.domain.Configuration)}.
	 */
	@Test
	public void test_rename() {
		ExifThumbnailRawPictureRenamer instance = new ExifThumbnailRawPictureRenamer();
		Configuration configuration = new Configuration();
		File renamedFile = instance.rename(rawPictureWithThumbnail, configuration);
		assertNotNull(renamedFile);

		renamedFile = instance.rename(rawPictureWithoutThumbnail, configuration);
		assertNull(renamedFile);
	}

	private File duplicate(File source, String newName) throws IOException {
		File copy;
		if (newName == null) {
			copy = new File(source.getParentFile(), "ExifThumbnailRawPictureRenamer-" + duplicateCounter++ + ".jpg");
		} else {
			copy = new File(source.getParentFile(), newName);
		}
		copy.deleteOnExit();
		FileUtils.copyFile(source, copy);
		return copy;
	}

}
