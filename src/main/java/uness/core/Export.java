package uness.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import uness.Uness;
import unessdb.FIELD;
import unessdb.UData;

public class Export {

	public Export()
	{
		
	}
	
	public enum EXPORT
	{
		CSV_HEADER_DISPLAYNAME,
		CSV_HEADER_ACCESSNAME;
	}
	
	public final void exportCsv(final String filepath, final Collection<UData> list, EXPORT option) throws IOException
	{
		File save = new File(filepath);
		BufferedWriter writer = Files.newBufferedWriter(save.toPath());
		
		//Header line
		FIELD[] fields = list.iterator().next().getFields();
		String header = "";
		for(int i = 0; i < fields.length; i++)
		{
			if( ! header.isEmpty()) header+= ",";
			if(option == EXPORT.CSV_HEADER_ACCESSNAME)
				header += fields[i].getAccessName();
			else if(option == EXPORT.CSV_HEADER_DISPLAYNAME)
				header += fields[i].getDisplayName();
		}
		String line = String.format(header + "%n");
		writer.write(line);
		
		for(UData data : list)
		{
			String record = data.getCommaSeparatedValues();
			line = String.format(record + "%n");
			writer.write(line);
		}
		
		writer.close();
	}
	
	public final void exportHtml(final String filepath, final Collection<UData> list) throws IOException
	{
		File save = new File(filepath);
		BufferedWriter writer = Files.newBufferedWriter(save.toPath());
		
		String htmlHeader = "<!DOCTYPE html>";
		htmlHeader += "<html>";
		htmlHeader += "<head>";
		htmlHeader += "<meta charset=\"UTF-8\">";
		htmlHeader += "<title>Listing</title>";
		htmlHeader += "</head>";
		htmlHeader += "<style>";
		htmlHeader += "table, th, td {";
		htmlHeader += "margin: auto;";
		htmlHeader += "border: 1px solid black;";
		htmlHeader += "border-collapse: collapse;";
		htmlHeader += "text-align: center;";
		htmlHeader += "}";
		htmlHeader += "</style>";
		htmlHeader += "<body>";
		htmlHeader += "<table>";
		writer.write(htmlHeader);
		
		//Header line
		FIELD[] fields = list.iterator().next().getFields();
		String header = "<tr>";
		for(FIELD f : fields)
		{
			header += "<th>";
			header += f.getDisplayName();
			header += "</th>";
		}
		header += "</tr>";
		writer.write(header);
		
		int loopCount = 0;
		for(UData data : list)
		{
			String line = "";
			if(loopCount % 2 == 0)
				line = "<tr style=\"background-color: #d5d5d5;\">";
			else line = "<tr>";
			for(FIELD f : fields)
			{
				line += "<td>";
				line += String.valueOf(data.getValue(f));
				line += "</td>";
			}
			line += "</tr>";
			writer.write(line);
			loopCount++;
		}
		String htmlFooter = "</table></body></html>";
		writer.write(htmlFooter);
		
		writer.close();
		
	}
	
	public final void exportUnescan(final String filepath,
			final ArrayList<UData> models,
			final ArrayList<UData> enterprises,
			final ArrayList<UData> groups,
			final ArrayList<UData> locations) throws IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
		Document xml = builder.newDocument();
		Element nunescan = xml.createElement("unescan");
		nunescan.setAttribute("generated", Uness.currentTime);

		Element nmodels = xml.createElement("models");
		nunescan.appendChild(nmodels);
		for(UData model : models)
		{
			Element item = xml.createElement("model");
			item.setAttribute("value", String.valueOf(model.getValue(FIELD.MODEL_model)));
			item.setAttribute("pserial", String.valueOf(model.getValue(FIELD.MODEL_serial_pattern)));
			nmodels.appendChild(item);
		}
		
		Element unknown = xml.createElement("model");
		unknown.setAttribute("value", "Inconnu");
		unknown.setAttribute("pserial","");
		nmodels.appendChild(unknown);
		
		Element nenterprise = xml.createElement("enterprises");
		nunescan.appendChild(nenterprise);
		for(UData enterprise : enterprises)
		{
			Element item = xml.createElement("enterprise");
			item.setAttribute("value", String.valueOf(enterprise.getValue(FIELD.ENTERPRISE_enterprise)));
			nenterprise.appendChild(item);
		}
		
		Element nlocation = xml.createElement("locations");
		nunescan.appendChild(nlocation);
		Element root = null;
		for(final UData g : groups)
		{
			if( (String.valueOf(g.getValue(FIELD.GROUP_parentgroup)).equals("")))
			{	
				
				root = xml.createElement("location");
				nlocation.appendChild(root);
				root.setAttribute("value", (String)g.getValue(FIELD.GROUP_group));
				break;
			}
		}
		setChild(xml,root, groups, locations);
		
		try {
			Transformer transform = TransformerFactory.newInstance().newTransformer();
			transform.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult XML = new StreamResult(filepath);
			transform.transform(new DOMSource(nunescan), XML);
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}
	
	private void setChild(final Document xml, Element parent, final ArrayList<UData> groups, final ArrayList<UData> locations)
	{
		for(UData group : groups)
		{
			String parentGroup = String.valueOf(group.getValue(FIELD.GROUP_parentgroup));
			if( parentGroup.equals(parent.getAttribute("value")) ) {
				Element child = xml.createElement("group");
				child.setAttribute("value", (String)group.getValue(FIELD.GROUP_group));
				parent.appendChild(child);
				
				for(UData location : locations)
				{
					if(((String)location.getValue(FIELD.LOCATION_group)).equals(child.getAttribute("value"))){
						Element elem = xml.createElement("location");
						elem.setAttribute("value", (String)location.getValue(FIELD.LOCATION_location));
						child.appendChild(elem);
					}
				}
				setChild(xml,child, groups, locations);
			}
		}
	}
	
}
