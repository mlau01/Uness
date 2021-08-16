package unessdb;

import java.util.ArrayList;

import unessdb.usql.USQL.FIELD_TYPE;

// ---- For Access DB: wess-0.1.3 ----

//Fields lists
public enum FIELD {
	
	ENTERPRISE_id(TABLE.ENTERPRISE, 1, "id", "Id", FIELD_TYPE.DIGIT, null),
	ENTERPRISE_enterprise(TABLE.ENTERPRISE, 2, "enterprise", "Entreprise", FIELD_TYPE.TEXT, null),
	
	MODEL_id(TABLE.MODEL, 1, "id", "Id", FIELD_TYPE.DIGIT, null), 
	MODEL_model(TABLE.MODEL, 2, "model", "Nom", FIELD_TYPE.TEXT, null),
	MODEL_constructor(TABLE.MODEL, 3, "constructor", "Fabriquant", FIELD_TYPE.TEXT, FIELD.ENTERPRISE_enterprise),
	MODEL_serial_pattern(TABLE.MODEL, 4, "serial_pattern", "Serial pattern", FIELD_TYPE.TEXT, null),
	MODEL_comment(TABLE.MODEL, 5, "comment", "Commentaire", FIELD_TYPE.TEXT, null),
	
	GROUP_id(TABLE.GROUP, 1, "id", "Id", FIELD_TYPE.DIGIT, null),
	GROUP_group(TABLE.GROUP, 2, "group", "Nom", FIELD_TYPE.TEXT, null),
	GROUP_parentgroup(TABLE.GROUP, 3, "parentgroup", "Groupe Parent", FIELD_TYPE.DIGIT, FIELD.GROUP_group),
	GROUP_comment(TABLE.GROUP, 4, "comment", "Commentaire", FIELD_TYPE.TEXT, null),
	
	LOCATION_id(TABLE.LOCATION, 1, "id", "Id", FIELD_TYPE.DIGIT, null),
	LOCATION_location(TABLE.LOCATION, 2, "location", "Localisation", FIELD_TYPE.TEXT, null),
	LOCATION_group(TABLE.LOCATION, 3, "group", "Groupe", FIELD_TYPE.DIGIT, FIELD.GROUP_group),
	LOCATION_comment(TABLE.LOCATION, 4, "comment", "Commentaire", FIELD_TYPE.TEXT, null), 
	
	DEVICE_id(TABLE.DEVICE, 1, "id", "Id", FIELD_TYPE.DIGIT, null),
	DEVICE_model(TABLE.DEVICE, 2, "model", "Modèle", FIELD_TYPE.DIGIT, FIELD.MODEL_model),
	DEVICE_serial(TABLE.DEVICE, 3, "serial", "Serial", FIELD_TYPE.TEXT, null), 
	DEVICE_location(TABLE.DEVICE, 4, "location", "Localisation", FIELD_TYPE.DIGIT, FIELD.LOCATION_location),
	DEVICE_idclient(TABLE.DEVICE, 5, "idClient", "Id Client", FIELD_TYPE.TEXT, null),
	DEVICE_owner(TABLE.DEVICE, 6, "owner", "Propriétaire", FIELD_TYPE.DIGIT, FIELD.ENTERPRISE_enterprise), 
	DEVICE_purchasedate(TABLE.DEVICE, 7, "purchaseDate", "Date d'achat", FIELD_TYPE.DATE, null),
	DEVICE_warrantyexpire(TABLE.DEVICE, 8, "warrantyExpire", "Date fin de garantie", FIELD_TYPE.DATE, null),
	DEVICE_networkName(TABLE.DEVICE, 9, "networkName", "Nom réseau", FIELD_TYPE.TEXT, null),
	DEVICE_comment(TABLE.DEVICE, 10, "comment", "Commentaire", FIELD_TYPE.TEXT, null),
	

	USER_id(TABLE.USER, 1, "id", "Id", FIELD_TYPE.DIGIT, null), 
	//USER_login(TABLE.USER, 2, "login", "Signature", FIELD_TYPE.TEXT, null), 
	USER_fullname(TABLE.USER, 2, "fullname", "Nom complet", FIELD_TYPE.TEXT, null),
	//USER_password(TABLE.USER, 4, "password", "MDP", FIELD_TYPE.TEXT, null),
	//USER_level(TABLE.USER, 5, "level", "Niveau d'accés", FIELD_TYPE.DIGIT, null),
	//USER_w_welcomepage(TABLE.USER, 5, "w_welcome_page", "Page d'acceuil", FIELD_TYPE.TEXT, null),

	LOG_id(TABLE.LOG, 1, "id", "Id", FIELD_TYPE.DIGIT, null), 
	LOG_device(TABLE.LOG, 2, "device", "Appareil", FIELD_TYPE.DIGIT, FIELD.DEVICE_id),
	LOG_author(TABLE.LOG, 3, "author", "Auteur", FIELD_TYPE.TEXT, null),
	LOG_comment(TABLE.LOG, 4, "comment", "Commentaire", FIELD_TYPE.TEXT, null),
	LOG_datetime(TABLE.LOG, 5, "datetime", "Date", FIELD_TYPE.DATE, null), 

	RMA_id(TABLE.RMA, 1, "id", "Id", FIELD_TYPE.DIGIT, null),
	RMA_device(TABLE.RMA, 2, "device", "Appareil", FIELD_TYPE.DIGIT, null),
	RMA_devicelastlocation(TABLE.RMA, 3, "devicelastlocation", "Dernière localisation connue", FIELD_TYPE.TEXT, null),
	RMA_rmanumber(TABLE.RMA, 4, "rmanumber", "Numéro RMA", FIELD_TYPE.TEXT, null), 
	RMA_sender(TABLE.RMA,5, "sender", "Envoyé par", FIELD_TYPE.TEXT, null),
	RMA_datesent(TABLE.RMA, 6,"datesent", "Date d'envoi", FIELD_TYPE.DATE, null),
	RMA_travelvoucher(TABLE.RMA,7,"travelvoucher", "Numéro de reçu transporteur", FIELD_TYPE.TEXT,null),
	RMA_catcher(TABLE.RMA,8,"catcher", "Reçu par", FIELD_TYPE.TEXT, null), 
	RMA_datecatched(TABLE.RMA,9,"datecatched", "Date de réception", FIELD_TYPE.DATE,null), 
	RMA_comment(TABLE.RMA, 10,"comment", "Commentaire", FIELD_TYPE.TEXT, null), 
	RMA_status(TABLE.RMA,11,"status", "Statut", FIELD_TYPE.TEXT, null);

	private TABLE table;
	private String accessName;
	private String displayName;
	private FIELD_TYPE accessType;
	private FIELD accessLink;
	private int fieldNumber;
	private final static int verboseLevel = 0;

	FIELD(TABLE p_table, int p_fieldNumber, String p_accessName, String p_displayName, FIELD_TYPE p_accessType, FIELD p_accessLink) {
		table = p_table;
		accessName = p_accessName;
		displayName = p_displayName;
		accessType = p_accessType;
		accessLink = p_accessLink;
		fieldNumber = p_fieldNumber;
	}

	public final TABLE getTable() {
		return table;
	}
	
	public final boolean isIn(FIELD[] array)
	{
		for(FIELD f : array)
			if(this == f) return true;
		
		return false;
	}

	public boolean isIn(ArrayList<FIELD> arrayList) {
		for(FIELD f : arrayList)
			if(this == f) return true;
		
		return false;
	}
	
	
	/**
	 * Search a field in an array of fields
	 * @param array
	 * @return Position of the field in the array or -1 if the found
	 */
	public final int getPosition(FIELD[] array)
	{
		for(int i = 0; i < array.length; i++)
			if(this == array[i]) return i;
		
		return -1;
	}

	public final String getAccessName() {
		return this.accessName;
	}
	
	public final String getDisplayName()
	{
		return this.displayName;
	}

	public final boolean isLinked() {
		if (this.accessLink != null)
			return true;

		return false;
	}

	public final FIELD getLink() {
		FIELD finalField = null;
		if(this.isLinked())
		{
			
			FIELD link = accessLink;
			while(link != null)
			{
				finalField = link;
				link = link.getLink();
			}
		}
		return finalField;
	}

	public final FIELD_TYPE getAccessType() {
		return this.accessType;
	}

	public final int getFieldNumber() {
		return fieldNumber;
	}

	/**
	 * 
	 * @param sqlname
	 * @return Return a field identified by TABLE and displayName or null otherwise
	 */
	public static FIELD getFieldByDisplayName(TABLE table, String displayName)
	{
		for(FIELD f : FIELD.values()){
			if(f.getDisplayName().equals(displayName) && f.getTable() == table)
				return f;
		}
		
		return null;
	}
	public static FIELD getFieldByDisplayName(FIELD[] fields, String displayName)
	{
		for(FIELD f : fields)
		{
			if(f.getDisplayName().equals(displayName)) return f;
		}
		
		return null;
	}
	
	public static FIELD getFieldByAccessName(TABLE table, String accessName)
	{
		for(FIELD f : FIELD.values()){
			if(f.getAccessName().equals(accessName) && f.getTable() == table)
				return f;
		}
		
		return null;
	}
	
	public static final FIELD getIdField(TABLE table) {
		for (FIELD f : FIELD.values()) {
			if ((f.getTable() == table) && (f.getFieldNumber() == 1)) {
				return f;
			}
		}

		return null;
	}
	
	/**
	 * Construct a FIELD array with the String given in arg
	 * @param String has to be the FIELD name separated by a comma when there is multiple
	 * @return FIELD[]
	 */
	public static final ArrayList<FIELD> toFieldArray(final String str)
	{	
		if(verboseLevel > 1) System.out.println("FIELD -> toFieldArray(...) -> Splitting: " + str);
		String[] strSplited = str.split(",");
		ArrayList<FIELD> fieldArray = new ArrayList<FIELD>(strSplited.length);
		for(int i = 0; i < strSplited.length; i++)
		{
			FIELD f = FIELD.valueOf(strSplited[i]);
			if(f != null)
			{
				fieldArray.add(f);
			}
		}
		
		return fieldArray;
	}
	
	public static final String toCommaSeparatedString(final ArrayList<FIELD> fields)
	{
		String s = "";
		for(final FIELD f : fields)
		{
			if( ! s.isEmpty()) s += ",";
			
			s += f.toString();
		}
		
		return s;
	}
	public static final String[] toStrings(final FIELD[] fields)
	{
		String[] ss = new String[fields.length];
		for(int i = 0; i < fields.length; i++)
		{
			ss[i] = fields[i].toString();
		}
		
		return ss;
	}


}