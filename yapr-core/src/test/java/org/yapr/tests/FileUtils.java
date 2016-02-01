package org.yapr.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author http://www.rgagnon.com/javadetails/java-0064.html
 */
public class FileUtils {

	public static void copyFile(File in, File out) throws IOException {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(in);
			inChannel = inputStream.getChannel();
			outputStream = new FileOutputStream(out);
			outChannel = outputStream.getChannel();
			// magic number for Windows, 64Mb - 32Kb
			int maxCount = (64 * 1024 * 1024) - (32 * 1024);
			long size = inChannel.size();
			long position = 0;
			while (position < size) {
				position += inChannel.transferTo(position, maxCount, outChannel);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (inputStream != null)
				inputStream.close();
			if (inChannel != null)
				inChannel.close();
			if (outputStream != null)
				outputStream.close();
			if (outChannel != null)
				outChannel.close();
		}
	}
}
