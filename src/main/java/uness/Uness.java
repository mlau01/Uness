package uness;

import java.text.SimpleDateFormat;

import unessdb.FIELD;

public class Uness {

	public static final String APP_NAME = "uness";
	public static final String APP_VERSION = "1.1.0";
	public static final String NCPATTERN = "NC[0-9]{7}";
	
	public static final String logTemplate = "/template/log.jrxml";
	public static final String sendRmaTemplate = "/template/sendRma.jrxml";
	public static final String returnRmaTemplate = "/template/returnRma.jrxml";
	
	public static final String resourcesPath = "/resources/";
	public static final String iconsPath = resourcesPath + "icons/";
	
	public static final int userSessionLenght = 350; //Temps de la session d'un users en seconde
	public static final int userShowedLevelMin = 0;
	public static final int userShowedLevelMax = 200;

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String currentTime = new SimpleDateFormat(DATE_FORMAT).format(System.currentTimeMillis());
	
	// Options conf
	public static final String CONF_NAME = "general-config.conf";
	public static final String CONF_DIRECTORY = System.getenv("APPDATA") + "/" + APP_NAME;
	
	public static final String MANAGE_PASSWORD = "Uni6!";


	
	public enum OPTION {
		useDb("access"),
		dbUrl(""),
		dbName(""),
		dbLogin(""),
		dbPasswd(""),
		table_devices_show("DEVICE_location,DEVICE_model,DEVICE_serial,DEVICE_idclient,DEVICE_comment"),
		table_devices_sort("DEVICE_location"),
		table_devices_sens("ASC"),
		table_log_show("LOG_datetime,LOG_author,LOG_comment"),
		table_log_sort("LOG_datetime"),
		table_log_sens("DESC"),
		table_rma_show("RMA_id"),
		table_rma_sort("RMA_id"),
		table_rma_sens("ASC"),
		table_search_show("DEVICE_location,DEVICE_model,DEVICE_serial,DEVICE_idclient,DEVICE_comment"),
		table_search_sort("DEVICE_location"),
		table_search_sens("ASC"),
		table_manage_model_show("MODEL_id"),
		table_manage_model_sort("MODEL_constructor"),
		table_manage_model_sens("ASC"),
		table_manage_location_show("LOCATION_id"),
		table_manage_location_sort("LOCATION_location"),
		table_manage_location_sens("ASC"),
		table_manage_group_show("GROUP_id"),
		table_manage_group_sort("GROUP_group"),
		table_manage_group_sens("ASC"),
		table_manage_enterprise_show("ENTERPRISE_id"),
		table_manage_enterprise_sort("ENTERPRISE_enterprise"),
		table_manage_enterprise_sens("ASC"),
		table_manage_user_show("USER_id"),
		table_manage_user_sort("USER_fullname"),
		table_manage_user_sens("ASC");
		
		private final String def;
		OPTION(final String p_def){
			def = p_def;
		}
		
		public final String getDefault()
		{
			return def;
		}
	}
	
	
	public static final FIELD[] import_device_required = {
		FIELD.DEVICE_serial,
		FIELD.DEVICE_model,
		FIELD.DEVICE_owner
	};
}
