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
public class ExifThumbnailMovieRenamerTest {

	private FileSystemInitializer _fileSystem;
	private static int duplicateCounter = 0;
	File thumbnailForMovie, movieWithThumbnail, movieWithoutThumbnail;

	public ExifThumbnailMovieRenamerTest() {
		_fileSystem = new FileSystemInitializer(getClass().getSimpleName());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		thumbnailForMovie = duplicate(new File(ClassLoader.getSystemResource("./movie-with-thumbnail.jpg").toURI()), "Test-movie-with-thumbnail.jpg");
		movieWithThumbnail = duplicate(new File(ClassLoader.getSystemResource("./movie-with-thumbnail.mpg").toURI()), "Test-movie-with-thumbnail.mpg");
		movieWithoutThumbnail = duplicate(new File(ClassLoader.getSystemResource("./movie-without-thumbnail.mpg").toURI()), "Test-movie-without-thumbnail.mpg");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (movieWithoutThumbnail != null) movieWithoutThumbnail.delete();
		if (movieWithThumbnail != null) movieWithThumbnail.delete();
		if (thumbnailForMovie != null) thumbnailForMovie.delete();
	}

	/**
	 * Test method for {@link org.yapr.renamer.strategies.ExifThumbnailMovieRenamer#accept(java.io.File)}.
	 */
	@Test
	public void test_accept() {
		ExifThumbnailMovieRenamer instance = new ExifThumbnailMovieRenamer();
		boolean result = instance.accept(null);
		assertFalse(result);
		
		result = instance.accept(new File("ahiohioshcioxhckxwcxwkcxwkcwc"));
		assertFalse(result);
		
		result = instance.accept(_fileSystem.get(FileSystemInitializer.FOLDER_1));
		assertFalse(result);

		result = instance.accept(_fileSystem.get(FileSystemInitializer.PHOTO_2_1));
		assertFalse(result);
		
		result = instance.accept(_fileSystem.get(FileSystemInitializer.MOVIE_2_1));
		assertTrue(result);
		
		result = instance.accept(_fileSystem.get(FileSystemInitializer.MOVIE_2_2));
		assertFalse(result);
	}

	/**
	 * Test method for {@link org.yapr.renamer.strategies.ExifThumbnailMovieRenamer#rename(java.io.File, org.yapr.domain.Configuration)}.
	 */
	@Test
	public void test_rename() {
		ExifThumbnailMovieRenamer instance = new ExifThumbnailMovieRenamer();
		Configuration configuration = new Configuration();
		File renamedFile = instance.rename(movieWithThumbnail, configuration);
		assertNotNull(renamedFile);

		renamedFile = instance.rename(movieWithoutThumbnail, configuration);
		assertNull(renamedFile);
	}

	private File duplicate(File source, String newName) throws IOException {
		File copy;
		if (newName == null) {
			copy = new File(source.getParentFile(), "ExifThumbnailMovieRenamerTest-" + duplicateCounter++ + ".jpg");
		} else {
			copy = new File(source.getParentFile(), newName);
		}
		copy.deleteOnExit();
		FileUtils.copyFile(source, copy);
		return copy;
	}

}
