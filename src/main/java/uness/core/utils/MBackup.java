package uness.core.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class MBackup {
	
	public static void backup(final String dstFolder, final String filePath)
	{
		String backupFolder = null;
		try {
			backupFolder = getFolderPath(dstFolder);
		} catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		String currentDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(System.currentTimeMillis());
		Path file = null;
		try {
			file = getFilePath(filePath);
			Path target = Paths.get(backupFolder + "/" + currentDate + "_backup_" + file.getFileName());
			Files.copy(file, target, LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e)
		{
			e.printStackTrace();
			return;
		}	
	}
	
	private static final Path getFilePath(final String pathString) throws IOException
	{
		Path path = Paths.get(pathString);
		if(Files.exists(path, LinkOption.NOFOLLOW_LINKS))
		{
			//if(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
			if( ! Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS) && Files.isReadable(path))
				return path;
			else
				throw new IOException("File: " + pathString + " is not valid");
		}
		else
			throw new IOException("File: " + pathString + " not exists");
	}
	
	private static final String getFolderPath(final String pathString) throws IOException
	{	
		Path path = Paths.get(pathString);
		if(Files.exists(path, LinkOption.NOFOLLOW_LINKS))
		{
			if(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
				return path.toString();
			else
				throw new IOException("Folder: " + pathString + " is not valid");
		}
		else
			throw new IOException("Folder: " + pathString + " not exists");
	}

}
