package uness.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;
import unessdb.UDataException;

public class Import {
	
	private ArrayList<UData> lastImport;
	
	public Import()
	{
		
	}
	
	public final ArrayList<UData> importCsv(final String filepath) throws UDataException, IOException, IllegalArgumentException
	{
		File file = new File(filepath);
		CSVParser parser = CSVParser.parse(file, Charset.forName("UTF-8"), CSVFormat.DEFAULT.withFirstRecordAsHeader());
		lastImport = new ArrayList<UData>();
		
		for(CSVRecord rec : parser) {
			UData tupple = new UData();
			for(String header : parser.getHeaderMap().keySet())
			{
				FIELD f = FIELD.getFieldByAccessName(TABLE.DEVICE, header);
				if(f == null) throw new IOException("Incorrect field name: " + header);
				
				tupple.add(f, rec.get(header));
			}
			lastImport.add(tupple);
		}
		
		return lastImport;
	}

}
