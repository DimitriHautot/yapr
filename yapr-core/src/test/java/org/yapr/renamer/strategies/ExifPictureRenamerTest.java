package org.yapr.renamer.strategies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yapr.domain.Configuration;
import org.yapr.tests.FileUtils;

/**
 * @author Dimitri
 *
 */
public class ExifPictureRenamerTest {

	private ExifPictureRenamer instance = new ExifPictureRenamer();
	private File workingCopy;
	private File workingCopyNoEXIF;
	private static int duplicateCounter = 0;

	@Before
	public void setUp() throws IOException, URISyntaxException {
		workingCopy = duplicate(new File(ClassLoader.getSystemResource("./photo_with_EXIF.jpg").toURI()));
		workingCopyNoEXIF = duplicate(new File(ClassLoader.getSystemResource("./photo_without_EXIF.jpg").toURI()));
	}

	@After
	public void tearDown() {
		workingCopyNoEXIF.delete();
		workingCopy.delete();
	}

	/**
	 * Test method for {@link org.yapr.renamer.strategies.ExifPictureRenamer#rename(java.io.File, org.yapr.domain.Configuration)}.
	 * @throws URISyntaxException 
	 * @throws IOException If any file manipulation throws an exception
	 */
	@Test
	public void rename() throws URISyntaxException, IOException {
		File renamedWorkingCopy = instance.rename(workingCopy, new Configuration());
		assertNotNull(renamedWorkingCopy);
		File renamedPhoto = new File(ClassLoader.getSystemResource("./2009-05-09-14h37m50.jpg").toURI());
		renamedPhoto.deleteOnExit();
		assertTrue(renamedPhoto.exists());
		renamedPhoto.delete();
	}

	/**
	 * Test method for {@link org.yapr.renamer.strategies.ExifPictureRenamer#rename(java.io.File, org.yapr.domain.Configuration)}.
	 * @throws URISyntaxException 
	 * @throws IOException If any file manipulation throws an exception
	 */
	@Test
	public void rename__no_EXIF() throws URISyntaxException, IOException {
		File renamedFile = instance.rename(workingCopyNoEXIF, new Configuration());
		assertNull(renamedFile);
	}

	/**
	 * Test method for {@link org.yapr.renamer.strategies.ExifPictureRenamer#accept(java.io.File)}.
	 * Verify that a PNG image cannot be renamed using this class.
	 * @throws URISyntaxException 
	 */
	@Test
	public void accept__PNG() throws URISyntaxException {
		File photo = new File(ClassLoader.getSystemResource("photo.png").toURI());
		photo.deleteOnExit();
		assertTrue(photo.exists());
		assertFalse(instance.accept(photo));
	}

	/**
	 * Test method for {@link org.yapr.renamer.strategies.ExifPictureRenamer#accept(java.io.File)}.
	 * Verify that a JPG image can be renamed using this class.
	 * @throws URISyntaxException 
	 */
	@Test
	public void accept__JPG() throws URISyntaxException {
		assertTrue(workingCopy.exists());
		assertTrue(instance.accept(workingCopy));
	}

	private File duplicate(File source) throws IOException {
		File copy = new File(source.getParentFile(), "PictureRenamerTest-" + duplicateCounter++ + ".jpg");
		copy.deleteOnExit();
		FileUtils.copyFile(source, copy);
		return copy;
	}
}
