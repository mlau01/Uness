package unessdb;

import java.util.ArrayList;

import unessdb.usql.USQL.DB;
import unessdb.usql.USQLManager;

public class Install {
	
	private final USQLManager man;
	
	public Install(String dbpath) throws DBException
	{
		man = new USQLManager();
		man.connect(DB.access, dbpath, "", "unisys", "");
		
		makeGroups();
		makeEnterprise();
		makeModels();
		makeLocationsT1();
		makeLocationsT2();
		
	}
	
	private void makeLocationsT2() throws DBException
	{
		ArrayList<String[]> dataArray = new ArrayList<String[]>();
		
		dataArray.add(new String[] {"T2", "T2-Spare", ""});
		
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A01"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A02"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A03"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A04"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A05"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A06"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A07"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A08"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A09"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A10"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A11"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A12"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A13"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A14"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A15"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A16"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ-A17"});
		
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B03"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B04"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B05"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B06"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B07"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B08"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B09"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B10"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B11"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B12"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B13"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ-B14"});
		
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-BLS02"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-BLS01"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C01"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C02"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C03"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C04"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C05"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C06"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C07"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C08"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C09"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C10"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C11"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C12"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C13"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C14"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C15"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C16"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C17"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C18"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C19"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ-C20"});

		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-BLS03"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D00"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D01"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D02"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D03"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D04"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D05"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D06"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D07"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D08"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D09"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D10"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D11"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D12"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D13"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D14"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D15"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D16"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D17"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D18"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D19"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ-D20"});
		
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A01"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A01B"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A02"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A02B"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A03"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A03B"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A04"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A04B"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A05"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A06"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A07"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A08"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A09"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A09B"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A10"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A11"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A12"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A13"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A14"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A14B"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A15"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A16"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A17"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A17B"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A18"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A18B"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A19"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A19B"});
		
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A98"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT-A99"});
		
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B20"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B21"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B22"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B23"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B24"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B24B"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B25"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B25B"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B26"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B26B"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B27"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B27B"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B30"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B31"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B32"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B32B"});
		
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B98"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT-B99"});
		
		dataArray.add(new String[] {"T2-PASS", "T2-PASS-40"});
		dataArray.add(new String[] {"T2-PASS", "T2-PASS-42"});
		dataArray.add(new String[] {"T2-PASS", "T2-PASS-44"});
		dataArray.add(new String[] {"T2-PASS", "T2-PASS-46"});
		dataArray.add(new String[] {"T2-PASS", "T2-PASS-48"});
		dataArray.add(new String[] {"T2-PASS", "T2-PASS-50"});
		dataArray.add(new String[] {"T2-PASS", "T2-PASS-50B"});
		dataArray.add(new String[] {"T2-PASS", "T2-PASS-52"});
		dataArray.add(new String[] {"T2-PASS", "T2-PASS-54"});
		dataArray.add(new String[] {"T2-PASS", "T2-PASS-54B"});
		
		for(String[] line : dataArray)
		{
			UData data = new UData(new FIELD[] {FIELD.LOCATION_group, FIELD.LOCATION_location}, line);
			man.insert(data.getFields(), false, data);
		}
	}
		
		
	
	private void makeLocationsT1() throws DBException
	{
		ArrayList<String[]> dataArray = new ArrayList<String[]>();
		
		
		dataArray.add(new String[] {"NCE", "Constructor", ""});
		dataArray.add(new String[] {"T1", "T1-Spare", ""});
		
		dataArray.add(new String[] {"T1-BQ-Zone A", "T1-BQ-A01"});
		dataArray.add(new String[] {"T1-BQ-Zone A", "T1-BQ-A02"});
		dataArray.add(new String[] {"T1-BQ-Zone A", "T1-BQ-A03"});
		dataArray.add(new String[] {"T1-BQ-Zone A", "T1-BQ-A04"});
		dataArray.add(new String[] {"T1-BQ-Zone A", "T1-BQ-A05"});
		dataArray.add(new String[] {"T1-BQ-Zone A", "T1-BQ-A06"});
		dataArray.add(new String[] {"T1-BQ-Zone A", "T1-BQ-A07"});
		dataArray.add(new String[] {"T1-BQ-Zone A", "T1-BQ-A08"});
		
		dataArray.add(new String[] {"T1-BQ-Zone A", "T1-SB-A01", ""});
		
		dataArray.add(new String[] {"T1-BQ-Zone B", "T1-BQ-B09"});
		dataArray.add(new String[] {"T1-BQ-Zone B", "T1-BQ-B10"});
		dataArray.add(new String[] {"T1-BQ-Zone B", "T1-BQ-B11"});
		dataArray.add(new String[] {"T1-BQ-Zone B", "T1-BQ-B12"});
		dataArray.add(new String[] {"T1-BQ-Zone B", "T1-BQ-B13"});
		dataArray.add(new String[] {"T1-BQ-Zone B", "T1-BQ-B14"});
		dataArray.add(new String[] {"T1-BQ-Zone B", "T1-BQ-B15"});
		dataArray.add(new String[] {"T1-BQ-Zone B", "T1-BQ-B16"});
		dataArray.add(new String[] {"T1-BQ-Zone B", "T1-BQ-B17"});
		
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C18"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C19"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C20"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C21"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C22"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C23"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C24"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C25"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C26"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C27"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C28"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ-C29"});
		
		dataArray.add(new String[] {"T1-BQ-Zone D", "T1-BQ-D30"});
		dataArray.add(new String[] {"T1-BQ-Zone D", "T1-BQ-D31"});
		dataArray.add(new String[] {"T1-BQ-Zone D", "T1-BQ-D32"});
		dataArray.add(new String[] {"T1-BQ-Zone D", "T1-BQ-D33"});
		dataArray.add(new String[] {"T1-BQ-Zone D", "T1-BQ-D34"});
		dataArray.add(new String[] {"T1-BQ-Zone D", "T1-BQ-D35"});
		dataArray.add(new String[] {"T1-BQ-Zone D", "T1-BQ-D36"});
		dataArray.add(new String[] {"T1-BQ-Zone D", "T1-BQ-D37"});
		dataArray.add(new String[] {"T1-BQ-Zone D", "T1-BQ-D38"});
		
		dataArray.add(new String[] {"T1-BQ-Zone E", "T1-BQ-E39"});
		dataArray.add(new String[] {"T1-BQ-Zone E", "T1-BQ-E40"});
		dataArray.add(new String[] {"T1-BQ-Zone E", "T1-BQ-E41"});
		dataArray.add(new String[] {"T1-BQ-Zone E", "T1-BQ-E42"});
		dataArray.add(new String[] {"T1-BQ-Zone E", "T1-BQ-E43"});
		
		dataArray.add(new String[] {"T1-BQ-Zone F", "T1-BQ-F44"});
		dataArray.add(new String[] {"T1-BQ-Zone F", "T1-BQ-F45"});
		dataArray.add(new String[] {"T1-BQ-Zone F", "T1-BQ-F46"});
		dataArray.add(new String[] {"T1-BQ-Zone F", "T1-BQ-F47"});
		dataArray.add(new String[] {"T1-BQ-Zone F", "T1-BQ-F48"});
		dataArray.add(new String[] {"T1-BQ-Zone F", "T1-BQ-F49"});
		dataArray.add(new String[] {"T1-BQ-Zone F", "T1-BQ-F50"});
		dataArray.add(new String[] {"T1-BQ-Zone F", "T1-BQ-F51"});
		
		dataArray.add(new String[] {"T1-BQ-Zone G", "T1-BQ-G52"});
		dataArray.add(new String[] {"T1-BQ-Zone G", "T1-BQ-G53"});
		dataArray.add(new String[] {"T1-BQ-Zone G", "T1-BQ-G54"});
		dataArray.add(new String[] {"T1-BQ-Zone G", "T1-BQ-G55"});
		dataArray.add(new String[] {"T1-BQ-Zone G", "T1-BQ-G56"});
		dataArray.add(new String[] {"T1-BQ-Zone G", "T1-BQ-G57"});
		
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A01"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A01B"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A02"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A02B"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A03"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A03B"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A04"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A04B"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A05"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A05B"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A06"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A07"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A08"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A09"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A10"});
		//Pas de porte A11
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A31"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A31B"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A32"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A32B"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A33"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A33B"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A34"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A34B"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A35"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A35B"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A36"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT-A36B"});
		
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B12"});
		//Pas de porte B13
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B14"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B14B"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B15"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B16"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B17"});
		
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B37"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B38"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B39"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B40"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B41"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B41B"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT-B42"});

		dataArray.add(new String[] {"T1-PASS", "T1-PASS-08"});
		dataArray.add(new String[] {"T1-PASS", "T1-PASS-08B"});
		dataArray.add(new String[] {"T1-PASS", "T1-PASS-10"});
		dataArray.add(new String[] {"T1-PASS", "T1-PASS-10B"});
		dataArray.add(new String[] {"T1-PASS", "T1-PASS-12"});
		dataArray.add(new String[] {"T1-PASS", "T1-PASS-14"});
		
		for(String[] line : dataArray)
		{
			UData data = new UData(new FIELD[] {FIELD.LOCATION_group, FIELD.LOCATION_location}, line);
			man.insert(data.getFields(), false, data);
		}
		
		
		
	}
	
	private void makeEnterprise() throws DBException
	{
		ArrayList<String[]> dataArray = new ArrayList<String[]>();
		
		dataArray.add(new String[] {"ACA"});
		dataArray.add(new String[] {"IER"});
		dataArray.add(new String[] {"Fujitsu"});
		dataArray.add(new String[] {"Eizo"});
		dataArray.add(new String[] {"Viewsonic"});
		dataArray.add(new String[] {"Epson"});
		dataArray.add(new String[] {"Oki"});
		dataArray.add(new String[] {"Honeywell"});
		
		
		for(String[] line : dataArray)
		{
			UData data = new UData(new FIELD[] {FIELD.ENTERPRISE_enterprise}, line);
			man.insert(data.getFields(), false, data);
		}
	}
	
	private void makeGroups() throws DBException{
		
		ArrayList<String[]> dataArray = new ArrayList<String[]>();
		/*
		 * dataArray.add(new String[] {"NCE", ""}); This has to be insert by hand using console or MS Access
		 * INSERT INTO Group VALUES (0,"NCE", null,"");
		 */
		
		dataArray.add(new String[] {"T1", "NCE"});
		dataArray.add(new String[] {"T1-BQ", "T1"});
		dataArray.add(new String[] {"T1-BQ-Zone A", "T1-BQ"});
		dataArray.add(new String[] {"T1-BQ-Zone B", "T1-BQ"});
		dataArray.add(new String[] {"T1-BQ-Zone C", "T1-BQ"});
		dataArray.add(new String[] {"T1-BQ-Zone D", "T1-BQ"});
		dataArray.add(new String[] {"T1-BQ-Zone E", "T1-BQ"});
		dataArray.add(new String[] {"T1-BQ-Zone F", "T1-BQ"});
		dataArray.add(new String[] {"T1-BQ-Zone G", "T1-BQ"});
		dataArray.add(new String[] {"T1-PT", "T1"});
		dataArray.add(new String[] {"T1-PT-Zone A", "T1-PT"});
		dataArray.add(new String[] {"T1-PT-Zone B", "T1-PT"});
		dataArray.add(new String[] {"T1-BO", "T1"});
		dataArray.add(new String[] {"T1-PASS", "T1"});
		dataArray.add(new String[] {"T1-BLS", "T1"});
		
		dataArray.add(new String[] {"T2", "NCE"});
		dataArray.add(new String[] {"T2-BO", "T2"});
		dataArray.add(new String[] {"T2-BQ", "T2"});
		dataArray.add(new String[] {"T2-BQ-Zone A", "T2-BQ"});
		dataArray.add(new String[] {"T2-BQ-Zone B", "T2-BQ"});
		dataArray.add(new String[] {"T2-BQ-Zone C", "T2-BQ"});
		dataArray.add(new String[] {"T2-BQ-Zone D", "T2-BQ"});
		dataArray.add(new String[] {"T2-PT", "T2"});
		dataArray.add(new String[] {"T2-PT-Zone A", "T2-PT"});
		dataArray.add(new String[] {"T2-PT-Zone B", "T2-PT"});
		dataArray.add(new String[] {"T2-PASS", "T2"});
		dataArray.add(new String[] {"T2-BLS", "T2"});
		
		for(String[] line : dataArray)
		{
			UData data = new UData(new FIELD[] {FIELD.GROUP_group, FIELD.GROUP_parentgroup}, line);
			man.insert(data.getFields(), false, data);
		}
	}
	
	private void makeModels() throws DBException
	{
		ArrayList<String[]> dataArray = new ArrayList<String[]>();
		
		dataArray.add(new String[] {"IER 400", "IER", "^40000.*"});
		dataArray.add(new String[] {"IER i420", "IER", "^42000.*"});
		dataArray.add(new String[] {"IER 506", "IER", "^50600.*"});
		dataArray.add(new String[] {"IER 600", "IER", "^I600.*"});
		dataArray.add(new String[] {"IER 602A", "IER", "^602.*"});
		dataArray.add(new String[] {"Fujitsu E720", "Fujitsu", "^YLQS.*"});
		dataArray.add(new String[] {"Fujitsu E700", "Fujitsu", "^YLCU.*"});
		dataArray.add(new String[] {"Ecran Fujitsu 19", "Fujitsu", "^YV6V.*"});
		dataArray.add(new String[] {"Ecran Viewsonic 15", "Viewsonic", "^Q790.*"});
		dataArray.add(new String[] {"Ecran Viewsonic 17", "Viewsonic", "^Q7S0.*"});
		dataArray.add(new String[] {"Ecran Eizo 17", "Eizo", ""});
		dataArray.add(new String[] {"Epson TM-L500A", "Epson", "^QJ3F.*"});
		dataArray.add(new String[] {"OKI 3320", "Oki", "^.*AK.*"});
		dataArray.add(new String[] {"OKI 520", "Oki", "^.*CK.*"});
		
		for(String[] line : dataArray)
		{
			UData data = new UData(new FIELD[] {FIELD.MODEL_model, FIELD.MODEL_constructor, FIELD.MODEL_serial_pattern}, line);
			man.insert(data.getFields(), false, data);
		}
	}
	

	
	public static void main(String[] args)
	{
		if(args.length < 1) {
			System.out.println("Database path is needed");
			return;
		}
		try {
			new Install(args[0]);
		} catch (DBException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
