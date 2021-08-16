package uness.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import uness.Uness;
import uness.Uness.OPTION;
import unessdb.FIELD;

public class Option {

	private Properties p;
	private short verboseLevel = 2;
	
	public Option() throws OptionException
	{
		if(verboseLevel >= 2) System.out.println(this.getClass().getName() + " -> INIT");
		init();
		checkIntegrity();
	}
	
	private void init() throws OptionException
	{
		try {
			if(verboseLevel >= 2) System.out.println(this.getClass().getName() + " -> Trying to get conf file: " + Uness.CONF_DIRECTORY + "/" + Uness.CONF_NAME);
			p = getPropertiesFile(Uness.CONF_DIRECTORY + "/" + Uness.CONF_NAME);
		} catch (IOException e)
		{
			if(verboseLevel >= 2) System.out.println(this.getClass().getName() + " -> Conf file not found, loading default conf...");
			p = getDefaultProperties();
			try {
				writePropertiesFile(p);
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new OptionException(e1.getMessage());
			}
		}
	}
	
	private void checkIntegrity()
	{
		for(OPTION o : OPTION.values())
		{
			if(getProperty(o) == null) {
				System.out.println(this.getClass().getName() + " -> checkIntegrity() -> no value found for key: " + o + ", default value loaded");
				p.setProperty(o.toString(), o.getDefault());
			}
		}
	}
	
	public void setProperty(final OPTION o, final String value)
	{
		p.setProperty(o.toString(), value);
	}
	
	
	public final String getProperty(final OPTION o)
	{
		
		String property = p.getProperty(o.toString());

		return property;
	}
	
	private final Properties getDefaultProperties()
	{
		final Properties dp = new Properties();
		for(OPTION o : OPTION.values())
			dp.setProperty(o.toString(), o.getDefault());
		
		return dp;
	}
	
	public void columnToShowSwitch(final OPTION o, final FIELD field)
	{
		String value = getProperty(o);
		ArrayList<FIELD> fields = FIELD.toFieldArray(value);
		if(fields.contains(field)) fields.remove(field);
		else fields.add(field);
		String newProperty = FIELD.toCommaSeparatedString(fields);
		setProperty(o, newProperty);
	}
	public void changeColumnOrder(final OPTION o, String newOrder)
	{
		String currentOrder = getProperty(o);
		if( ! newOrder.equals(currentOrder)){
			setProperty(o, newOrder);
		}
	}
	
	public void write() throws OptionException
	{
		System.out.println(this.getClass().getName() + " -> write() : Trigger");
		try {
			writePropertiesFile(p);
		} catch (IOException e) {
			e.printStackTrace();
			throw new OptionException(e.getMessage());
		}
	}
	
	/**
	 * Write a properties file to the local user data
	 * The project name would be use that folder name
	 * @param p Properties object
	 * @param projectName Name of the folder that contains properties file
	 * @param configName Name of the file that contains properties
	 * @return String represents the config file or null if something goes wrong
	 * @throws IOException When a error was encountered while trying to write in the target folder
	 * @throws MOPException When an error was encountered while getting user configuration path
	 */
	public final String writePropertiesFile(final Properties p) throws IOException
	{	
		final String filepath = getConfigDirectoryPath()  + "/" + Uness.CONF_NAME;
		final FileOutputStream fos = new FileOutputStream(filepath);
		p.store(fos, "Properties for: " + Uness.APP_NAME);
	
		return filepath;
	}
	
	/**
	 * Load a properties file found in the local user data
	 * @param projectName Name of the folder that contains properties file
	 * @param configName Name of the file that contains properties
	 * @return Properties object
	 * @throws IOException When an error was encountered while trying to read the target file
	 * @throws MOPException When an error was encountered while getting user configuration path;
	 */
	public final Properties getPropertiesFile(final String filepath) throws IOException
	{
		final FileInputStream fis = new FileInputStream(filepath);
		final Properties p = new Properties();
		p.load(fis);
	
		
		return p;
	}
	
	private final String getConfigDirectoryPath() throws FileNotFoundException, IOException
	{		
		final Path path = Paths.get(Uness.CONF_DIRECTORY);
		if(Files.exists(path, LinkOption.NOFOLLOW_LINKS))
		{
			if(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
			{
				return path.toString();	
			}
			else 
				throw new FileNotFoundException("The targeted file is not valid");
		}
		else
		{
			if(verboseLevel >= 2) System.out.println(this.getClass().getName() + " -> Directory not exists, trying to create it...");
			return createConfigFilepath();
		}
	}
	
	private String createConfigFilepath() throws IOException
	{	
		final Path path = Paths.get(Uness.CONF_DIRECTORY);
		final Path createdDir = Files.createDirectories(path);
		
		return createdDir.toString();
	}
}
