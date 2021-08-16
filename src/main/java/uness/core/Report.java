package uness.core;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterName;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import unessdb.FIELD;

public class Report {
	
	public static void print(final InputStream template, Map<String, Object> params, JRDataSource data) throws JRException
	{
		JasperReport jrTemplate = JasperCompileManager.compileReport(template);
		JasperPrint report = JasperFillManager.fillReport(jrTemplate, params, data);
		
		/*
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		printRequestAttributeSet.add(MediaSizeName.ISO_A4);
		printRequestAttributeSet.add(new Copies(1));
		printRequestAttributeSet.add(new PageRanges(1,1));
		
		JRPrintServiceExporter exporter = new JRPrintServiceExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printService.getAttributes());
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
		*/
		JasperPrintManager.printReport(report, false);
		//exporter.exportReport();
	}
	
	public static void saveAsPdf(final InputStream template, Map<String, Object> params, JRDataSource data, String filename) throws JRException, IOException
	{
		JasperReport jasperReport = JasperCompileManager.compileReport(template);
		
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, data);

	    JasperExportManager.exportReportToPdfFile(jasperPrint, filename);
	    
	    
	   /*
       JRPdfExporter exporter = new JRPdfExporter();
       exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
       FileOutputStream out = new FileOutputStream("test.pdf");
       exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
       exporter.exportReport();
       out.close();
       */

			
	}
	
	
	public static void sendmail()
	{
		final String username = "unisysnice@gmail.com";
        final String password = "Nce2x4x6x";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("unisysnice@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("mathias.lauer@prestinfo.eu,mathias.lauer.fr@gmail.com")
            );
            message.setSubject("Uness: Ouverture d'une demande de RMA");
            message.setText("Dear Mail Crawler,"
                    + "\n\n Please do not spam my email!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
	}
	public static void main(String[] args)
	{
		sendmail();
	}
	
}
